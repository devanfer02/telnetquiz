package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import com.example.telnetquiz.ui.theme.scoreColor

@Composable
fun LevelButton(
    level: Int,
    onClick: () -> Unit = {},
    onLockedClick: () -> Unit = {},
    done: Boolean = false,
    isLocked: Boolean = false,
    score: Int? = null
) {
    OutlinedButton(
        onClick = { if (!isLocked) onClick() else onLockedClick() },
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        modifier = Modifier
            .size(50.dp),
        border = BorderStroke(
            5.dp,
            if (isLocked) Color.Gray.copy(alpha = 0.5f) else LitecartesColor.Secondary
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                isLocked -> Color.Gray.copy(alpha = 0.3f)
                done && score != null -> scoreColor(score)
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
                text = "L$level",
                color = if (done) {
                    LitecartesColor.PathColor
                } else {
                    LitecartesColor.Secondary
                },
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.5.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewLevelButton() {
    LevelButton(level = 1)
}
