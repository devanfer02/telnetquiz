package com.example.telnetquiz.features.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telnetquiz.data.remote.dto.StudyMaterialDto
import com.example.telnetquiz.data.audio.AudioManager
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.data.repository.MaterialRepository
import com.example.telnetquiz.data.repository.Result
import com.example.telnetquiz.data.tts.TtsProvider
import com.example.telnetquiz.features.quiz.presentation.singletons.RemedialHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudyMaterialState(
    val isLoading: Boolean = false,
    val material: StudyMaterialDto? = null,
    val error: String? = null
)

@HiltViewModel
class StudyMaterialViewModel @Inject constructor(
    private val materialRepository: MaterialRepository,
    private val ttsProvider: TtsProvider,
    val audioManager: AudioManager
) : ViewModel() {

    fun speak(text: String) = ttsProvider.speak(text)
    fun stopTts() = ttsProvider.stop()

    private val _state = MutableStateFlow(StudyMaterialState())
    val state: StateFlow<StudyMaterialState> = _state.asStateFlow()

    fun loadMaterial(materialId: Int) {
        val cached = RemedialHolder.materialsCache[materialId]
        if (cached != null) {
            _state.value = _state.value.copy(isLoading = false, material = cached)
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = materialRepository.getStudyMaterial(materialId)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        material = result.data
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
