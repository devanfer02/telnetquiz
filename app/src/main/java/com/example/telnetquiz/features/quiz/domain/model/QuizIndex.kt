package com.example.telnetquiz.features.quiz.domain.model

data class QuizIndex(
    val chapterId: Int,
    val level: Int,
    val id: Int,
    val materialId: Int = 0
)