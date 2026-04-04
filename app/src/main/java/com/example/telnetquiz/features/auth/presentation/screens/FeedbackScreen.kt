package com.example.telnetquiz.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.components.MascotLoadingScreen
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.features.auth.presentation.components.MaterialContentCard
import com.example.telnetquiz.features.quiz.presentation.singletons.LearnFirstHolder
import com.example.telnetquiz.features.quiz.presentation.singletons.RemedialHolder
import com.example.telnetquiz.features.quiz.presentation.singletons.WrongQuizManager
import com.example.telnetquiz.features.quiz.presentation.viewmodel.StudyMaterialViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme

@Composable
fun FeedbackScren(
    navController: NavController,
    chapterId: Int,
    level: Int,
    materialId: Int = 0,
    viewModel: StudyMaterialViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    DisposableEffect(Unit) {
        onDispose { viewModel.stopTts() }
    }

    LaunchedEffect(materialId) {
        if (materialId > 0) {
            viewModel.loadMaterial(materialId)
        }
    }

    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Surface)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(50.dp))

            when {
                state.isLoading -> {
                    MascotLoadingScreen(modifier = Modifier.fillMaxSize())
                }
                state.error != null && materialId > 0 -> {
                    ErrorRetryBox(
                        message = state.error ?: "Gagal memuat materi"
                    )
                }
                state.material != null -> {
                    val material = state.material!!
                    MaterialContentCard(
                        title = material.title,
                        content = material.content,
                        imageLink = material.imageLink,
                        onSpeakClick = {
                            val plainContent = material.content.replace(Regex("<[^>]*>"), "")
                            viewModel.speak("${material.title}. $plainContent")
                            viewModel.speakContent("material", material.id, null)
                        }
                    )
                }
                else -> {
                    MaterialContentCard(
                        title = "Pelajari Materi",
                        content = "Materi pembelajaran akan ditampilkan di sini. Silakan coba lagi setelah mempelajari materi.",
                        imageLink = null,
                        onSpeakClick = {}
                    )
                }
            }

            Spacer(modifier = Modifier.padding(30.dp))
            Button(
                text = when {
                    WrongQuizManager.queue.isNotEmpty() -> "Lanjut Belajar"
                    LearnFirstHolder.hasNext() -> "Lanjut Belajar"
                    LearnFirstHolder.isActive() -> "Mulai Kuis"
                    else -> "Ayo Coba Lagi!"
                },
                borderColor = LitecartesColor.Secondary,
                color = LitecartesColor.Surface,
                backgroundColor = LitecartesColor.Secondary,
                onClick = {
                    if (WrongQuizManager.queue.isNotEmpty()) {
                        // Remedial flow: more materials to review
                        val next = WrongQuizManager.queue.first()
                        WrongQuizManager.queue.removeFirst()
                        navController.navigate(
                            "${Screen.FeedbackScreen.route}/${next.chapterId}/levels/${next.level}/questions/${next.id}?materialId=${next.materialId}"
                        ) {
                            popUpTo("${Screen.FeedbackScreen.route}/{chapterId}/levels/{level}/questions/{id}?materialId={materialId}") { inclusive = true }
                        }
                    } else if (LearnFirstHolder.hasNext()) {
                        // Learn-first flow: more materials to view
                        val nextMaterial = LearnFirstHolder.next()!!
                        navController.navigate(
                            "${Screen.FeedbackScreen.route}/${chapterId}/levels/${level}/questions/0?materialId=${nextMaterial.id}"
                        ) {
                            popUpTo("${Screen.FeedbackScreen.route}/{chapterId}/levels/{level}/questions/{id}?materialId={materialId}") { inclusive = true }
                        }
                    } else if (LearnFirstHolder.isActive()) {
                        viewModel.audioManager.playSfx(SfxType.START_LEVEL)
                        val quizId = LearnFirstHolder.quizId
                        LearnFirstHolder.clear()
                        navController.navigate(
                            "${Screen.QuestionScreen.route}/${quizId}"
                        ) {
                            popUpTo("${Screen.FeedbackScreen.route}/{chapterId}/levels/{level}/questions/{id}?materialId={materialId}") { inclusive = true }
                        }
                    } else {
                        // Remedial flow done: retry the quiz
                        val quizId = RemedialHolder.quizId
                        navController.navigate(
                            "${Screen.QuestionScreen.route}/${quizId}?retry=true"
                        ) {
                            popUpTo("${Screen.FeedbackScreen.route}/{chapterId}/levels/{level}/questions/{id}?materialId={materialId}") { inclusive = true }
                        }
                    }
                },
                textModifier = Modifier.padding(8.dp),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewFeedbackScren() {
    LitecartesNativeTheme {
        FeedbackScren(
            navController = rememberNavController(),
            chapterId = 0,
            level = 1
        )
    }
}
