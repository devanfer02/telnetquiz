package com.example.telnetquiz.features.quiz.presentation.util

import com.example.telnetquiz.features.quiz.domain.model.LevelData
import kotlin.random.Random

fun generateLevelPositions(count: Int, chapterId: Int): List<LevelData> {
    if (count == 0) return emptyList()

    val random = Random(seed = chapterId)
    val positions = mutableListOf<LevelData>()
    val yStart = 0.07f
    val yEnd = 0.78f
    val yStep = if (count > 1) (yEnd - yStart) / (count - 1) else 0f

    for (i in 0 until count) {
        val yFraction = yStart + (i * yStep)
        val xFraction = if (i % 2 == 0) {
            random.nextFloat() * 0.25f + 0.15f
        } else {
            random.nextFloat() * 0.25f + 0.55f
        }
        positions.add(LevelData(level = i + 1, xFraction = xFraction, yFraction = yFraction))
    }

    return positions
}
