package com.example.litecartesnative.features.quiz.domain.model

data class LevelData(
    val level: Int,
    val onClick: () -> Unit = {},
    val xFraction: Float,  // 0.0 to 1.0 of screen width
    val yFraction: Float   // 0.0 to 1.0 of content height
)
