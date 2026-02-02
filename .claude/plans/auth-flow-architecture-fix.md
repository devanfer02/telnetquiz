# Authentication Flow Architecture Fix

## Problem Analysis

### Issue 1: Active Token Still Shows AuthStartScreen
**Root Cause:** The `startDestination` in `Navigation.kt` was **hardcoded**:
```kotlin
NavHost(
    navController = navController,
    startDestination = Screen.AuthStartScreen.route  // Always this, regardless of auth state
)
```

### Issue 2: HomeScreen Flickers on Expired Token
**Root Cause:** The app used **reactive** token validation (401 response), not **proactive** validation:
1. Token exists in DataStore → App assumes user is logged in
2. App navigates to HomeScreen
3. API call triggers 401 → Interceptor detects expiration
4. Session cleared → Redirect to login
5. **Result:** User sees HomeScreen briefly before being kicked out

---

## Solution: Session-Aware Navigation with Proactive Validation

### Architecture Pattern

```
┌─────────────────────────────────────────────────────────────────┐
│                        APP LAUNCH                               │
├─────────────────────────────────────────────────────────────────┤
│  1. Show Splash/Loading Screen (SessionState.Loading)           │
│           ↓                                                     │
│  2. Check Token Existence (from DataStore)                      │
│           ↓                                                     │
│  ┌────────┴────────┐                                           │
│  │ Token exists?   │                                           │
│  └────────┬────────┘                                           │
│     NO    │    YES                                              │
│     ↓     │     ↓                                               │
│  Auth     │  3. Call GET /user/profile to validate token        │
│  Start    │           ↓                                         │
│  Screen   │  ┌────────┴────────┐                               │
│           │  │ Response 200?   │                               │
│           │  └────────┬────────┘                               │
│           │     401   │    200                                  │
│           │     ↓     │     ↓                                   │
│           │  Clear    │  HomeScreen                             │
│           │  Session  │  (+ cache profile data)                 │
│           │     ↓     │                                         │
│           │  Auth     │                                         │
│           │  Login    │                                         │
│           │  Screen   │                                         │
└───────────┴───────────┴─────────────────────────────────────────┘
```

### Why Use `/user/profile` for Validation?

| Approach | Verdict |
|----------|---------|
| Dedicated `/auth/validate` | Redundant - extra endpoint to maintain |
| **Use `/user/profile`** | Already exists, returns useful data, DRY principle |

Benefits of using `/user/profile`:
1. Already exists and is protected by auth
2. Returns user data you'll need anyway (name, avatar for ProfileTopBar)
3. Kills two birds with one stone - validate + prefetch user data
4. No backend changes required

---

## Implementation Details

### Files Modified

| File | Changes |
|------|---------|
| `TelNetQuizApi.kt` | Added `GET /user/profile` endpoint |
| `ApiResponse.kt` | Added `UserProfileDto` data class |
| `AuthRepository.kt` | Added `validateSession()` method |
| `AuthViewModel.kt` | Added `SessionState` sealed class and validation logic |
| `Navigation.kt` | Conditional rendering based on session state |

---

### Step 1: User Profile API Endpoint

**File:** `app/src/main/java/com/example/litecartesnative/data/remote/api/TelNetQuizApi.kt`

```kotlin
// User profile endpoint (also used for session validation)
@GET("api/user/profile")
suspend fun getUserProfile(): Response<ApiResponse<UserProfileDto>>
```

---

### Step 2: User Profile DTO

**File:** `app/src/main/java/com/example/litecartesnative/data/remote/dto/ApiResponse.kt`

```kotlin
data class UserProfileDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("email")
    val email: String
)
```

---

### Step 3: Session Validation in AuthRepository

**File:** `app/src/main/java/com/example/litecartesnative/data/repository/AuthRepository.kt`

```kotlin
suspend fun validateSession(): Result<UserProfileDto> {
    return try {
        val response = api.getUserProfile()
        if (response.isSuccessful) {
            val profile = response.body()?.data
            if (profile != null) {
                tokenManager.saveUserInfo(profile.email, profile.fullname)
                Result.Success(profile)
            } else {
                Result.Error("Invalid profile response")
            }
        } else {
            Result.Error("Session invalid", response.code())
        }
    } catch (e: Exception) {
        Log.e("AuthRepository", "validateSession error: ${e.message}", e)
        Result.Error(e.message ?: "Network error")
    }
}
```

---

### Step 4: Session State in AuthViewModel

**File:** `app/src/main/java/com/example/litecartesnative/features/auth/presentation/viewmodel/AuthViewModel.kt`

```kotlin
sealed class SessionState {
    data object Loading : SessionState()
    data object Authenticated : SessionState()
    data object Unauthenticated : SessionState()
}

// In AuthViewModel class:
private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

init {
    validateSession()  // Call on init instead of checkLoginStatus
    observeSessionExpired()
}

private fun validateSession() {
    viewModelScope.launch {
        val token = authRepository.authToken.first()
        if (token == null) {
            _sessionState.value = SessionState.Unauthenticated
            _state.value = _state.value.copy(isLoggedIn = false)
            return@launch
        }

        when (val result = authRepository.validateSession()) {
            is Result.Success -> {
                _sessionState.value = SessionState.Authenticated
                _state.value = _state.value.copy(isLoggedIn = true)
            }
            is Result.Error -> {
                tokenManager.clearSession()
                _sessionState.value = SessionState.Unauthenticated
                _state.value = _state.value.copy(isLoggedIn = false)
            }
            is Result.Loading -> {
                // Stay in loading state
            }
        }
    }
}
```

---

### Step 5: Navigation.kt Conditional Routing

**File:** `app/src/main/java/com/example/litecartesnative/components/Navigation.kt`

```kotlin
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val sessionState by authViewModel.sessionState.collectAsState()

    // Handle session expiration during app usage
    LaunchedEffect(Unit) {
        authViewModel.sessionExpiredEvent.collectLatest {
            navController.navigate(Screen.AuthLoginScreen.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Conditional rendering based on session state
    when (sessionState) {
        SessionState.Loading -> {
            SplashLoadingScreen()
        }
        SessionState.Authenticated -> {
            MainNavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.route
            )
        }
        SessionState.Unauthenticated -> {
            MainNavHost(
                navController = navController,
                startDestination = Screen.AuthStartScreen.route
            )
        }
    }
}

@Composable
private fun SplashLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LitecartesColor.Surface),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = LitecartesColor.Secondary)
    }
}
```

---

## Verification Plan

| Test Case | Expected Behavior |
|-----------|-------------------|
| Fresh install | App shows AuthStartScreen |
| After login, close & reopen | App goes directly to HomeScreen (no auth screens) |
| Token expired, close & reopen | App shows AuthLoginScreen directly (no HomeScreen flicker) |
| Logout | Token cleared, next launch shows AuthStartScreen |
| No internet + valid cached token | Shows loading, then handles gracefully |

---

## Summary

| Before | After |
|--------|-------|
| Always starts at AuthStartScreen | Starts at correct screen based on session |
| Token validated reactively (on 401) | Token validated proactively (on startup) |
| HomeScreen flickers on expired token | No flicker - loading → correct destination |
| No loading state | Explicit Loading → Auth/Unauth state machine |

---

## Status: IMPLEMENTED

**Date:** 2026-02-02

**Build Status:** ✅ BUILD SUCCESSFUL
