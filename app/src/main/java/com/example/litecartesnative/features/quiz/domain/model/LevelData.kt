package com.example.litecartesnative.features.quiz.domain.model

import androidx.compose.ui.unit.Dp

data class LevelData(
    val level: Int,
    val onClick: () -> Unit,
    val x: Dp,
    val y: Dp
)