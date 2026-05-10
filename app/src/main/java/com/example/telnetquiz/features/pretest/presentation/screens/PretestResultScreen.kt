package com.example.telnetquiz.features.pretest.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.components.ScoreCountRow
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.audio.AudioManager
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.data.local.FlowResultStore
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
            description = "Masih banyak materi yang perlu dipelajari. Mulai dari bab-bab di bawah ini dan coba lagi nanti!"
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

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Primary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        vertical = 40.dp,
                        horizontal = 20.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "HASIL PRETEST",
                    color = LitecartesColor.Surface,
                    fontSize = 28.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.result)
                        .build(),
                    contentDescription = "result",
                    modifier = Modifier.size(300.dp)
                )
                Text(
                    text = "${scorePercentage.toInt()}%",
                    color = Color.White,
                    fontSize = 48.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.padding(8.dp))
                val feedback = getFeedback(scorePercentage)
                Text(
                    text = feedback.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = feedback.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                ScoreCountRow(
                    correctCount = correctCount,
                    wrongCount = wrongCount
                )
                if (wrongCount > 0 && weaknesses.isNotEmpty()) {
                    Spacer(modifier = Modifier.padding(12.dp))
                    Text(
                        text = "Bab yang perlu dipelajari lagi:",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    weaknesses.forEach { weakness ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(LitecartesColor.Surface.copy(alpha = 0.15f))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = weakness.chapterTitle,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = nunitosFontFamily,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${weakness.totalQuestions - weakness.wrongCount}/${weakness.totalQuestions} benar",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontFamily = nunitosFontFamily
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    text = "Lanjutkan",
                    borderColor = LitecartesColor.Secondary,
                    color = LitecartesColor.Surface,
                    backgroundColor = LitecartesColor.Secondary,
                    textModifier = Modifier.padding(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
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
