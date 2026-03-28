package com.example.telnetquiz.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AvatarPreferenceManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val SELECTED_AVATAR_INDEX_KEY = intPreferencesKey("selected_avatar_index")
        const val NO_AVATAR_SELECTED = -1
    }

    val selectedAvatarIndex: Flow<Int> = dataStore.data.map { preferences ->
        preferences[SELECTED_AVATAR_INDEX_KEY] ?: NO_AVATAR_SELECTED
    }

    suspend fun setSelectedAvatarIndex(index: Int) {
        dataStore.edit { preferences ->
            preferences[SELECTED_AVATAR_INDEX_KEY] = index
        }
    }

    suspend fun clearSelection() {
        setSelectedAvatarIndex(NO_AVATAR_SELECTED)
    }
}
