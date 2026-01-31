package com.example.litecartesnative.features.pretest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litecartesnative.data.remote.dto.PretestQuestionDto
import com.example.litecartesnative.data.remote.dto.PretestSubmissionDto
import com.example.litecartesnative.data.repository.PretestRepository
import com.example.litecartesnative.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PretestState(
    val isLoading: Boolean = false,
    val questions: List<PretestQuestionDto> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val answers: Map<Int, Int> = emptyMap(), // questionId -> selectedOptionId
    val isSubmitting: Boolean = false,
    val isCompleted: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class PretestViewModel @Inject constructor(
    private val pretestRepository: PretestRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PretestState())
    val state: StateFlow<PretestState> = _state.asStateFlow()

    val currentQuestion: PretestQuestionDto?
        get() = _state.value.questions.getOrNull(_state.value.currentQuestionIndex)

    val progress: Float
        get() = if (_state.value.questions.isEmpty()) 0f
                else (_state.value.currentQuestionIndex + 1).toFloat() / _state.value.questions.size

    fun loadPretestQuestions() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = pretestRepository.getPretestQuestions()) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        questions = result.data,
                        currentQuestionIndex = 0,
                        answers = emptyMap()
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
        if (currentIndex < _state.value.questions.size - 1) {
            _state.value = _state.value.copy(currentQuestionIndex = currentIndex + 1)
        }
    }

    fun previousQuestion() {
        val currentIndex = _state.value.currentQuestionIndex
        if (currentIndex > 0) {
            _state.value = _state.value.copy(currentQuestionIndex = currentIndex - 1)
        }
    }

    fun submitPretest() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSubmitting = true, error = null)

            val submissions = _state.value.answers.map { (questionId, optionId) ->
                PretestSubmissionDto(questionId = questionId, answeredOptionId = optionId)
            }

            when (val result = pretestRepository.submitPretestAnswers(submissions)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        isCompleted = true,
                        successMessage = result.data
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

    fun resetPretest() {
        _state.value = PretestState()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
