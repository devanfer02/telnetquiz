package com.example.telnetquiz.features.pretest.presentation.screens

import com.example.telnetquiz.components.ErrorRetryBox
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.components.OptionButton
import com.example.telnetquiz.components.OptionFeedback
import com.example.telnetquiz.components.PretestButton
import com.example.telnetquiz.components.ProgressBarFromApi
import com.example.telnetquiz.components.QuestionHeaderBox
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.features.pretest.presentation.viewmodel.PretestNavEvent
import com.example.telnetquiz.features.pretest.presentation.viewmodel.PretestViewModel
import com.example.telnetquiz.features.quiz.presentation.components.AnswerFeedbackSheet
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun PretestScreen(
    navController: NavController,
    viewModel: PretestViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isTtsLoading by viewModel.ttsLoading.collectAsState()
    val isTtsPlaying by viewModel.ttsPlaying.collectAsState()
    val currentQuestion = state.questions.getOrNull(state.currentQuestionIndex)
    val haptic = LocalHapticFeedback.current

    val letters = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H')

    var showDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { viewModel.stopTts() }
    }

    LaunchedEffect(Unit) {
        viewModel.loadPretestQuestions()
    }

    LaunchedEffect(state.verifiedQuestions.size) {
        if (currentQuestion != null && state.verifiedQuestions.containsKey(currentQuestion.id)) {
            showDialog = true
            val verification = state.verifiedQuestions[currentQuestion.id]
            if (verification != null) {
                viewModel.playAnswerSfx(verification.correct)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is PretestNavEvent.GoToResult -> {
                    navController.navigate(Screen.PretestResultScreen.route) {
                        popUpTo(Screen.QuickCheckScreen.route) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (state.questions.isNotEmpty()) {
                ProgressBarFromApi(
                    current = state.currentQuestionIndex + 1,
                    total = state.questions.size
                )
            }
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(LitecartesColor.Surface)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LitecartesColor.Secondary)
                    }
                }
                state.error != null -> {
                    ErrorRetryBox(
                        message = state.error ?: "Terjadi kesalahan",
                        onRetry = { viewModel.loadPretestQuestions() }
                    )
                }
                currentQuestion != null -> {
                    val selectedOptionId = state.answers[currentQuestion.id]
                    val verification = state.verifiedQuestions[currentQuestion.id]

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
                                    title = "Prolog",
                                    subtitle = currentQuestion.chapterTitle,
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
                                        viewModel.speakContent("pretest", currentQuestion.id, null, currentQuestion.audioLink)
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
                                Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                                    OptionButton(
                                        text = option.text,
                                        letter = letters.getOrElse(index) { ' ' },
                                        isActive = selectedOptionId == option.id && verification == null,
                                        feedback = optionFeedback,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            viewModel.selectAnswer(currentQuestion.id, option.id)
                                        },
                                        haptic = haptic
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                        ) {
                            val hasSelectedAnswer = selectedOptionId != null
                            val isVerified = verification != null

                            if (state.isSubmitting || state.isVerifying) {
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
                            } else if (!isVerified) {
                                PretestButton(
                                    text = "Cek Jawaban",
                                    backgroundColor = if (hasSelectedAnswer) LitecartesColor.Secondary else Color.Gray,
                                    textColor = if (hasSelectedAnswer) LitecartesColor.Surface else Color.White,
                                    onClick = {
                                        if (hasSelectedAnswer) {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            viewModel.audioManager.playSfx(SfxType.BTN_CLICK)
                                            viewModel.verifyCurrentAnswer()
                                        }
                                    }
                                )
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
                                        viewModel.audioManager.playSfx(SfxType.BTN_CLICK)
                                        viewModel.audioManager.playSfx(SfxType.PRETEST_SUBMIT)
                                        viewModel.submitPretest()
                                    } else {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.audioManager.playSfx(SfxType.BTN_CLICK)
                                        viewModel.nextQuestion()
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPretestScreen() {
    LitecartesNativeTheme {
        PretestScreen(
            navController = rememberNavController()
        )
    }
}
