package com.example.telnetquiz.features.quiz.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.components.MascotLoadingScreen
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.quiz.presentation.components.AnswerFeedbackSheet
import com.example.telnetquiz.components.OptionButton
import com.example.telnetquiz.components.OptionFeedback
import com.example.telnetquiz.components.ProgressBarFromApi
import com.example.telnetquiz.components.QuestionHeaderBox
import com.example.telnetquiz.features.quiz.presentation.components.VerifyButton
import com.example.telnetquiz.features.quiz.presentation.viewmodel.QuizNavEvent
import com.example.telnetquiz.features.quiz.presentation.viewmodel.QuizViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun QuestionScreen(
    navController: NavController,
    quizId: Int,
    isRetry: Boolean = false,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentQuestion = state.quiz?.questions?.getOrNull(state.currentQuestionIndex)
    val haptic = LocalHapticFeedback.current
    val tutorialController = LocalTutorialController.current

    var showDialog by remember { mutableStateOf(false) }
    var showExitConfirm by remember { mutableStateOf(false) }

    BackHandler { showExitConfirm = true }

    if (showExitConfirm) {
        AlertDialog(
            onDismissRequest = { showExitConfirm = false },
            title = { androidx.compose.material3.Text("Yakin mau keluar, Penjelajah?") },
            text = { androidx.compose.material3.Text("Progres kuis kamu belum tersimpan. Kalau keluar sekarang, kamu harus mengulang dari awal.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitConfirm = false
                    viewModel.stopTts()
                    navController.popBackStack()
                }) { androidx.compose.material3.Text("Keluar") }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirm = false }) {
                    androidx.compose.material3.Text("Lanjut Kuis")
                }
            }
        )
    }

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

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is QuizNavEvent.GoToRemedial -> {
                    navController.navigate(
                        "${Screen.RemedialScreen.route}/${event.wrongCount}/${event.totalCount}"
                    ) {
                        popUpTo("${Screen.QuestionScreen.route}/${quizId}") { inclusive = true }
                    }
                }
                is QuizNavEvent.GoToResult -> {
                    navController.navigate(
                        "${Screen.ResultScreen.route}/${event.chapterId}/levels/${event.level}"
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
                    total = state.quiz!!.questions.size,
                    containerColor = LitecartesColor.Primary,
                    barColor = LitecartesColor.Surface,
                    borderColor = LitecartesColor.Surface,
                    useDashedBorder = true,
                    showLabel = false,
                    showDivider = true
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

                    val isTtsLoading by viewModel.ttsLoading.collectAsState()
                    val isTtsPlaying by viewModel.ttsPlaying.collectAsState()

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(LitecartesColor.Surface)
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            item {
                                QuestionHeaderBox(
                                    title = state.quiz?.title ?: "",
                                    description = currentQuestion.description,
                                    imageLink = currentQuestion.imageLink,
                                    isTtsLoading = isTtsLoading,
                                    isTtsPlaying = isTtsPlaying,
                                    onStopClick = { viewModel.stopTts() },
                                    onSpeakClick = {
                                        val optionsText = currentQuestion.options
                                            .mapIndexed { i, opt -> "${letters.getOrElse(i) { ' ' }}. ${opt.text}" }
                                            .joinToString(". ")
                                        val textToRead = "${currentQuestion.description}. ${currentQuestion.question}. Pilihan jawaban: $optionsText"
                                        viewModel.speak(textToRead)
                                        viewModel.speakContent("question", currentQuestion.id, null, currentQuestion.audioLink)
                                    }
                                )
                            }
                            item {
                                Text(
                                    text = currentQuestion.question,
                                    textAlign = TextAlign.Center,
                                    color = LitecartesColor.Secondary,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = nunitosFontFamily,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                                )
                            }
                            itemsIndexed(currentQuestion.options) { index, option ->
                                val optionFeedback = when {
                                    verification == null -> OptionFeedback.NONE
                                    option.id == selectedOptionId && verification.correct -> OptionFeedback.CORRECT
                                    option.id == selectedOptionId && !verification.correct -> OptionFeedback.WRONG
                                    else -> OptionFeedback.NONE
                                }

                                val optionWrapper = Modifier
                                    .padding(horizontal = 12.dp)
                                    .then(
                                        if (index == 0 && tutorialController != null) {
                                            Modifier.onGloballyPositioned {
                                                tutorialController.registerTarget("quiz_option_first", it)
                                            }
                                        } else Modifier
                                    )
                                androidx.compose.foundation.layout.Box(modifier = optionWrapper) {
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
                        }

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 18.dp, vertical = 8.dp)
                                .then(
                                    if (tutorialController != null) Modifier.onGloballyPositioned {
                                        tutorialController.registerTarget("quiz_verify_btn", it)
                                    } else Modifier
                                )
                        ) {
                            VerifyButton(
                                isVisible = selectedOptionId != null && !viewModel.isCurrentQuestionVerified,
                                isVerifying = state.isVerifying,
                                isSubmitting = state.isSubmitting,
                                isLastQuestion = viewModel.isLastQuestion,
                                onVerify = { viewModel.verifyCurrentAnswer() }
                            )
                        }

                        if (showDialog && verification != null) {
                            AnswerFeedbackSheet(
                                isCorrect = verification.correct,
                                isLastQuestion = viewModel.isLastQuestion,
                                onDismiss = { showDialog = false },
                                onContinue = {
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
