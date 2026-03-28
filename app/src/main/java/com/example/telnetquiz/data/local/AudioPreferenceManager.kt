package com.example.telnetquiz.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPreferenceManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val AUDIO_MUTED_KEY = booleanPreferencesKey("audio_muted")
    }

    val isMuted: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUDIO_MUTED_KEY] ?: false
    }

    suspend fun setMuted(muted: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUDIO_MUTED_KEY] = muted
        }
    }
}
