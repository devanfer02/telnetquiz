package com.example.litecartesnative.features.quiz.domain.model

data class Chapter(
    val title: String,
    val description: String,
    val imageLink: Int,
    val levels: List<List<Question>>
)