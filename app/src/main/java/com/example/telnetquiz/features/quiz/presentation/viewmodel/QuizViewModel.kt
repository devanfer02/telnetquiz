package com.example.telnetquiz.features.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telnetquiz.data.remote.dto.QuizAnswerDto
import com.example.telnetquiz.data.remote.dto.QuizDto
import com.example.telnetquiz.data.remote.dto.QuizResultDto
import com.example.telnetquiz.data.remote.dto.VerifyAnswerResponse
import com.example.telnetquiz.data.audio.AudioManager
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.data.local.FlowResultStore
import com.example.telnetquiz.data.local.QuizFlowManager
import com.example.telnetquiz.data.repository.QuizRepository
import com.example.telnetquiz.data.repository.MaterialRepository
import com.example.telnetquiz.data.repository.Result
import com.example.telnetquiz.data.tts.TtsProvider
import com.example.telnetquiz.features.quiz.domain.model.QuizIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizState(
    val isLoading: Boolean = false,
    val quiz: QuizDto? = null,
    val currentQuestionIndex: Int = 0,
    val answers: Map<Int, Int> = emptyMap(),
    val isSubmitting: Boolean = false,
    val result: QuizResultDto? = null,
    val error: String? = null,
    val isVerifying: Boolean = false,
    val verifiedQuestions: Map<Int, VerifyAnswerResponse> = emptyMap()
)

sealed class QuizNavEvent {
    data class GoToRemedial(val wrongCount: Int, val totalCount: Int) : QuizNavEvent()
    data class GoToResult(val chapterId: Int, val level: Int) : QuizNavEvent()
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val materialRepository: MaterialRepository,
    private val ttsProvider: TtsProvider,
    private val audioManager: AudioManager,
    private val quizFlowManager: QuizFlowManager,
    private val flowResultStore: FlowResultStore
) : ViewModel() {

    val ttsLoading = ttsProvider.isLoading
    fun speak(text: String) = ttsProvider.speak(text)
    fun speakContent(type: String, id: Int, gender: Boolean?) = ttsProvider.speakContent(type, id, gender)
    fun stopTts() = ttsProvider.stop()

    fun playAnswerSfx(isCorrect: Boolean, isRetry: Boolean) {
        when {
            isRetry && isCorrect -> audioManager.playSfx(SfxType.QUESTION_REMEDIAL_RIGHT)
            isCorrect -> audioManager.playSfx(SfxType.QUESTION_RIGHT)
            else -> audioManager.playSfx(SfxType.QUESTION_WRONG)
        }
    }

    private val _state = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = _state.asStateFlow()

    private val _navEvent = MutableSharedFlow<QuizNavEvent>()
    val navEvent: SharedFlow<QuizNavEvent> = _navEvent.asSharedFlow()

    val currentQuestion
        get() = _state.value.quiz?.questions?.getOrNull(_state.value.currentQuestionIndex)

    val progress: Float
        get() {
            val questions = _state.value.quiz?.questions ?: return 0f
            if (questions.isEmpty()) return 0f
            return (_state.value.currentQuestionIndex + 1).toFloat() / questions.size
        }

    val isLastQuestion: Boolean
        get() {
            val questions = _state.value.quiz?.questions ?: return true
            return _state.value.currentQuestionIndex >= questions.size - 1
        }

    val isCurrentQuestionVerified: Boolean
        get() {
            val question = currentQuestion ?: return false
            return _state.value.verifiedQuestions.containsKey(question.id)
        }

    fun loadQuiz(quizId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = quizRepository.getQuizById(quizId)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        quiz = result.data,
                        currentQuestionIndex = 0,
                        answers = emptyMap(),
                        result = null,
                        verifiedQuestions = emptyMap()
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

    fun selectAnswer(questionId: Int, optionId: Int) {
        if (_state.value.verifiedQuestions.containsKey(questionId)) return

        _state.value = _state.value.copy(
            answers = _state.value.answers + (questionId to optionId)
        )
    }

    fun verifyCurrentAnswer() {
        val quiz = _state.value.quiz ?: return
        val question = currentQuestion ?: return
        val answeredOptionId = _state.value.answers[question.id] ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(isVerifying = true)
            when (val result = quizRepository.verifyAnswer(quiz.id, question.id, answeredOptionId)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isVerifying = false,
                        verifiedQuestions = _state.value.verifiedQuestions + (question.id to result.data)
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isVerifying = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isVerifying = true)
                }
            }
        }
    }

    fun nextQuestion() {
        stopTts()
        val currentIndex = _state.value.currentQuestionIndex
        val questions = _state.value.quiz?.questions ?: return
        if (currentIndex < questions.size - 1) {
            _state.value = _state.value.copy(currentQuestionIndex = currentIndex + 1)
        }
    }

    fun previousQuestion() {
        val currentIndex = _state.value.currentQuestionIndex
        if (currentIndex > 0) {
            _state.value = _state.value.copy(currentQuestionIndex = currentIndex - 1)
        }
    }

    fun submitQuiz() {
        val quiz = _state.value.quiz ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(isSubmitting = true, error = null)

            val answers = _state.value.answers.map { (questionId, optionId) ->
                QuizAnswerDto(questionId = questionId, answeredOptionId = optionId)
            }

            when (val result = quizRepository.submitQuizAnswers(quiz.id, answers)) {
                is Result.Success -> {
                    val quizResult = result.data

                    if (!quizResult.passed && !quizFlowManager.isRetry) {
                        val wrongIds = quizResult.wrongQuestionIds ?: emptyList()
                        val correctAnswerMap = _state.value.answers.filter { (qId, _) ->
                            qId !in wrongIds
                        }

                        quizFlowManager.setupRemedial(
                            quizId = quiz.id,
                            quizData = quiz,
                            wrongIds = wrongIds,
                            correctAnswerMap = correctAnswerMap
                        )

                        quizFlowManager.resetWrongQueue()
                        val materialIds = mutableListOf<Int>()
                        for (wrongQId in wrongIds) {
                            val question = quiz.questions.find { it.id == wrongQId }
                            val matId = question?.materialId ?: 0
                            quizFlowManager.wrongQueue.addLast(
                                QuizIndex(
                                    chapterId = quiz.chapterId,
                                    level = quiz.level,
                                    id = wrongQId,
                                    materialId = matId
                                )
                            )
                            if (matId > 0) materialIds.add(matId)
                        }

                        val uniqueMaterialIds = materialIds.distinct()
                        if (uniqueMaterialIds.isNotEmpty()) {
                            when (val matResult = materialRepository.bulkGetMaterials(uniqueMaterialIds)) {
                                is Result.Success -> {
                                    quizFlowManager.setMaterialsCache(matResult.data.associateBy { it.id })
                                }
                                else -> {}
                            }
                        }
                    }

                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        result = quizResult
                    )

                    handleResult(quiz, quizResult)
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isSubmitting = true)
                }
            }
        }
    }

    fun loadQuizForRetry() {
        val quiz = quizFlowManager.remedialQuizData ?: return
        val wrongIds = quizFlowManager.wrongQuestionIds

        val retryQuiz = quiz.copy(
            questions = quiz.questions.filter { it.id in wrongIds }
        )

        quizFlowManager.markAsRetry()

        _state.value = _state.value.copy(
            isLoading = false,
            quiz = retryQuiz,
            currentQuestionIndex = 0,
            answers = emptyMap(),
            result = null,
            verifiedQuestions = emptyMap()
        )
    }

    fun submitRetry() {
        val originalQuiz = quizFlowManager.remedialQuizData ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(isSubmitting = true, error = null)

            val allAnswers = (quizFlowManager.correctAnswers + _state.value.answers).map { (questionId, optionId) ->
                QuizAnswerDto(questionId = questionId, answeredOptionId = optionId)
            }

            when (val result = quizRepository.submitQuizAnswers(originalQuiz.id, allAnswers)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        result = result.data
                    )

                    handleResult(originalQuiz, result.data)
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isSubmitting = true)
                }
            }
        }
    }

    private suspend fun handleResult(quiz: QuizDto, result: QuizResultDto) {
        val originalQuiz = quizFlowManager.remedialQuizData ?: quiz

        if (!result.passed && !quizFlowManager.isRetry) {
            _navEvent.emit(
                QuizNavEvent.GoToRemedial(
                    wrongCount = result.wrongQuestionIds?.size ?: 0,
                    totalCount = result.totalQuestions
                )
            )
        } else {
            flowResultStore.clearQuiz()
            flowResultStore.quizResult = result
            quizFlowManager.clearRemedial()
            _navEvent.emit(
                QuizNavEvent.GoToResult(
                    chapterId = originalQuiz.chapterId,
                    level = originalQuiz.level
                )
            )
        }
    }

    fun startRemedialReview(): QuizIndex? {
        return if (quizFlowManager.wrongQueue.isNotEmpty()) {
            quizFlowManager.wrongQueue.removeFirst()
        } else null
    }

    fun resetQuiz() {
        _state.value = QuizState()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
