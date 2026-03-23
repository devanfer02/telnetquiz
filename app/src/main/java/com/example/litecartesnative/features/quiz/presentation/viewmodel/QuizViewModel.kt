package com.example.litecartesnative.features.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litecartesnative.data.remote.dto.QuizAnswerDto
import com.example.litecartesnative.data.remote.dto.QuizDto
import com.example.litecartesnative.data.remote.dto.QuizResultDto
import com.example.litecartesnative.data.remote.dto.VerifyAnswerResponse
import com.example.litecartesnative.data.repository.QuizRepository
import com.example.litecartesnative.data.repository.Result
import com.example.litecartesnative.data.tts.TtsProvider
import com.example.litecartesnative.features.quiz.domain.model.QuizIndex
import com.example.litecartesnative.features.quiz.presentation.singletons.RemedialHolder
import com.example.litecartesnative.features.quiz.presentation.singletons.WrongQuizManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizState(
    val isLoading: Boolean = false,
    val quiz: QuizDto? = null,
    val currentQuestionIndex: Int = 0,
    val answers: Map<Int, Int> = emptyMap(), // questionId -> selectedOptionId
    val isSubmitting: Boolean = false,
    val result: QuizResultDto? = null,
    val error: String? = null,
    val isVerifying: Boolean = false,
    val verifiedQuestions: Map<Int, VerifyAnswerResponse> = emptyMap() // questionId -> verification result
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val ttsProvider: TtsProvider
) : ViewModel() {

    fun speak(text: String) = ttsProvider.speak(text)
    fun stopTts() = ttsProvider.stop()

    private val _state = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = _state.asStateFlow()

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

                    // If failed and not already a retry, populate remedial state
                    if (!quizResult.passed && !RemedialHolder.isRetry) {
                        val wrongIds = quizResult.wrongQuestionIds ?: emptyList()
                        val correctAnswerMap = _state.value.answers.filter { (qId, _) ->
                            qId !in wrongIds
                        }

                        RemedialHolder.quizId = quiz.id
                        RemedialHolder.quizData = quiz
                        RemedialHolder.wrongQuestionIds = wrongIds
                        RemedialHolder.correctAnswers = correctAnswerMap
                        RemedialHolder.isRetry = false

                        // Populate WrongQuizManager queue for FeedbackScreen chain
                        WrongQuizManager.reset()
                        for (wrongQId in wrongIds) {
                            val question = quiz.questions.find { it.id == wrongQId }
                            WrongQuizManager.queue.addLast(
                                QuizIndex(
                                    chapterId = quiz.chapterId,
                                    level = quiz.level,
                                    id = wrongQId,
                                    materialId = question?.materialId ?: 0
                                )
                            )
                        }
                    }

                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        result = quizResult
                    )
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
        val holder = RemedialHolder
        val quiz = holder.quizData ?: return
        val wrongIds = holder.wrongQuestionIds

        // Filter quiz to only show wrong questions
        val retryQuiz = quiz.copy(
            questions = quiz.questions.filter { it.id in wrongIds }
        )

        RemedialHolder.isRetry = true

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
        val holder = RemedialHolder
        val originalQuiz = holder.quizData ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(isSubmitting = true, error = null)

            // Combine correct answers from first attempt + new retry answers
            val allAnswers = (holder.correctAnswers + _state.value.answers).map { (questionId, optionId) ->
                QuizAnswerDto(questionId = questionId, answeredOptionId = optionId)
            }

            when (val result = quizRepository.submitQuizAnswers(originalQuiz.id, allAnswers)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        result = result.data
                    )
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

    fun resetQuiz() {
        _state.value = QuizState()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
