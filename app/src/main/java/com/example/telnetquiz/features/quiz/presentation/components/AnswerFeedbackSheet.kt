package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.R
import com.example.telnetquiz.components.PretestButton
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
    val accent = if (isCorrect) feedbackGreen else feedbackRed

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MascotInRing(isCorrect = isCorrect, accent = accent)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (isCorrect) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isCorrect) "Jawaban benar!" else "Belum tepat",
                    color = accent,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isCorrect)
                    "Mantap! Pemahamanmu makin tajam, lanjut ke soal berikutnya."
                else
                    "Jangan menyerah, kamu pasti bisa di soal berikutnya!",
                color = Color(0xFF555555),
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            PretestButton(
                text = if (isLastQuestion) "Selesai" else "Lanjut",
                backgroundColor = accent,
                textColor = Color.White,
                onClick = onContinue
            )
        }
    }
}

@Composable
private fun MascotInRing(isCorrect: Boolean, accent: Color) {
    val ringSize = 140.dp
    Box(
        modifier = Modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(ringSize)) {
            val strokeWidth = 3.dp.toPx()
            val inset = strokeWidth / 2f
            drawArc(
                color = accent,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                )
            )
        }
        Box(
            modifier = Modifier
                .size(ringSize - 14.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.06f)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(
                    id = if (isCorrect) R.drawable.chap1 else R.drawable.mascot_wrong
                ),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
        FeedbackSparkle(Alignment.TopStart, 12.dp, 10.dp, 10.dp, accent)
        FeedbackSparkle(Alignment.TopEnd, (-14).dp, 22.dp, 8.dp, accent)
        FeedbackSparkle(Alignment.CenterStart, 2.dp, 0.dp, 7.dp, accent)
        FeedbackSparkle(Alignment.CenterEnd, (-6).dp, (-24).dp, 9.dp, accent)
        FeedbackSparkle(Alignment.BottomStart, 18.dp, (-18).dp, 8.dp, accent)
        FeedbackSparkle(Alignment.BottomEnd, (-16).dp, (-12).dp, 10.dp, accent)
    }
}

@Composable
private fun BoxScope.FeedbackSparkle(
    alignment: Alignment,
    offsetX: Dp,
    offsetY: Dp,
    size: Dp,
    tint: Color
) {
    Icon(
        imageVector = Icons.Filled.AutoAwesome,
        contentDescription = null,
        tint = tint.copy(alpha = 0.75f),
        modifier = Modifier
            .align(alignment)
            .offset(x = offsetX, y = offsetY)
            .size(size)
    )
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
