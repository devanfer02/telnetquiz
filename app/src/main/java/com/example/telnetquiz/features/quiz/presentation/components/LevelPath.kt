package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import com.example.telnetquiz.features.quiz.domain.model.LevelData
import com.example.telnetquiz.ui.theme.LitecartesColor

@Composable
fun LevelPath(
    levels: List<LevelData>,
    pathAnimationProgress: Float,
    buttonCenterOffsetPx: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        clipRect(
            top = 0f,
            bottom = canvasHeight * pathAnimationProgress,
            left = 0f,
            right = canvasWidth
        ) {
            for (i in 0 until levels.size - 1) {
                val from = levels[i]
                val to = levels[i + 1]

                val startX = canvasWidth * from.xFraction
                val startY = canvasHeight * from.yFraction + buttonCenterOffsetPx
                val endX = canvasWidth * to.xFraction
                val endY = canvasHeight * to.yFraction + buttonCenterOffsetPx

                val midY = (startY + endY) / 2f
                val path = Path().apply {
                    moveTo(startX, startY)
                    cubicTo(startX, midY, endX, midY, endX, endY)
                }

                translate(top = 4f) {
                    drawPath(
                        path = path,
                        color = Color.Black.copy(alpha = 0.12f),
                        style = Stroke(width = 72f, cap = StrokeCap.Round)
                    )
                }
                drawPath(
                    path = path,
                    color = LitecartesColor.PathColor.copy(alpha = 0.5f),
                    style = Stroke(width = 64f, cap = StrokeCap.Round)
                )
                drawPath(
                    path = path,
                    color = LitecartesColor.PathColor,
                    style = Stroke(width = 44f, cap = StrokeCap.Round)
                )
            }
        }
    }
}
