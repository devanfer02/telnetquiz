package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

private fun scoreColor(score: Int): Color = when {
    score >= 100 -> Color(0xFF2196F3)
    score >= 80 -> Color(0xFF4CAF50)
    score >= 60 -> Color(0xFFFFEB3B)
    score >= 40 -> Color(0xFFFF9800)
    else -> Color(0xFFE53935)
}

@Composable
fun LevelButton(
    level: Int,
    onClick: () -> Unit = {},
    done: Boolean = false,
    isLocked: Boolean = false,
    score: Int? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = { if (!isLocked) onClick() },
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape,
            enabled = !isLocked,
            modifier = Modifier
                .size(50.dp),
            border = BorderStroke(
                5.dp,
                if (isLocked) Color.Gray.copy(alpha = 0.5f) else LitecartesColor.Secondary
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = when {
                    isLocked -> Color.Gray.copy(alpha = 0.3f)
                    done -> LitecartesColor.Primary
                    else -> LitecartesColor.Surface
                },
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            )
        ) {
            if (isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "$level",
                    color = if (done) {
                        LitecartesColor.PathColor
                    } else {
                        LitecartesColor.Secondary
                    },
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
        if (score != null) {
            Text(
                text = "$score",
                color = scoreColor(score),
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewLevelButton() {
    LevelButton(level = 1)
}
