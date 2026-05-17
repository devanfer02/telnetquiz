package com.example.telnetquiz.features.quiz.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
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
import com.example.telnetquiz.components.tutorial.TutorialStepId
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.quiz.presentation.components.AnswerFeedbackSheet
import com.example.telnetquiz.components.OptionButton
import com.example.telnetquiz.components.OptionFeedback
import com.example.telnetquiz.components.ProgressBarFromApi
import com.example.telnetquiz.components.QuestionHeaderBox
import com.example.telnetquiz.features.quiz.presentation.components.VerifyButton
import com.example.telnetquiz.features.quiz.presentation.viewmodel.QuizErrorAction
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
                QuizProgressHeader(
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
        ) {
            when {
                state.isLoading -> {
                    MascotLoadingScreen(modifier = Modifier.fillMaxSize())
                }
                state.error != null && state.errorAction == QuizErrorAction.LoadQuiz -> {
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
                    val listState = rememberLazyListState()

                    val tutorialStepId = tutorialController?.currentStep?.id
                    LaunchedEffect(tutorialStepId, currentQuestion.id) {
                        if (tutorialStepId == TutorialStepId.QUIZ_VERIFY ||
                            tutorialStepId == TutorialStepId.RETRY_VERIFY) {
                            val lastIndex = (listState.layoutInfo.totalItemsCount - 1)
                                .coerceAtLeast(0)
                            listState.animateScrollToItem(lastIndex)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(LitecartesColor.Surface)
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            state = listState
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
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 18.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(LitecartesColor.Primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.QuestionMark,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = currentQuestion.question,
                                        textAlign = TextAlign.Start,
                                        color = LitecartesColor.Secondary,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = nunitosFontFamily,
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
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
                                Box(modifier = optionWrapper) {
                                    OptionButton(
                                        text = option.text,
                                        letter = letters.getOrElse(index) { ' ' },
                                        isActive = selectedOptionId == option.id && verification == null,
                                        feedback = optionFeedback,
                                        onClick = {
                                            viewModel.selectAnswer(currentQuestion.id, option.id)
                                            if (index == 0) {
                                                tutorialController?.notifyTargetClicked("quiz_option_first")
                                            }
                                        },
                                        haptic = haptic
                                    )
                                }
                            }
                            item {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 18.dp, vertical = 12.dp)
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
                                        onVerify = {
                                            viewModel.verifyCurrentAnswer()
                                            tutorialController?.notifyTargetClicked("quiz_verify_btn")
                                        }
                                    )
                                }
                            }
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

                        if (state.error != null && state.errorAction != null &&
                            state.errorAction != QuizErrorAction.LoadQuiz) {
                            val dialogTitle = when (state.errorAction) {
                                QuizErrorAction.SubmitQuiz,
                                QuizErrorAction.SubmitRetry -> "Gagal Mengirim Jawaban"
                                QuizErrorAction.VerifyAnswer -> "Gagal Memeriksa Jawaban"
                                else -> "Terjadi Kesalahan"
                            }
                            AlertDialog(
                                onDismissRequest = { viewModel.clearError() },
                                title = { androidx.compose.material3.Text(dialogTitle) },
                                text = {
                                    androidx.compose.material3.Text(
                                        state.error ?: "Terjadi kesalahan. Coba lagi."
                                    )
                                },
                                confirmButton = {
                                    TextButton(onClick = { viewModel.retryLastAction(quizId) }) {
                                        androidx.compose.material3.Text("Coba Lagi")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { viewModel.clearError() }) {
                                        androidx.compose.material3.Text("Tutup")
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

@Composable
private fun QuizProgressHeader(
    current: Int,
    total: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LitecartesColor.Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ProgressBarFromApi(
                    current = current,
                    total = total,
                    containerColor = LitecartesColor.Surface,
                    barColor = LitecartesColor.Primary,
                    borderColor = LitecartesColor.Secondary.copy(alpha = 0.2f),
                    useDashedBorder = false,
                    showLabel = false,
                    showDivider = false
                )
                ProgressSparkles()
            }
            Text(
                text = "$current/$total",
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = LitecartesColor.Secondary
            )
        }
        androidx.compose.material3.Divider(
            color = LitecartesColor.Secondary.copy(alpha = 0.08f),
            thickness = 1.dp
        )
    }
}

@Composable
private fun ProgressSparkles() {
    Box(modifier = Modifier.fillMaxWidth().height(30.dp)) {
        Icon(
            imageVector = Icons.Filled.AutoAwesome,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.75f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 12.dp, y = 5.dp)
                .size(8.dp)
        )
        Icon(
            imageVector = Icons.Filled.AutoAwesome,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 26.dp, y = 0.dp)
                .size(6.dp)
        )
        Icon(
            imageVector = Icons.Filled.AutoAwesome,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 8.dp, y = (-6).dp)
                .size(7.dp)
        )
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
