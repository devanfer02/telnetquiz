package com.example.litecartesnative.features.quiz.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.litecartesnative.R
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.features.pretest.presentation.components.PretestButton
import com.example.litecartesnative.features.quiz.presentation.components.ProgressBarFromApi
import com.example.litecartesnative.features.quiz.presentation.components.OptionButton
import com.example.litecartesnative.features.quiz.presentation.viewmodel.QuizViewModel
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navController: NavController,
    quizId: Int,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentQuestion = viewModel.currentQuestion

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(quizId) {
        viewModel.loadQuiz(quizId)
    }

    // Navigate to result when quiz is submitted
    LaunchedEffect(state.result) {
        if (state.result != null) {
            val quiz = state.quiz
            if (quiz != null) {
                navController.navigate(
                    "${Screen.ResultScreen.route}/${quiz.chapterId}/levels/${quiz.level}"
                ) {
                    popUpTo("${Screen.QuestionScreen.route}/${quizId}") { inclusive = true }
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
                            text = state.error ?: "An error occurred",
                            color = LitecartesColor.Secondary
                        )
                    }
                }
                currentQuestion != null -> {
                    val selectedOptionId = state.answers[currentQuestion.id]

                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    bottomEnd = 20.dp,
                                    bottomStart = 20.dp
                                )
                            )
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(20.dp),
                                clip = false
                            )
                            .background(LitecartesColor.Primary)
                            .padding(top = 18.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.quiz?.title ?: "",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontFamily = nunitosFontFamily
                            )
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
                                modifier = Modifier.padding(vertical = 20.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LitecartesColor.Surface)
                            .padding(top = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 18.dp)
                        ) {
                            Text(
                                text = currentQuestion.question,
                                textAlign = TextAlign.Center,
                                color = LitecartesColor.Secondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            LazyColumn(
                                modifier = Modifier.weight(1f)
                            ) {
                                items(currentQuestion.options) { option ->
                                    OptionButton(
                                        text = option.text,
                                        isActive = selectedOptionId == option.id,
                                        onClick = {
                                            viewModel.selectAnswer(currentQuestion.id, option.id)
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(10.dp))

                            if (selectedOptionId != null) {
                                OutlinedButton(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    onClick = {
                                        showDialog = true
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, LitecartesColor.DarkBrown),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LitecartesColor.DarkBrown
                                    ),
                                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
                                ) {
                                    Text(
                                        text = "Lanjutkan",
                                        color = LitecartesColor.Surface
                                    )
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

                            Spacer(modifier = Modifier.padding(10.dp))

                            if (showDialog) {
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
                                            text = "Jawaban tersimpan!",
                                            fontFamily = nunitosFontFamily,
                                            fontSize = 20.sp,
                                            color = LitecartesColor.Secondary,
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
                                                        viewModel.submitQuiz()
                                                    } else {
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
}
