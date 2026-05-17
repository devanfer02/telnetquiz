package com.example.telnetquiz.features.pretest.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.telnetquiz.R
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.components.StatRow
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.audio.AudioManager
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.data.local.FlowResultStore
import com.example.telnetquiz.data.remote.dto.ChapterWeaknessDto
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

private data class PretestFeedback(
    val title: String,
    val description: String
)

private fun getFeedback(scorePercentage: Double): PretestFeedback {
    return when {
        scorePercentage >= 90 -> PretestFeedback(
            title = "Luar Biasa! 🌟",
            description = "Kamu memiliki pemahaman yang sangat baik! Tetap pertahankan dan terus belajar."
        )
        scorePercentage >= 75 -> PretestFeedback(
            title = "Bagus! 👍",
            description = "Pemahaman kamu sudah cukup baik. Pelajari lagi beberapa bab untuk hasil yang lebih maksimal."
        )
        scorePercentage >= 40 -> PretestFeedback(
            title = "Perlu Ditingkatkan 📚",
            description = "Ada beberapa materi yang perlu kamu pelajari lebih dalam. Jangan menyerah, terus berlatih!"
        )
        else -> PretestFeedback(
            title = "Ayo Semangat! 💪",
            description = "Masih banyak materi yang perlu dipelajari. Mulai dari bab-bab di bawah dan coba lagi nanti!"
        )
    }
}

@Composable
fun PretestResultScreen(
    navController: NavController,
    audioManager: AudioManager? = null,
    flowResultStore: FlowResultStore? = null
) {
    LaunchedEffect(Unit) {
        audioManager?.playSfx(SfxType.PRETEST_RESULT)
    }

    val result = flowResultStore?.pretestResult
    val correctCount = result?.correctAnswers ?: 0
    val wrongCount = result?.incorrectAnswers ?: 0
    val scorePercentage = result?.scorePercentage ?: 0.0
    val weaknesses = result?.chapterWeaknesses ?: emptyList()
    val feedback = getFeedback(scorePercentage)

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = LitecartesColor.Primary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Primary)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 32.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HASIL PRETEST",
                    color = LitecartesColor.Surface,
                    fontSize = 13.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.weight(0.5f))

                MascotCircle()

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${scorePercentage.toInt()}%",
                    color = Color.White,
                    fontSize = 44.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = feedback.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = feedback.description,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontFamily = nunitosFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                StatRow(correctCount = correctCount, wrongCount = wrongCount)

                if (weaknesses.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    WeaknessSection(
                        weaknesses = weaknesses,
                        onChapterClick = { chapterId ->
                            flowResultStore?.clearPretest()
                            navController.navigate("${Screen.LevelScreen.route}/$chapterId")
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(0.4f))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    text = "Lanjut",
                    color = LitecartesColor.Secondary,
                    backgroundColor = LitecartesColor.Surface,
                    borderColor = LitecartesColor.Secondary,
                    textModifier = Modifier.padding(vertical = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    onClick = {
                        flowResultStore?.clearPretest()
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun MascotCircle() {
    Box(
        modifier = Modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = LitecartesColor.Surface.copy(alpha = 0.25f),
                radius = size.minDimension / 2f,
                center = Offset(size.width / 2f, size.height / 2f)
            )
            val strokeWidth = 3.dp.toPx()
            val inset = strokeWidth / 2f
            drawArc(
                color = LitecartesColor.Surface,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), 0f)
                )
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.result)
                .build(),
            contentDescription = "Maskot motivasi",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun WeaknessSection(
    weaknesses: List<ChapterWeaknessDto>,
    onChapterClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.MenuBook,
                contentDescription = null,
                tint = LitecartesColor.Surface,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "BAB YANG PERLU DIPELAJARI",
                color = LitecartesColor.Surface,
                fontSize = 12.sp,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }
        weaknesses.forEach { weakness ->
            WeaknessCard(weakness = weakness, onClick = { onChapterClick(weakness.chapterId) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun WeaknessCard(weakness: ChapterWeaknessDto, onClick: () -> Unit) {
    val correct = weakness.totalQuestions - weakness.wrongCount
    CardWithShadow(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = LitecartesColor.Surface,
        elevation = 3.dp,
        cornerRadius = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(LitecartesColor.ScoreBlue)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = weakness.chapterTitle,
                    color = LitecartesColor.Secondary,
                    fontSize = 13.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
                Text(
                    text = "$correct/${weakness.totalQuestions} benar",
                    color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = LitecartesColor.Secondary.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp)
            )
        }
    }
}
