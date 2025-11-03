package com.example.litecartesnative.features.quiz.domain.model

data class Question(
    val title: String,
    val imageId: Int? = null,
    val description: String,
    val question: String,
    val options: List<String>,
    val answer: String,
    val material: Material? = null
)