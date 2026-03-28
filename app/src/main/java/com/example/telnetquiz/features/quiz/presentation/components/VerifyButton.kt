package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun VerifyButton(
    isVisible: Boolean,
    isVerifying: Boolean,
    isSubmitting: Boolean,
    isLastQuestion: Boolean,
    onVerify: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        OutlinedButton(
            modifier = modifier
                .padding(5.dp)
                .fillMaxWidth(),
            onClick = onVerify,
            enabled = !isVerifying,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, LitecartesColor.DarkBrown),
            colors = ButtonDefaults.buttonColors(
                containerColor = LitecartesColor.DarkBrown
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
        ) {
            if (isVerifying) {
                CircularProgressIndicator(
                    color = LitecartesColor.Surface,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = if (isLastQuestion) "Selesai" else "Lanjutkan",
                    color = LitecartesColor.Surface,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    if (isSubmitting) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = LitecartesColor.Secondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewVerifyButton() {
    LitecartesNativeTheme {
        VerifyButton(
            isVisible = true,
            isVerifying = false,
            isSubmitting = false,
            isLastQuestion = false,
            onVerify = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewVerifyButtonLast() {
    LitecartesNativeTheme {
        VerifyButton(
            isVisible = true,
            isVerifying = false,
            isSubmitting = false,
            isLastQuestion = true,
            onVerify = {}
        )
    }
}
