package com.example.telnetquiz.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
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
        targetValue = if (isActive || feedback != OptionFeedback.NONE) 1.02f else 1f,
        label = "scale"
    )

    val feedbackGreen = Color(0xFF4CAF50)
    val feedbackRed = Color(0xFFE53935)

    val badgeColor = when (feedback) {
        OptionFeedback.CORRECT -> feedbackGreen
        OptionFeedback.WRONG -> feedbackRed
        OptionFeedback.NONE -> if (isActive) LitecartesColor.Primary else LitecartesColor.Secondary
    }

    val borderColor = when (feedback) {
        OptionFeedback.CORRECT -> feedbackGreen
        OptionFeedback.WRONG -> feedbackRed
        OptionFeedback.NONE -> if (isActive) LitecartesColor.Primary else LitecartesColor.Secondary
    }

    val containerColor = when (feedback) {
        OptionFeedback.CORRECT -> feedbackGreen.copy(alpha = 0.15f)
        OptionFeedback.WRONG -> feedbackRed.copy(alpha = 0.15f)
        OptionFeedback.NONE -> if (isActive) LitecartesColor.Secondary else LitecartesColor.DarkerSurface
    }

    val textColor = when (feedback) {
        OptionFeedback.CORRECT -> feedbackGreen
        OptionFeedback.WRONG -> feedbackRed
        OptionFeedback.NONE -> if (isActive) LitecartesColor.Surface else LitecartesColor.Secondary
    }

    Row(
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .fillMaxWidth()
            .scale(scale),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (letter != ' ') {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = nunitosFontFamily
                )
            }
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(2.dp)
                    .background(borderColor)
            )
        }
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = {
                haptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            },
            enabled = feedback == OptionFeedback.NONE,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(
                width = if (isActive || feedback != OptionFeedback.NONE) 2.dp else 1.dp,
                color = borderColor
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                disabledContainerColor = containerColor
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = if (isActive || feedback != OptionFeedback.NONE) 4.dp else 2.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = text,
                    color = textColor,
                    fontFamily = nunitosFontFamily,
                    fontWeight = if (isActive || feedback != OptionFeedback.NONE) FontWeight.Bold else FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (isActive && feedback == OptionFeedback.NONE) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = LitecartesColor.Secondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewOptionButton() {
    OptionButton(
        text = "Ini Opsi A",
        letter = 'A'
    )
}

@Preview
@Composable
fun PreviewOptionButtonActive() {
    OptionButton(
        text = "Ini Opsi B (Active)",
        letter = 'B',
        isActive = true
    )
}

@Preview
@Composable
fun PreviewOptionButtonCorrect() {
    OptionButton(
        text = "Ini Opsi C (Correct)",
        letter = 'C',
        feedback = OptionFeedback.CORRECT
    )
}

@Preview
@Composable
fun PreviewOptionButtonWrong() {
    OptionButton(
        text = "Ini Opsi D (Wrong)",
        letter = 'D',
        feedback = OptionFeedback.WRONG
    )
}
