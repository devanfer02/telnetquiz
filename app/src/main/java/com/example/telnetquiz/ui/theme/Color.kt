package com.example.telnetquiz.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

object LitecartesColor {
    val Primary = Color(0xFFF37704)
    val Secondary = Color(0xFF8B340D)
    val Tertiary = Color(0xFFFF542E)
    val Surface = Color(0xFFFFDAB7)
    val DarkerSurface = Color(0xFFF9BD85)
    val DarkBrown = Color(0xFF662500)
    val PathColor = Color(0xFFF5E9B8)
    val GreenCactus = Color(0xFF588432)

    val ScoreBlue = Color(0xFF2196F3)
    val ScoreGreen = Color(0xFF4CAF50)
    val ScoreYellow = Color(0xFFFFEB3B)
    val ScoreOrange = Color(0xFFFF9800)
    val ScoreRed = Color(0xFFE53935)
}

fun scoreColor(score: Int): Color = when {
    score >= 100 -> LitecartesColor.ScoreBlue
    score >= 80 -> LitecartesColor.ScoreGreen
    score >= 60 -> LitecartesColor.ScoreYellow
    score >= 40 -> LitecartesColor.ScoreOrange
    else -> LitecartesColor.ScoreRed
}

