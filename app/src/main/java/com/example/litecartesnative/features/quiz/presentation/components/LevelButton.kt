package com.example.litecartesnative.features.quiz.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun LevelButton(
    level: Int,
    onClick: () -> Unit = {},
    done: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        modifier = Modifier
            .size(50.dp)
            ,
        border = BorderStroke(5.dp, LitecartesColor.Secondary),
//        border = BorderStroke(5.dp, LitecartesColor.Primary),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(done) {
                LitecartesColor.Primary
            } else {
                LitecartesColor.Surface
            }
//                    containerColor = LitecartesColor.PathColor
        )
    ) {
        Text(
            text = "$level",
            color = if(done) {
                LitecartesColor.PathColor
            } else {
                LitecartesColor.Secondary
            },
//            color = LitecartesColor.Secondary,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Preview
@Composable
fun PreviewLevelButton() {
    LevelButton(level = 1)
}