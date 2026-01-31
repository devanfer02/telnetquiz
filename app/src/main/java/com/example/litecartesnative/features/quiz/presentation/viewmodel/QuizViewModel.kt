package com.example.litecartesnative.features.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litecartesnative.data.remote.dto.QuizAnswerDto
import com.example.litecartesnative.data.remote.dto.QuizDto
import com.example.litecartesnative.data.remote.dto.QuizResultDto
import com.example.litecartesnative.data.repository.QuizRepository
import com.example.litecartesnative.data.repository.Result
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
    val error: String? = null
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

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
                        result = null
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
        _state.value = _state.value.copy(
            answers = _state.value.answers + (questionId to optionId)
        )
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
