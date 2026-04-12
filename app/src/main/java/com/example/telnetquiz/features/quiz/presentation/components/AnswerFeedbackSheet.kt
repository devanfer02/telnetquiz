package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.R
import com.example.telnetquiz.components.PretestButton
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

private val feedbackGreen = Color(0xFF4CAF50)
private val feedbackRed = Color(0xFFE53935)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerFeedbackSheet(
    isCorrect: Boolean,
    isLastQuestion: Boolean,
    onDismiss: () -> Unit,
    onContinue: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = LitecartesColor.Surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isCorrect) "Jawaban benar!" else "Jawaban salah!",
                fontFamily = nunitosFontFamily,
                fontSize = 20.sp,
                color = if (isCorrect) feedbackGreen else feedbackRed,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = if (isCorrect) R.drawable.chap1 else R.drawable.mascot_wrong),
                contentDescription = "",
                modifier = Modifier.size(200.dp)
            )
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                PretestButton(
                    text = if (isLastQuestion) "Selesai" else "Lanjut",
                    backgroundColor = LitecartesColor.Secondary,
                    textColor = LitecartesColor.Surface,
                    onClick = onContinue
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAnswerFeedbackSheetCorrect() {
    AnswerFeedbackSheet(
        isCorrect = true,
        isLastQuestion = false,
        onDismiss = {},
        onContinue = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAnswerFeedbackSheetWrong() {
    AnswerFeedbackSheet(
        isCorrect = false,
        isLastQuestion = true,
        onDismiss = {},
        onContinue = {}
    )
}
