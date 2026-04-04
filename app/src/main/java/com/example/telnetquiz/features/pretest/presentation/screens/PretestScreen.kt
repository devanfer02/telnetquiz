package com.example.telnetquiz.features.pretest.presentation.screens

import com.example.telnetquiz.components.ErrorRetryBox
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.features.pretest.presentation.components.PretestButton
import com.example.telnetquiz.components.ProgressBarFromApi
import com.example.telnetquiz.features.pretest.presentation.singletons.PretestResultHolder
import com.example.telnetquiz.features.pretest.presentation.viewmodel.PretestViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme

@Composable
fun PretestScreen(
    navController: NavController,
    viewModel: PretestViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentQuestion = viewModel.currentQuestion
    val haptic = LocalHapticFeedback.current

    DisposableEffect(Unit) {
        onDispose { viewModel.stopTts() }
    }

    LaunchedEffect(Unit) {
        viewModel.loadPretestQuestions()
    }

    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) {
            PretestResultHolder.lastResult = state.result
            navController.navigate(Screen.PretestResultScreen.route) {
                popUpTo(Screen.QuickCheckScren.route) { inclusive = true }
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
                .padding(horizontal = 12.dp)
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
                    var isQuestionExpanded by remember { mutableStateOf(true) }

                    Spacer(modifier = Modifier.padding(5.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(LitecartesColor.Primary)
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    val letters = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H')
                                    val optionsText = currentQuestion.options
                                        .mapIndexed { i, opt -> "${letters.getOrElse(i) { ' ' }}. ${opt.text}" }
                                        .joinToString(". ")
                                    val textToRead = "${currentQuestion.description}. ${currentQuestion.question}. Pilihan jawaban: $optionsText"
                                    viewModel.speak(textToRead)
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Baca soal",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = isQuestionExpanded,
                            enter = expandVertically(expandFrom = Alignment.Top),
                            exit = shrinkVertically(shrinkTowards = Alignment.Top)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (!currentQuestion.imageLink.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = currentQuestion.imageLink,
                                        contentDescription = "",
                                        modifier = Modifier.size(250.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                                Text(
                                    text = currentQuestion.question,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isQuestionExpanded = !isQuestionExpanded }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color.White.copy(alpha = 0.5f))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(14.dp))
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(currentQuestion.options) { option ->
                            PretestButton(
                                text = option.text,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    viewModel.selectAnswer(currentQuestion.id, option.id)
                                },
                                isActive = selectedOptionId == option.id
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(10.dp))

                    val isLastQuestion = state.currentQuestionIndex >= state.questions.size - 1
                    val hasSelectedAnswer = selectedOptionId != null

                    PretestButton(
                        text = if (isLastQuestion) "Selesai" else "Lanjutkan",
                        backgroundColor = if (hasSelectedAnswer) LitecartesColor.Secondary else Color.Gray,
                        textColor = if (hasSelectedAnswer) LitecartesColor.Surface else Color.White,
                        onClick = {

                            if (hasSelectedAnswer) {
                                if (isLastQuestion) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.audioManager.playSfx(SfxType.PRETEST_SUBMIT)
                                    viewModel.submitPretest()
                                } else {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    viewModel.nextQuestion()
                                }
                            }
                        }
                    )

                    if (state.isSubmitting) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = LitecartesColor.Secondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(10.dp))
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
