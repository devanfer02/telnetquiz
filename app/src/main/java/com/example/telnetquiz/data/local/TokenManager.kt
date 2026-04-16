package com.example.telnetquiz.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val PREFS_NAME = "secure_token_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _authToken = MutableStateFlow(prefs.getString(KEY_AUTH_TOKEN, null))
    private val _refreshToken = MutableStateFlow(prefs.getString(KEY_REFRESH_TOKEN, null))
    private val _userEmail = MutableStateFlow(prefs.getString(KEY_USER_EMAIL, null))
    private val _userName = MutableStateFlow(prefs.getString(KEY_USER_NAME, null))

    private val _sessionExpired = MutableSharedFlow<Unit>(replay = 0)
    val sessionExpired: SharedFlow<Unit> = _sessionExpired.asSharedFlow()

    val authToken: Flow<String?> = _authToken.asStateFlow()
    val refreshToken: Flow<String?> = _refreshToken.asStateFlow()
    val userEmail: Flow<String?> = _userEmail.asStateFlow()
    val userName: Flow<String?> = _userName.asStateFlow()

    fun getAuthTokenSync(): String? = _authToken.value
    fun getRefreshTokenSync(): String? = _refreshToken.value

    suspend fun saveAuthToken(token: String) {
        prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
        _authToken.value = token
    }

    suspend fun saveRefreshToken(token: String) {
        prefs.edit().putString(KEY_REFRESH_TOKEN, token).apply()
        _refreshToken.value = token
    }

    suspend fun saveUserInfo(email: String, name: String) {
        prefs.edit()
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_NAME, name)
            .apply()
        _userEmail.value = email
        _userName.value = name
    }

    suspend fun clearSession() {
        prefs.edit()
            .remove(KEY_AUTH_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_NAME)
            .apply()
        _authToken.value = null
        _refreshToken.value = null
        _userEmail.value = null
        _userName.value = null
    }

    suspend fun onSessionExpired() {
        if (_authToken.value == null) return
        clearSession()
        _sessionExpired.emit(Unit)
    }
}
