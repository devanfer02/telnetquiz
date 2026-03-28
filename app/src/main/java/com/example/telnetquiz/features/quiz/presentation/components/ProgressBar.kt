package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.ui.theme.LitecartesColor

@Composable
fun ProgressBarFromApi(
    current: Int,
    total: Int
) {
    val progress = if (total > 0) current.toFloat() / total.toFloat() else 0f

    Column {
        Row(
            modifier = Modifier
                .background(LitecartesColor.Primary)
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .background(LitecartesColor.Primary)
                    .drawBehind {
                        drawRoundRect(
                            color = LitecartesColor.Surface,
                            cornerRadius = CornerRadius(40.dp.toPx()),
                            style = Stroke(
                                width = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progress)
                        .background(LitecartesColor.Surface, RoundedCornerShape(40))
                )
            }
        }
        Divider(
            color = Color(0xFFFFD4B8).copy(alpha = 0.25f),
            thickness = 0.5.dp,
        )
    }
}

@Preview
@Composable
fun PreviewProgressBarFromApi() {
    ProgressBarFromApi(
        current = 3,
        total = 6
    )
}