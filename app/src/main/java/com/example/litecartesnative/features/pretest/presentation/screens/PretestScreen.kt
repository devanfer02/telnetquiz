package com.example.litecartesnative.features.pretest.presentation.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.litecartesnative.features.pretest.presentation.components.PretestButton
import com.example.litecartesnative.features.pretest.presentation.components.ProgressBarFromApi
import com.example.litecartesnative.features.pretest.presentation.viewmodel.PretestViewModel
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.ui.theme.LitecartesColor

@Composable
fun PretestScreen(
    navController: NavController,
    viewModel: PretestViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentQuestion = viewModel.currentQuestion

    LaunchedEffect(Unit) {
        viewModel.loadPretestQuestions()
    }

    // Navigate to Home when pretest is completed successfully
    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) {
            navController.navigate(Screen.HomeScreen.route) {
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "Terjadi kesalahan",
                            color = LitecartesColor.Secondary
                        )
                    }
                }
                currentQuestion != null -> {
                    val selectedOptionId = state.answers[currentQuestion.id]

                    Spacer(modifier = Modifier.padding(5.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(LitecartesColor.Primary)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
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
                    Spacer(modifier = Modifier.padding(14.dp))
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(currentQuestion.options) { option ->
                            PretestButton(
                                text = option.text,
                                onClick = {
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
                        backgroundColor = if (hasSelectedAnswer) LitecartesColor.Secondary else LitecartesColor.Secondary.copy(alpha = 0.5f),
                        textColor = LitecartesColor.Surface,
                        onClick = {
                            if (hasSelectedAnswer) {
                                if (isLastQuestion) {
                                    viewModel.submitPretest()
                                } else {
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
