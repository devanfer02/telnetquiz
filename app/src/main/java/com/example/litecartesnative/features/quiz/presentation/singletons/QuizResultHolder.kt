package com.example.litecartesnative.features.quiz.presentation.singletons

import com.example.litecartesnative.data.remote.dto.QuizResultDto

object QuizResultHolder {
    var lastResult: QuizResultDto? = null

    fun clear() {
        lastResult = null
    }
}
