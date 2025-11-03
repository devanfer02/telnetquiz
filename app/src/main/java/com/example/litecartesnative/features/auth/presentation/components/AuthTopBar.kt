package com.example.litecartesnative.features.auth.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.litecartesnative.R
import com.example.litecartesnative.ui.theme.LitecartesColor

@Composable
fun AuthTopBar(
    painter: Painter,
    contentAlignment: Alignment = Alignment.TopStart
) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .background(LitecartesColor.Primary)
            .fillMaxWidth(),
       contentAlignment = contentAlignment
    ) {
        Image(
            painter = painter,
            contentDescription = "hellow",
            modifier = Modifier
        )
    }
}