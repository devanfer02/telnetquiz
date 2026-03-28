package com.example.telnetquiz.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.ui.theme.LitecartesColor

@Composable
fun CardWithShadow(
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp,
    cornerRadius: Dp = 16.dp,
    backgroundColor: Color = LitecartesColor.DarkerSurface,
    ambientColor: Color = Color.Black,
    spotColor: Color = Color.Black,
    borderStroke: BorderStroke? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                clip = false,
                ambientColor = ambientColor,
                spotColor = spotColor
            )
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (borderStroke != null) Modifier.border(borderStroke, shape)
                else Modifier
            ),
        content = content
    )
}
