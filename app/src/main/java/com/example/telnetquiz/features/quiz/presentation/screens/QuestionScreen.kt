package com.example.telnetquiz.features.quiz.presentation.screens

import com.example.telnetquiz.components.ErrorRetryBox
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
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
import com.example.telnetquiz.R
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.components.MascotLoadingScreen
import com.example.telnetquiz.features.pretest.presentation.components.PretestButton
import com.example.telnetquiz.features.quiz.presentation.components.OptionButton
import com.example.telnetquiz.features.quiz.presentation.components.OptionFeedback
import com.example.telnetquiz.features.quiz.presentation.components.ProgressBarFromApi
import com.example.telnetquiz.features.quiz.presentation.singletons.QuizResultHolder
import com.example.telnetquiz.features.quiz.presentation.singletons.RemedialHolder
import com.example.telnetquiz.features.quiz.presentation.viewmodel.QuizViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navController: NavController,
    quizId: Int,
    isRetry: Boolean = false,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentQuestion = viewModel.currentQuestion
    val haptic = LocalHapticFeedback.current

    var showDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { viewModel.stopTts() }
    }

    val letters = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H')

    LaunchedEffect(quizId, isRetry) {
        if (isRetry) {
            viewModel.loadQuizForRetry()
        } else {
            viewModel.loadQuiz(quizId)
        }
    }

    LaunchedEffect(state.verifiedQuestions.size) {
        if (currentQuestion != null && state.verifiedQuestions.containsKey(currentQuestion.id)) {
            showDialog = true
            val verification = state.verifiedQuestions[currentQuestion.id]
            if (verification != null) {
                viewModel.playAnswerSfx(verification.correct, isRetry)
            }
        }
    }

    LaunchedEffect(state.result) {
        if (state.result != null) {
            val quiz = state.quiz
            val result = state.result
            if (quiz != null && result != null) {
                val originalQuiz = RemedialHolder.quizData ?: quiz

                if (!result.passed && !RemedialHolder.isRetry) {
                    navController.navigate(
                        "${Screen.RemedialScreen.route}/${result.wrongQuestionIds?.size ?: 0}/${result.totalQuestions}"
                    ) {
                        popUpTo("${Screen.QuestionScreen.route}/${quizId}") { inclusive = true }
                    }
                } else {
                    QuizResultHolder.lastResult = result
                    RemedialHolder.clear()
                    navController.navigate(
                        "${Screen.ResultScreen.route}/${originalQuiz.chapterId}/levels/${originalQuiz.level}"
                    ) {
                        popUpTo("${Screen.QuestionScreen.route}/${quizId}") { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (state.quiz != null) {
                ProgressBarFromApi(
                    current = state.currentQuestionIndex + 1,
                    total = state.quiz!!.questions.size
                )
            }
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Surface),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when {
                state.isLoading -> {
                    MascotLoadingScreen(modifier = Modifier.fillMaxSize())
                }
                state.error != null -> {
                    ErrorRetryBox(
                        message = state.error ?: "Terjadi kesalahan",
                        onRetry = {
                            if (isRetry) viewModel.loadQuizForRetry()
                            else viewModel.loadQuiz(quizId)
                        }
                    )
                }
                currentQuestion != null -> {
                    val selectedOptionId = state.answers[currentQuestion.id]
                    val verification = state.verifiedQuestions[currentQuestion.id]

                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(
                                    bottomEnd = 24.dp,
                                    bottomStart = 24.dp
                                ),
                                clip = false
                            )
                            .clip(
                                RoundedCornerShape(
                                    bottomEnd = 24.dp,
                                    bottomStart = 24.dp
                                )
                            )
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        LitecartesColor.Primary,
                                        LitecartesColor.Primary.copy(alpha = 0.9f)
                                    )
                                )
                            )
                            .padding(top = 18.dp, bottom = 20.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = state.quiz?.title ?: "",
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontFamily = nunitosFontFamily,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        val textToRead = "${currentQuestion.description}. ${currentQuestion.question}"
                                        viewModel.speak(textToRead)
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Baca soal",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(4.dp))
                            if (!currentQuestion.imageLink.isNullOrEmpty()) {
                                AsyncImage(
                                    model = currentQuestion.imageLink,
                                    contentDescription = "",
                                    modifier = Modifier.size(250.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Text(
                                text = currentQuestion.description,
                                textAlign = TextAlign.Justify,
                                color = Color.White,
                                fontFamily = nunitosFontFamily,
                                fontSize = 17.sp,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(LitecartesColor.Surface)
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = currentQuestion.question,
                            textAlign = TextAlign.Center,
                            color = LitecartesColor.Secondary,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = nunitosFontFamily,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(horizontal = 18.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp)
                        ) {
                            itemsIndexed(currentQuestion.options) { index, option ->
                                val optionFeedback = when {
                                    verification == null -> OptionFeedback.NONE
                                    option.id == selectedOptionId && verification.correct -> OptionFeedback.CORRECT
                                    option.id == selectedOptionId && !verification.correct -> OptionFeedback.WRONG
                                    else -> OptionFeedback.NONE
                                }

                                OptionButton(
                                    text = option.text,
                                    letter = letters.getOrElse(index) { ' ' },
                                    isActive = selectedOptionId == option.id && verification == null,
                                    feedback = optionFeedback,
                                    onClick = {
                                        viewModel.selectAnswer(currentQuestion.id, option.id)
                                    },
                                    haptic = haptic
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                        ) {
                            if (selectedOptionId != null && !viewModel.isCurrentQuestionVerified) {
                                OutlinedButton(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    onClick = {
                                        viewModel.verifyCurrentAnswer()
                                    },
                                    enabled = !state.isVerifying,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, LitecartesColor.DarkBrown),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LitecartesColor.DarkBrown
                                    ),
                                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
                                ) {
                                    if (state.isVerifying) {
                                        CircularProgressIndicator(
                                            color = LitecartesColor.Surface,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Text(
                                            text = if (viewModel.isLastQuestion) "Selesai" else "Lanjutkan",
                                            color = LitecartesColor.Surface,
                                            fontFamily = nunitosFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            if (state.isSubmitting) {
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

                        if (showDialog && verification != null) {
                            val feedbackGreen = Color(0xFF4CAF50)
                            val feedbackRed = Color(0xFFE53935)

                            ModalBottomSheet(
                                onDismissRequest = { showDialog = false },
                                containerColor = LitecartesColor.Surface
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(350.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if (verification.correct) "Jawaban benar!" else "Jawaban salah!",
                                        fontFamily = nunitosFontFamily,
                                        fontSize = 20.sp,
                                        color = if (verification.correct) feedbackGreen else feedbackRed,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Image(
                                        painter = painterResource(id = R.drawable.chap1),
                                        contentDescription = "",
                                        modifier = Modifier.size(200.dp)
                                    )

                                    Column(
                                        modifier = Modifier.padding(horizontal = 20.dp)
                                    ) {
                                        PretestButton(
                                            text = if (viewModel.isLastQuestion) "Selesai" else "Lanjut",
                                            backgroundColor = LitecartesColor.Secondary,
                                            textColor = LitecartesColor.Surface,
                                            onClick = {
                                                showDialog = false
                                                if (viewModel.isLastQuestion) {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    if (isRetry) viewModel.submitRetry() else viewModel.submitQuiz()
                                                } else {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    viewModel.nextQuestion()
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewQuestionScreen() {
    LitecartesNativeTheme {
        QuestionScreen(
            navController = rememberNavController(),
            quizId = 1
        )
    }
}
