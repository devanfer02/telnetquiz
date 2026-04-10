package com.example.telnetquiz.data.local

import com.example.telnetquiz.data.remote.dto.PretestResultDto
import com.example.telnetquiz.data.remote.dto.QuizResultDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlowResultStore @Inject constructor() {
    var quizResult: QuizResultDto? = null
    var pretestResult: PretestResultDto? = null

    fun clearQuiz() {
        quizResult = null
    }

    fun clearPretest() {
        pretestResult = null
    }
}
