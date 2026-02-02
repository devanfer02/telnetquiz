# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run Android instrumented tests
./gradlew connectedAndroidTest

# Clean and rebuild
./gradlew clean build

# Run lint checks
./gradlew lint
```

## Architecture

This is a native Android app built with Kotlin and Jetpack Compose for an educational geometry quiz application ("Geomatruiz").

**Tech Stack:**
- Kotlin 1.9.0 with Jetpack Compose
- Navigation Compose for routing
- Hilt for dependency injection (configured but minimally used)
- Arrow-kt for functional programming utilities
- Coil for image loading
- Target SDK 34, Min SDK 24

**Code Organization:**

The app follows a feature-based architecture under `app/src/main/java/com/example/litecartesnative/`:

- `components/` - Shared UI components (Button, Navbar, Navigation, StrokedText)
- `constants/` - Route definitions (Screen sealed class), static quiz data, navigation items
- `features/` - Feature modules, each with `presentation/` (screens, components) and optionally `domain/` (models)
  - `auth/` - Authentication screens (start, login, register, about)
  - `chapter/` - Chapter selection screen
  - `quiz/` - Quiz functionality (levels, questions, results, feedback)
  - `pretest/` - Pretest functionality
  - `user/` - Profile, leaderboard, friends
- `ui/theme/` - Material 3 theming (LitecartesColor palette, typography with Nunito font)

**Navigation:**

Routes defined as sealed class objects in `constants/Screen.kt`. The NavHost in `components/Navigation.kt` handles all routing with typed arguments:
- Simple routes: `Screen.AuthStartScreen.route`
- Routes with args: `${Screen.LevelScreen.route}/{id}` or `${Screen.QuestionScreen.route}/{chapterId}/levels/{level}/questions/{id}`

**State Management:**

- Singleton objects for global state: `WrongQuizManager` (tracks wrong answers in ArrayDeque), `MarkAsDoneManager` (2D BooleanArray for completed levels)
- Local state via Compose `remember { mutableStateOf() }`
- No ViewModel/LiveData pattern in use

**Data:**

Quiz content is hardcoded in `constants/` (chaptersData, pretestsData, levelsData). Models in `features/*/domain/model/` are data classes (Chapter, LevelData, Question, Material, QuizIndex).
