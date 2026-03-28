package com.example.telnetquiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.ui.theme.LitecartesColor

@Composable
fun TopBarContainer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 0.dp,
    backgroundColor: Color = LitecartesColor.Primary,
    backgroundBrush: Brush? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius)
    Box(
        modifier = modifier
            .then(
                if (elevation > 0.dp) Modifier.shadow(elevation = elevation, shape = shape)
                else Modifier
            )
            .clip(shape)
            .then(
                if (backgroundBrush != null) Modifier.background(backgroundBrush)
                else Modifier.background(backgroundColor)
            ),
        contentAlignment = contentAlignment,
        content = content
    )
}
