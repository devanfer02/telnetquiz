package com.example.telnetquiz.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun rememberShimmerColor(
    baseColor: Color = Color.White,
    initialAlpha: Float = 0.3f,
    targetAlpha: Float = 0.6f,
    durationMillis: Int = 800
): Color {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = initialAlpha,
        targetValue = targetAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    return baseColor.copy(alpha = alpha)
}

@Composable
fun Modifier.shimmerEffect(
    baseColor: Color = Color(0xFFE0AE78),
    highlightColor: Color = Color(0xFFF7DDB8),
    durationMillis: Int = 1200
): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    return this.background(
        brush = Brush.linearGradient(
            colors = listOf(baseColor, highlightColor, baseColor),
            start = Offset(translateAnim, 0f),
            end = Offset(translateAnim + 400f, 0f)
        )
    )
}

@Composable
fun Modifier.shimmerOnPrimary(
    durationMillis: Int = 1200
): Modifier = shimmerEffect(
    baseColor = Color.White.copy(alpha = 0.2f),
    highlightColor = Color.White.copy(alpha = 0.5f),
    durationMillis = durationMillis
)
