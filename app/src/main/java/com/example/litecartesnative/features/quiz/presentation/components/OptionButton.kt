package com.example.litecartesnative.features.quiz.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun OptionButton(
    text: String,
    letter: Char = ' ',
    isActive: Boolean = false,
    onClick: () -> Unit = {}
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.02f else 1f,
        label = "scale"
    )

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
                    .background(
                        if (isActive) LitecartesColor.Primary else LitecartesColor.Secondary
                    ),
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
            Spacer(modifier = Modifier.width(12.dp))
        }
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(
                width = if (isActive) 2.dp else 1.dp,
                color = if (isActive) LitecartesColor.Primary else LitecartesColor.Secondary
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isActive) {
                    LitecartesColor.Secondary
                } else {
                    LitecartesColor.DarkerSurface
                }
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = if (isActive) 4.dp else 2.dp
            )
        ) {
            Text(
                text = text,
                color = if (isActive) {
                    LitecartesColor.Surface
                } else {
                    LitecartesColor.Secondary
                },
                fontFamily = nunitosFontFamily,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
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
