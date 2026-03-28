package com.example.telnetquiz.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ProgressBarFromApi(
    current: Int,
    total: Int,
    containerColor: Color = LitecartesColor.Surface,
    barColor: Color = LitecartesColor.Primary,
    borderColor: Color = LitecartesColor.Secondary,
    useDashedBorder: Boolean = false,
    showLabel: Boolean = true,
    showDivider: Boolean = false
) {
    val progress = if (total > 0) current.toFloat() / total.toFloat() else 0f

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(containerColor)
                .padding(
                    horizontal = 20.dp,
                    vertical = if (showLabel) 40.dp else 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .background(containerColor)
                    .then(
                        if (useDashedBorder) {
                            Modifier.drawBehind {
                                drawRoundRect(
                                    color = borderColor,
                                    cornerRadius = CornerRadius(40.dp.toPx()),
                                    style = Stroke(
                                        width = 2f,
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(10f, 10f), 0f
                                        )
                                    )
                                )
                            }
                        } else {
                            Modifier.border(
                                BorderStroke(2.dp, borderColor),
                                RoundedCornerShape(40)
                            )
                        }
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progress)
                        .background(barColor, RoundedCornerShape(40))
                )
            }
            if (showLabel) {
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "${current}/${total}",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = LitecartesColor.DarkBrown
                )
            }
        }
        if (showDivider) {
            Divider(
                color = Color(0xFFFFD4B8).copy(alpha = 0.25f),
                thickness = 0.5.dp,
            )
        }
    }
}
