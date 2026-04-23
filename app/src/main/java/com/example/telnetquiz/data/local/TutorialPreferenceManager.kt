package com.example.telnetquiz.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.telnetquiz.components.tutorial.TutorialSegmentId
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TutorialPreferenceManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val tokenManager: TokenManager
) {
    private fun completedKey(id: TutorialSegmentId, userKey: String) =
        booleanPreferencesKey("user_${userKey}_segment_${id.name}_completed")

    private fun stepKey(id: TutorialSegmentId, userKey: String) =
        intPreferencesKey("user_${userKey}_segment_${id.name}_step")

    private suspend fun currentUserKey(): String? =
        tokenManager.userEmail.first()?.takeIf { it.isNotBlank() }

    suspend fun isSegmentCompleted(id: TutorialSegmentId): Boolean {
        val userKey = currentUserKey() ?: return true
        return dataStore.data.first()[completedKey(id, userKey)] ?: false
    }

    suspend fun getSegmentStep(id: TutorialSegmentId): Int {
        val userKey = currentUserKey() ?: return 0
        return dataStore.data.first()[stepKey(id, userKey)] ?: 0
    }

    suspend fun setSegmentStep(id: TutorialSegmentId, index: Int) {
        val userKey = currentUserKey() ?: return
        dataStore.edit { it[stepKey(id, userKey)] = index }
    }

    suspend fun setSegmentCompleted(id: TutorialSegmentId) {
        val userKey = currentUserKey() ?: return
        dataStore.edit {
            it[completedKey(id, userKey)] = true
            it.remove(stepKey(id, userKey))
        }
    }

    suspend fun resetSegment(id: TutorialSegmentId) {
        val userKey = currentUserKey() ?: return
        dataStore.edit {
            it.remove(completedKey(id, userKey))
            it.remove(stepKey(id, userKey))
        }
    }
}
