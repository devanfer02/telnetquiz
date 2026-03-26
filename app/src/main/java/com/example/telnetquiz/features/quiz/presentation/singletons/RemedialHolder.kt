package com.example.telnetquiz.features.quiz.presentation.singletons

import com.example.telnetquiz.data.remote.dto.QuizDto
import com.example.telnetquiz.data.remote.dto.StudyMaterialDto

object RemedialHolder {
    var quizId: Int = 0
    var quizData: QuizDto? = null
    var wrongQuestionIds: List<Int> = emptyList()
    var correctAnswers: Map<Int, Int> = emptyMap() // questionId -> optionId (correct from first attempt)
    var isRetry: Boolean = false
    var materialsCache: Map<Int, StudyMaterialDto> = emptyMap() // materialId -> material

    fun clear() {
        quizId = 0
        quizData = null
        wrongQuestionIds = emptyList()
        correctAnswers = emptyMap()
        isRetry = false
        materialsCache = emptyMap()
    }
}
