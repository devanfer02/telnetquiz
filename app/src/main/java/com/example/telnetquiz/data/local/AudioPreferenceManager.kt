package com.example.telnetquiz.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
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
        private val GLOBAL_VOLUME_KEY = floatPreferencesKey("global_volume")
        private val SFX_VOLUME_KEY = floatPreferencesKey("sfx_volume")
        private val BG_MUSIC_VOLUME_KEY = floatPreferencesKey("bg_music_volume")
    }

    val isMuted: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUDIO_MUTED_KEY] ?: false
    }

    val audioSettings: Flow<AudioSettings> = dataStore.data.map { preferences ->
        AudioSettings(
            isMuted = preferences[AUDIO_MUTED_KEY] ?: false,
            globalVolume = preferences[GLOBAL_VOLUME_KEY] ?: 1f,
            sfxVolume = preferences[SFX_VOLUME_KEY] ?: 1f,
            bgMusicVolume = preferences[BG_MUSIC_VOLUME_KEY] ?: 1f
        )
    }

    suspend fun setMuted(muted: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUDIO_MUTED_KEY] = muted
        }
    }

    suspend fun setGlobalVolume(volume: Float) {
        dataStore.edit { preferences ->
            preferences[GLOBAL_VOLUME_KEY] = volume.coerceIn(0f, 1f)
        }
    }

    suspend fun setSfxVolume(volume: Float) {
        dataStore.edit { preferences ->
            preferences[SFX_VOLUME_KEY] = volume.coerceIn(0f, 1f)
        }
    }

    suspend fun setBgMusicVolume(volume: Float) {
        dataStore.edit { preferences ->
            preferences[BG_MUSIC_VOLUME_KEY] = volume.coerceIn(0f, 1f)
        }
    }
}
