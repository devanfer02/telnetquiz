package com.example.telnetquiz.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SkeletonBox(
    height: Dp,
    modifier: Modifier = Modifier,
    width: Dp? = null,
    cornerRadius: Dp = 4.dp,
    onPrimary: Boolean = false
) {
    val shimmer: @Composable Modifier.() -> Modifier = {
        if (onPrimary) shimmerOnPrimary() else shimmerEffect()
    }

    Box(
        modifier = modifier
            .then(if (width != null) Modifier.width(width) else Modifier)
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .shimmer()
    )
}
