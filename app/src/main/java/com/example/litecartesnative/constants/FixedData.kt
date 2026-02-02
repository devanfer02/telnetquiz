package com.example.litecartesnative.constants

import com.example.litecartesnative.R
import com.example.litecartesnative.features.quiz.domain.model.Chapter
import com.example.litecartesnative.features.quiz.domain.model.LevelData

// Fractions calculated based on Pixel 8 dimensions (~412dp width, ~800dp content height)
// Original values converted: x/412 for xFraction, y/800 for yFraction
val levelsData = listOf(
    LevelData(
        level = 1,
        xFraction = 0.073f,   // was 30.dp
        yFraction = 0.05f     // was 40.dp
    ),
    LevelData(
        level = 2,
        xFraction = 0.413f,   // was 170.dp
        yFraction = 0.137f    // was 110.dp
    ),
    LevelData(
        level = 3,
        xFraction = 0.825f,   // was 340.dp
        yFraction = 0.20f     // was 160.dp
    ),
    LevelData(
        level = 4,
        xFraction = 0.728f,   // was 300.dp
        yFraction = 0.40f     // was 320.dp
    ),
    LevelData(
        level = 5,
        xFraction = 0.388f,   // was 160.dp
        yFraction = 0.40f     // was 320.dp
    ),
    LevelData(
        level = 6,
        xFraction = 0.121f,   // was 50.dp
        yFraction = 0.50f     // was 400.dp
    ),
    LevelData(
        level = 7,
        xFraction = 0.437f,   // was 180.dp
        yFraction = 0.612f    // was 490.dp
    ),
    LevelData(
        level = 8,
        xFraction = 0.801f,   // was 330.dp
        yFraction = 0.675f    // was 540.dp
    ),
    LevelData(
        level = 9,
        xFraction = 0.485f,   // was 200.dp
        yFraction = 0.85f     // was 680.dp
    ),
    LevelData(
        level = 10,
        xFraction = 0.097f,   // was 40.dp
        yFraction = 0.887f    // was 710.dp
    )
)
