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
class TutorialPreferenceManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val TUTORIAL_COMPLETED_KEY = booleanPreferencesKey("tutorial_completed")
    }

    val hasCompletedTutorial: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TUTORIAL_COMPLETED_KEY] ?: false
    }

    suspend fun setTutorialCompleted() {
        dataStore.edit { preferences ->
            preferences[TUTORIAL_COMPLETED_KEY] = true
        }
    }
}
