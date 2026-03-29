package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
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
    pathAnimationProgress: () -> Float,
    buttonCenterOffsetPx: Float,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier.drawWithCache {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val paths = levels.windowed(2) { (from, to) ->
                val startX = canvasWidth * from.xFraction
                val startY = canvasHeight * from.yFraction + buttonCenterOffsetPx
                val endX = canvasWidth * to.xFraction
                val endY = canvasHeight * to.yFraction + buttonCenterOffsetPx
                val midY = (startY + endY) / 2f
                Path().apply {
                    moveTo(startX, startY)
                    cubicTo(startX, midY, endX, midY, endX, endY)
                }
            }

            val shadow = Stroke(width = 72f, cap = StrokeCap.Round)
            val outer = Stroke(width = 64f, cap = StrokeCap.Round)
            val inner = Stroke(width = 44f, cap = StrokeCap.Round)
            val shadowColor = Color.Black.copy(alpha = 0.12f)
            val outerColor = LitecartesColor.PathColor.copy(alpha = 0.5f)
            val innerColor = LitecartesColor.PathColor

            onDrawBehind {
                val progress = pathAnimationProgress()
                clipRect(
                    top = 0f,
                    bottom = canvasHeight * progress,
                    left = 0f,
                    right = canvasWidth
                ) {
                    for (path in paths) {
                        translate(top = 4f) {
                            drawPath(path = path, color = shadowColor, style = shadow)
                        }
                        drawPath(path = path, color = outerColor, style = outer)
                        drawPath(path = path, color = innerColor, style = inner)
                    }
                }
            }
        }
    )
}
