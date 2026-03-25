package com.example.telnetquiz.features.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telnetquiz.data.remote.dto.StudyMaterialDto
import com.example.telnetquiz.data.repository.Result
import com.example.telnetquiz.data.repository.UserRepository
import com.example.telnetquiz.data.tts.TtsProvider
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
    private val userRepository: UserRepository,
    private val ttsProvider: TtsProvider
) : ViewModel() {

    fun speak(text: String) = ttsProvider.speak(text)
    fun stopTts() = ttsProvider.stop()

    private val _state = MutableStateFlow(StudyMaterialState())
    val state: StateFlow<StudyMaterialState> = _state.asStateFlow()

    fun loadMaterial(materialId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = userRepository.getStudyMaterial(materialId)) {
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
