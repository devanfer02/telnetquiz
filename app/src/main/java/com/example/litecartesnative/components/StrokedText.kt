package com.example.litecartesnative.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun StrokedText(
    text: String,
    color: Color,
    strokeColor: Color,
    fontSize: TextUnit,
    textAlign: TextAlign?,
    lineHeight: TextUnit = 0.sp
) {
    Box {
        Text(
            text= text,
            color = color,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.Black,
            textAlign = textAlign,
            style = TextStyle(
                lineHeight = lineHeight
            ),
            fontSize = fontSize
        )
        Text(
            text = text,
            color = strokeColor,
            style = TextStyle.Default.copy(
                drawStyle = Stroke(
                    miter = 5f,
                    width = 1.5f,
                    join = StrokeJoin.Round,
                ),
                lineHeight = lineHeight
            ),
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.Black,
            textAlign = textAlign,
            fontSize = fontSize
        )
    }
}