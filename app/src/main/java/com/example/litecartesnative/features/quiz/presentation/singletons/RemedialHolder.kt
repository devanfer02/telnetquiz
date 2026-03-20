package com.example.litecartesnative.features.quiz.presentation.singletons

import com.example.litecartesnative.data.remote.dto.QuizDto

object RemedialHolder {
    var quizId: Int = 0
    var quizData: QuizDto? = null
    var wrongQuestionIds: List<Int> = emptyList()
    var correctAnswers: Map<Int, Int> = emptyMap() // questionId -> optionId (correct from first attempt)
    var isRetry: Boolean = false

    fun clear() {
        quizId = 0
        quizData = null
        wrongQuestionIds = emptyList()
        correctAnswers = emptyMap()
        isRetry = false
    }
}
