package com.example.telnetquiz.features.auth.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.example.telnetquiz.components.TopBarContainer

@Composable
fun AuthTopBar(
    painter: Painter,
    contentAlignment: Alignment = Alignment.TopStart
) {
    TopBarContainer(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = contentAlignment
    ) {
        Image(
            painter = painter,
            contentDescription = "hellow",
            modifier = Modifier
        )
    }
}
