package com.example.telnetquiz.features.quiz.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.audio.AudioManager
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.data.remote.dto.QuizResultDto
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ResultScreen(
    navController: NavController,
    chapterId: Int,
    quizResult: QuizResultDto? = null,
    audioManager: AudioManager? = null
) {
    val correctCount = quizResult?.correctAnswers ?: 0
    val wrongCount = (quizResult?.totalQuestions ?: 0) - correctCount
    val scorePercentage = quizResult?.scorePercentage ?: 0.0
    val passed = quizResult?.passed ?: true

    val titleText = if (passed) "Sempurna" else "Coba Lagi"
    val diamondReward = if (passed) (scorePercentage * 0.15).toInt().coerceAtLeast(5) else 0

    LaunchedEffect(Unit) {
        if (passed) audioManager?.playSfx(SfxType.RESULT_SUCCESS)
        else audioManager?.playSfx(SfxType.RESULT_FAIL)
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Surface),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false
                    )
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .background(LitecartesColor.Primary)
                    .padding(
                        vertical = 40.dp,
                        horizontal = 20.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titleText.uppercase(),
                    color = LitecartesColor.Surface,
                    fontSize = 28.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )
                Image(
                    painter = painterResource(id = R.drawable.result),
                    contentDescription = "result",
                    modifier = Modifier
                        .size(300.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(LitecartesColor.Surface)
                            .padding(
                                horizontal = 30.dp,
                                vertical = 10.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_benar),
                            contentDescription = "correct icon",
                            modifier = Modifier
                                .size(35.dp)
                        )
                        Text(
                            text = "$correctCount",
                            color = LitecartesColor.Primary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(24.dp)
                    )
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(LitecartesColor.Surface)
                            .padding(
                                horizontal = 30.dp,
                                vertical = 10.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_salah),
                            contentDescription = "wrong icon",
                            modifier = Modifier
                                .size(35.dp)
                        )
                        Text(
                            text = "$wrongCount",
                            color = LitecartesColor.Primary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                if (diamondReward > 0) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(LitecartesColor.Surface)
                            .padding(
                                vertical = 10.dp,
                                horizontal = 14.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Yeay kamu mendapatkan ",
                            color = LitecartesColor.Secondary,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = " +$diamondReward ",
                            color = LitecartesColor.Primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Image(
                            painter = painterResource(id = R.drawable.diamon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Button(
                    text = "Lanjutkan",
                    borderColor = LitecartesColor.Secondary,
                    color = LitecartesColor.Surface,
                    backgroundColor = LitecartesColor.Secondary,
                    textModifier = Modifier.padding(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 32.dp
                        ),
                    onClick = {
                        navController.navigate(
                            "${Screen.LevelScreen.route}/${chapterId}"
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewResultScreen() {
    LitecartesNativeTheme {
        ResultScreen(
            navController = rememberNavController(),
            chapterId = 0
        )
    }
}
