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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HASIL PRETEST",
                color = LitecartesColor.Primary,
                fontSize = 13.sp,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            MascotCircle()

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${scorePercentage.toInt()}%",
                color = LitecartesColor.Primary,
                fontSize = 44.sp,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = feedback.title,
                color = LitecartesColor.Secondary,
                fontSize = 18.sp,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = feedback.description,
                color = LitecartesColor.Secondary.copy(alpha = 0.75f),
                fontSize = 12.sp,
                fontFamily = nunitosFontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            StatsRow(correctCount = correctCount, wrongCount = wrongCount)

            if (weaknesses.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                WeaknessSection(
                    weaknesses = weaknesses,
                    onChapterClick = { chapterId ->
                        flowResultStore?.clearPretest()
                        navController.navigate("${Screen.LevelScreen.route}/$chapterId")
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ActionButtons(
                onContinueLearning = {
                    flowResultStore?.clearPretest()
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onRetry = {
                    flowResultStore?.clearPretest()
                    navController.navigate(Screen.PretestScreen.route) {
                        popUpTo(Screen.PretestResultScreen.route) { inclusive = true }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
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
                color = LitecartesColor.Primary.copy(alpha = 0.18f),
                radius = size.minDimension / 2f,
                center = Offset(size.width / 2f, size.height / 2f)
            )
            val strokeWidth = 3.dp.toPx()
            val inset = strokeWidth / 2f
            drawArc(
                color = LitecartesColor.Primary,
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
private fun StatsRow(correctCount: Int, wrongCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            accentColor = LitecartesColor.ScoreGreen,
            icon = Icons.Filled.Check,
            iconDescription = "Benar",
            count = correctCount,
            label = "Benar"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            accentColor = LitecartesColor.ScoreRed,
            icon = Icons.Filled.Close,
            iconDescription = "Salah",
            count = wrongCount,
            label = "Salah"
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    accentColor: Color,
    icon: ImageVector,
    iconDescription: String,
    count: Int,
    label: String
) {
    CardWithShadow(
        modifier = modifier,
        backgroundColor = Color.White,
        elevation = 4.dp,
        cornerRadius = 14.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = iconDescription,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "$count",
                    color = LitecartesColor.Secondary,
                    fontSize = 22.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = label,
                    color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
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
                tint = LitecartesColor.Primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "BAB YANG PERLU DIPELAJARI",
                color = LitecartesColor.Primary,
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
        backgroundColor = Color.White,
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
                    .background(LitecartesColor.ScoreRed)
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
                    color = LitecartesColor.Secondary.copy(alpha = 0.65f),
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

@Composable
private fun ActionButtons(
    onContinueLearning: () -> Unit,
    onRetry: () -> Unit
) {
    val refreshIcon = rememberVectorPainter(Icons.Filled.Refresh)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Button(
                text = "Lanjut belajar",
                color = LitecartesColor.Primary,
                backgroundColor = Color.White,
                borderColor = LitecartesColor.Primary,
                textModifier = Modifier.padding(vertical = 4.dp),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                onClick = onContinueLearning
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            Button(
                text = "Coba lagi",
                color = Color.White,
                backgroundColor = LitecartesColor.Primary,
                borderColor = LitecartesColor.Primary,
                textModifier = Modifier.padding(vertical = 4.dp),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                icon = refreshIcon,
                onClick = onRetry
            )
        }
    }
}
