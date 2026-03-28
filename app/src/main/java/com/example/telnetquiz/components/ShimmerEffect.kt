package com.example.telnetquiz.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.telnetquiz.ui.theme.LitecartesColor

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
fun rememberSurfaceShimmerColor(): Color = rememberShimmerColor(
    baseColor = LitecartesColor.DarkerSurface,
    initialAlpha = 0.4f,
    targetAlpha = 0.7f
)
