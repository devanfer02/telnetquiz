package com.example.telnetquiz.features.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telnetquiz.data.remote.dto.StudyMaterialDto
import com.example.telnetquiz.data.audio.AudioManager
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.data.local.QuizFlowManager
import com.example.telnetquiz.data.repository.MaterialRepository
import com.example.telnetquiz.data.repository.Result
import com.example.telnetquiz.data.tts.TtsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudyMaterialState(
    val isLoading: Boolean = false,
    val material: StudyMaterialDto? = null,
    val error: String? = null
)

sealed class StudyMaterialNavEvent {
    data class NextWrongQuestion(val chapterId: Int, val level: Int, val questionId: Int, val materialId: Int) : StudyMaterialNavEvent()
    data class NextLearnFirstMaterial(val chapterId: Int, val level: Int, val materialId: Int) : StudyMaterialNavEvent()
    data class StartQuiz(val quizId: Int) : StudyMaterialNavEvent()
    data class RetryQuiz(val quizId: Int) : StudyMaterialNavEvent()
}

@HiltViewModel
class StudyMaterialViewModel @Inject constructor(
    private val materialRepository: MaterialRepository,
    private val ttsProvider: TtsProvider,
    private val audioManager: AudioManager,
    private val quizFlowManager: QuizFlowManager
) : ViewModel() {

    val ttsLoading = ttsProvider.isLoading
    fun speak(text: String) = ttsProvider.speak(text)
    fun speakContent(type: String, id: Int, gender: Boolean?) = ttsProvider.speakContent(type, id, gender)
    fun stopTts() = ttsProvider.stop()

    private val _state = MutableStateFlow(StudyMaterialState())
    val state: StateFlow<StudyMaterialState> = _state.asStateFlow()

    private val _navEvent = MutableSharedFlow<StudyMaterialNavEvent>()
    val navEvent: SharedFlow<StudyMaterialNavEvent> = _navEvent.asSharedFlow()

    val buttonText: String
        get() = when {
            quizFlowManager.wrongQueue.isNotEmpty() -> "Lanjut Belajar"
            quizFlowManager.hasNextMaterial() -> "Lanjut Belajar"
            quizFlowManager.isLearnFirstActive() -> "Mulai Kuis"
            else -> "Ayo Coba Lagi!"
        }

    fun loadMaterial(materialId: Int) {
        val cached = quizFlowManager.materialsCache[materialId]
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

    fun onContinue() {
        viewModelScope.launch {
            when {
                quizFlowManager.wrongQueue.isNotEmpty() -> {
                    val next = quizFlowManager.wrongQueue.removeFirst()
                    _navEvent.emit(
                        StudyMaterialNavEvent.NextWrongQuestion(
                            chapterId = next.chapterId,
                            level = next.level,
                            questionId = next.id,
                            materialId = next.materialId
                        )
                    )
                }
                quizFlowManager.hasNextMaterial() -> {
                    val nextMaterial = quizFlowManager.nextMaterial()!!
                    _navEvent.emit(
                        StudyMaterialNavEvent.NextLearnFirstMaterial(
                            chapterId = quizFlowManager.learnFirstChapterId,
                            level = quizFlowManager.learnFirstLevel,
                            materialId = nextMaterial.id
                        )
                    )
                }
                quizFlowManager.isLearnFirstActive() -> {
                    audioManager.playSfx(SfxType.START_LEVEL)
                    val quizId = quizFlowManager.learnFirstQuizId
                    quizFlowManager.clearLearnFirst()
                    _navEvent.emit(StudyMaterialNavEvent.StartQuiz(quizId))
                }
                else -> {
                    val quizId = quizFlowManager.remedialQuizId
                    _navEvent.emit(StudyMaterialNavEvent.RetryQuiz(quizId))
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
