package com.example.telnetquiz.features.quiz.presentation.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.telnetquiz.R
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.components.StatRow
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.audio.AudioManager
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.data.remote.dto.QuizResultDto
import com.example.telnetquiz.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class ResultCopy(
    val badgeLabel: String,
    val title: String,
    val subtitle: String? = null
)

private fun copyFor(passed: Boolean, scorePercentage: Double): ResultCopy = when {
    !passed -> ResultCopy(
        badgeLabel = "BELUM LULUS",
        title = "COBA LAGI",
        subtitle = "Setiap tantangan bikin kamu makin kuat, Penjelajah. Pelajari lagi materinya dan tunjukkan kemampuanmu di percobaan berikutnya!"
    )
    scorePercentage >= 100.0 -> ResultCopy(
        badgeLabel = "SKOR SEMPURNA",
        title = "SEMPURNA",
        subtitle = "Luar biasa! Pemahamanmu top banget, Penjelajah. Lanjutkan petualangan ke level berikutnya!"
    )
    scorePercentage >= 80.0 -> ResultCopy(
        badgeLabel = "BAGUS BANGET",
        title = "BAGUS!",
        subtitle = "Kerja bagus! Kamu makin jago, Penjelajah. Lanjutkan momentum ini ke level berikutnya!"
    )
    else -> ResultCopy(
        badgeLabel = "LULUS",
        title = "BERHASIL!",
        subtitle = "Selamat, kamu berhasil melewati level ini! Lanjutkan petualanganmu, Penjelajah!"
    )
}

@Composable
fun ResultScreen(
    navController: NavController,
    chapterId: Int,
    level: Int = 0,
    quizResult: QuizResultDto? = null,
    audioManager: AudioManager? = null,
    chapterViewModel: ChapterViewModel = hiltViewModel()
) {
    val correctCount = quizResult?.correctAnswers ?: 0
    val wrongCount = (quizResult?.totalQuestions ?: 0) - correctCount
    val scorePercentage = quizResult?.scorePercentage ?: 0.0
    val passed = quizResult?.passed ?: true
    val tutorialController = LocalTutorialController.current
    val copy = copyFor(passed, scorePercentage)
    val diamondReward = if (passed) scorePercentage.toInt() else 0
    val chapterDetailState by chapterViewModel.detailState.collectAsState()
    val nextQuiz = chapterDetailState.chapter?.quizzes
        ?.sortedBy { it.level }
        ?.firstOrNull { it.level > level }
    val hasNextLevel = passed && nextQuiz != null

    LaunchedEffect(chapterId) {
        chapterViewModel.loadChapterById(chapterId)
    }

    LaunchedEffect(Unit) {
        if (passed) audioManager?.playSfx(SfxType.RESULT_SUCCESS)
        else audioManager?.playSfx(SfxType.RESULT_FAIL)
    }

    val onBackToLevelMap: () -> Unit = {
        tutorialController?.notifyTargetClicked("result_back_to_map_btn")
        navController.navigate("${Screen.LevelScreen.route}/${chapterId}") {
            popUpTo("${Screen.LevelScreen.route}/${chapterId}") { inclusive = true }
        }
    }

    val onContinueToNextLevel: () -> Unit = {
        nextQuiz?.let { quiz ->
            navController.navigate("${Screen.QuestionScreen.route}/${quiz.id}") {
                popUpTo("${Screen.LevelScreen.route}/${chapterId}") { inclusive = false }
            }
        }
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = LitecartesColor.Surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Surface)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BadgePill(label = copy.badgeLabel, passed = passed)

                Spacer(modifier = Modifier.weight(0.6f))

                MascotWithParticles(passed = passed)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = copy.title,
                    color = LitecartesColor.Secondary,
                    fontSize = 36.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )

                if (level > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "LEVEL $level",
                        color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                }

                copy.subtitle?.let { motivation ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = motivation,
                        color = LitecartesColor.Secondary.copy(alpha = 0.75f),
                        fontSize = 13.sp,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = if (tutorialController != null) Modifier.onGloballyPositioned {
                        tutorialController.registerTarget("result_score_section", it)
                    } else Modifier
                ) {
                    StatRow(
                        correctCount = correctCount,
                        wrongCount = wrongCount,
                        cardBackground = LitecartesColor.DarkerSurface
                    )
                }

                if (diamondReward > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    DiamondRewardChip(amount = diamondReward)
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
                if (hasNextLevel) {
                    Button(
                        text = "Lanjut ke level berikutnya",
                        color = LitecartesColor.Surface,
                        backgroundColor = LitecartesColor.Primary,
                        borderColor = LitecartesColor.Primary,
                        textModifier = Modifier.padding(vertical = 6.dp),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        onClick = onContinueToNextLevel
                    )
                }

                val backButtonIsPrimary = !hasNextLevel
                Button(
                    text = "Kembali ke peta level",
                    color = if (backButtonIsPrimary) LitecartesColor.Surface else LitecartesColor.Primary,
                    backgroundColor = if (backButtonIsPrimary) LitecartesColor.Primary else LitecartesColor.Surface,
                    borderColor = LitecartesColor.Primary,
                    textModifier = Modifier.padding(vertical = if (backButtonIsPrimary) 6.dp else 4.dp),
                    fontSize = if (backButtonIsPrimary) 14.sp else 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .then(
                            if (tutorialController != null) Modifier.onGloballyPositioned {
                                tutorialController.registerTarget("result_back_to_map_btn", it)
                            } else Modifier
                        ),
                    onClick = onBackToLevelMap
                )
            }
        }
    }
}

@Composable
private fun BadgePill(label: String, passed: Boolean) {
    val bg = if (passed) LitecartesColor.Primary else LitecartesColor.ScoreRed
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.EmojiEvents,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
private fun MascotWithParticles(passed: Boolean) {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        if (passed) {
            ParticleField(modifier = Modifier.fillMaxSize())
        }
        MascotCircle()
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
            contentDescription = "Maskot merayakan",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
        )
    }
}

private data class Particle(
    val angleDeg: Float,
    val radiusFraction: Float,
    val sizeDp: Int,
    val color: Color,
    val isSquare: Boolean,
    val phase: Float
)

@Composable
private fun ParticleField(modifier: Modifier = Modifier) {
    val particles = remember {
        val palette = listOf(
            LitecartesColor.ScoreOrange,
            LitecartesColor.ScoreGreen,
            LitecartesColor.ScoreYellow,
            LitecartesColor.Primary,
            LitecartesColor.DarkBrown
        )
        val rng = Random(0)
        List(18) {
            Particle(
                angleDeg = rng.nextFloat() * 360f,
                radiusFraction = 0.62f + rng.nextFloat() * 0.32f,
                sizeDp = listOf(4, 5, 6, 8).random(rng),
                color = palette.random(rng),
                isSquare = rng.nextBoolean(),
                phase = rng.nextFloat()
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "particles")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleProgress"
    )

    Box(modifier = modifier) {
        particles.forEach { p ->
            val localPhase = (progress + p.phase) % 1f
            val bob = sin(localPhase * 2f * Math.PI).toFloat()
            val radiusFactor = p.radiusFraction * (1f + 0.05f * bob)
            val angleRad = Math.toRadians(p.angleDeg.toDouble()).toFloat()
            val xFrac = 0.5f + 0.5f * radiusFactor * cos(angleRad)
            val yFrac = 0.5f + 0.5f * radiusFactor * sin(angleRad)
            val xBias = (xFrac - 0.5f) * 2f
            val yBias = (yFrac - 0.5f) * 2f

            Box(
                modifier = Modifier
                    .align(BiasAlignment(xBias, yBias))
                    .rotate(localPhase * 360f)
                    .size(p.sizeDp.dp)
                    .clip(if (p.isSquare) RoundedCornerShape(2.dp) else CircleShape)
                    .background(p.color)
            )
        }
    }
}

@Composable
private fun DiamondRewardChip(amount: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(LitecartesColor.DarkerSurface)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Hadiah:",
            color = LitecartesColor.Secondary,
            fontSize = 13.sp,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "+$amount",
            color = LitecartesColor.Primary,
            fontSize = 16.sp,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold
        )
        Image(
            painter = painterResource(id = R.drawable.diamond),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewResultScreen() {
    LitecartesNativeTheme {
        ResultScreen(
            navController = rememberNavController(),
            chapterId = 0,
            level = 3
        )
    }
}
