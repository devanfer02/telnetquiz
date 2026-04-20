package com.example.telnetquiz.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.telnetquiz.components.tutorial.TutorialSegmentId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TutorialPreferenceManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private fun completedKey(id: TutorialSegmentId) =
        booleanPreferencesKey("segment_${id.name}_completed")

    private fun stepKey(id: TutorialSegmentId) =
        intPreferencesKey("segment_${id.name}_step")

    fun segmentCompletedFlow(id: TutorialSegmentId): Flow<Boolean> =
        dataStore.data.map { it[completedKey(id)] ?: false }

    suspend fun isSegmentCompleted(id: TutorialSegmentId): Boolean =
        dataStore.data.first()[completedKey(id)] ?: false

    suspend fun getSegmentStep(id: TutorialSegmentId): Int =
        dataStore.data.first()[stepKey(id)] ?: 0

    suspend fun setSegmentStep(id: TutorialSegmentId, index: Int) {
        dataStore.edit { it[stepKey(id)] = index }
    }

    suspend fun setSegmentCompleted(id: TutorialSegmentId) {
        dataStore.edit {
            it[completedKey(id)] = true
            it.remove(stepKey(id))
        }
    }
}
