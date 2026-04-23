package com.example.telnetquiz.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingPreferenceManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val tokenManager: TokenManager
) {
    private fun seenKey(userKey: String) =
        booleanPreferencesKey("user_${userKey}_panduan_umum_seen")

    private suspend fun currentUserKey(): String? =
        tokenManager.userEmail.first()?.takeIf { it.isNotBlank() }

    suspend fun hasSeenPanduanUmum(): Boolean {
        val userKey = currentUserKey() ?: return true
        return dataStore.data.first()[seenKey(userKey)] ?: false
    }

    suspend fun markPanduanUmumSeen() {
        val userKey = currentUserKey() ?: return
        dataStore.edit { it[seenKey(userKey)] = true }
    }
}
