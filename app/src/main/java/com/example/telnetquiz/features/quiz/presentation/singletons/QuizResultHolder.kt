package com.example.telnetquiz.features.quiz.presentation.singletons

import com.example.telnetquiz.data.remote.dto.QuizResultDto

object QuizResultHolder {
    var lastResult: QuizResultDto? = null

    fun clear() {
        lastResult = null
    }
}
