package com.example.telnetquiz.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

enum class OptionFeedback {
    NONE, CORRECT, WRONG
}

@Composable
fun OptionButton(
    text: String,
    letter: Char = ' ',
    isActive: Boolean = false,
    feedback: OptionFeedback = OptionFeedback.NONE,
    onClick: () -> Unit = {},
    haptic: HapticFeedback? = null
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.01f else 1f,
        label = "scale"
    )

    val feedbackGreen = Color(0xFF4CAF50)
    val feedbackRed = Color(0xFFE53935)
    val feedbackGreenFill = Color(0xFFEAF6EA)
    val feedbackRedFill = Color(0xFFFCE6E6)

    val containerColor = when (feedback) {
        OptionFeedback.CORRECT -> feedbackGreenFill
        OptionFeedback.WRONG -> feedbackRedFill
        OptionFeedback.NONE -> if (isActive) LitecartesColor.Primary else Color.White
    }

    val borderColor = when (feedback) {
        OptionFeedback.CORRECT -> feedbackGreen
        OptionFeedback.WRONG -> feedbackRed
        OptionFeedback.NONE -> if (isActive) LitecartesColor.Primary
        else LitecartesColor.Secondary.copy(alpha = 0.12f)
    }

    val textColor = when (feedback) {
        OptionFeedback.CORRECT -> feedbackGreen
        OptionFeedback.WRONG -> feedbackRed
        OptionFeedback.NONE -> if (isActive) Color.White else LitecartesColor.Secondary
    }

    val letterBg = when (feedback) {
        OptionFeedback.CORRECT -> Color.White
        OptionFeedback.WRONG -> Color.White
        OptionFeedback.NONE -> if (isActive) Color.White
        else LitecartesColor.DarkerSurface
    }

    val letterColor = when (feedback) {
        OptionFeedback.CORRECT -> feedbackGreen
        OptionFeedback.WRONG -> feedbackRed
        OptionFeedback.NONE -> LitecartesColor.Primary
    }

    val isInteractive = feedback == OptionFeedback.NONE
    val isFeedback = feedback != OptionFeedback.NONE

    Row(
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .fillMaxWidth()
            .scale(scale)
            .then(
                if (isFeedback) Modifier else Modifier.shadow(
                    elevation = if (isActive) 4.dp else 2.dp,
                    shape = RoundedCornerShape(14.dp)
                )
            )
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .border(
                width = if (isActive || isFeedback) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .then(
                if (isInteractive) Modifier.clickable {
                    haptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                } else Modifier
            )
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (letter != ' ') {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(letterBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter.toString(),
                    color = letterColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    fontFamily = nunitosFontFamily
                )
            }
        }
        Text(
            text = text,
            color = textColor,
            fontFamily = nunitosFontFamily,
            fontWeight = if (isActive || feedback != OptionFeedback.NONE) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = 13.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        )
        if (isActive && feedback == OptionFeedback.NONE) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = LitecartesColor.Primary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewOptionButton() {
    OptionButton(text = "Menyimpan data di dalam hard disk komputer", letter = 'A')
}

@Preview
@Composable
fun PreviewOptionButtonActive() {
    OptionButton(
        text = "Mengatur komunikasi data antar perangkat dalam jaringan",
        letter = 'B',
        isActive = true
    )
}

@Preview
@Composable
fun PreviewOptionButtonCorrect() {
    OptionButton(text = "Ini Opsi C (Correct)", letter = 'C', feedback = OptionFeedback.CORRECT)
}

@Preview
@Composable
fun PreviewOptionButtonWrong() {
    OptionButton(text = "Ini Opsi D (Wrong)", letter = 'D', feedback = OptionFeedback.WRONG)
}
