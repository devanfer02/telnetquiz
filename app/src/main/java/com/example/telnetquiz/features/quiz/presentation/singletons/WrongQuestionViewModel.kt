package com.example.telnetquiz.features.quiz.presentation.singletons

import androidx.lifecycle.ViewModel
import com.example.telnetquiz.features.quiz.domain.model.QuizIndex

object WrongQuizManager {
    val queue: ArrayDeque<QuizIndex> = ArrayDeque()

    fun reset() {
        queue.clear()
    }
}
