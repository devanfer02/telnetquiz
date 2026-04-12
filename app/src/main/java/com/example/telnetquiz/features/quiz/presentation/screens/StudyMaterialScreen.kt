package com.example.telnetquiz.features.quiz.presentation.screens

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
import com.example.telnetquiz.components.MaterialContentCard
import com.example.telnetquiz.components.ProgressBarFromApi
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.quiz.presentation.viewmodel.StudyMaterialNavEvent
import com.example.telnetquiz.features.quiz.presentation.viewmodel.StudyMaterialViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme

@Composable
fun StudyMaterialScreen(
    navController: NavController,
    chapterId: Int,
    level: Int,
    materialId: Int = 0,
    viewModel: StudyMaterialViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isTtsLoading by viewModel.ttsLoading.collectAsState()
    val scrollState = rememberScrollState()

    DisposableEffect(Unit) {
        onDispose { viewModel.stopTts() }
    }

    LaunchedEffect(materialId) {
        if (materialId > 0) {
            viewModel.loadMaterial(materialId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            val materialRoute = "${Screen.StudyMaterialScreen.route}/{chapterId}/levels/{level}/questions/{id}?materialId={materialId}"
            when (event) {
                is StudyMaterialNavEvent.NextWrongQuestion -> {
                    navController.navigate(
                        "${Screen.StudyMaterialScreen.route}/${event.chapterId}/levels/${event.level}/questions/${event.questionId}?materialId=${event.materialId}"
                    ) {
                        popUpTo(materialRoute) { inclusive = true }
                    }
                }
                is StudyMaterialNavEvent.NextLearnFirstMaterial -> {
                    navController.navigate(
                        "${Screen.StudyMaterialScreen.route}/${event.chapterId}/levels/${event.level}/questions/0?materialId=${event.materialId}"
                    ) {
                        popUpTo(materialRoute) { inclusive = true }
                    }
                }
                is StudyMaterialNavEvent.PreviousLearnFirstMaterial -> {
                    navController.navigate(
                        "${Screen.StudyMaterialScreen.route}/${event.chapterId}/levels/${event.level}/questions/0?materialId=${event.materialId}"
                    ) {
                        popUpTo(materialRoute) { inclusive = true }
                    }
                }
                is StudyMaterialNavEvent.StartQuiz -> {
                    navController.navigate(
                        "${Screen.QuestionScreen.route}/${event.quizId}"
                    ) {
                        popUpTo(materialRoute) { inclusive = true }
                    }
                }
                is StudyMaterialNavEvent.RetryQuiz -> {
                    navController.navigate(
                        "${Screen.QuestionScreen.route}/${event.quizId}?retry=true"
                    ) {
                        popUpTo(materialRoute) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (viewModel.totalMaterials > 0) {
                ProgressBarFromApi(
                    current = viewModel.currentMaterialIndex,
                    total = viewModel.totalMaterials,
                    containerColor = LitecartesColor.Primary,
                    barColor = LitecartesColor.Surface,
                    borderColor = LitecartesColor.Surface,
                    useDashedBorder = true,
                    showLabel = false,
                    showDivider = true
                )
            }
        },
        modifier = Modifier
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Primary)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(8.dp))

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
                        isTtsLoading = isTtsLoading,
                        onSpeakClick = {
                            val plainContent = material.content.replace(Regex("<[^>]*>"), "")
                            viewModel.speak("${material.title}. $plainContent")
                            viewModel.speakContent("material", material.id, null, material.audioLink)
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

            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                text = viewModel.buttonText,
                borderColor = LitecartesColor.Secondary,
                color = LitecartesColor.Surface,
                backgroundColor = LitecartesColor.Secondary,
                onClick = { viewModel.onContinue() },
                textModifier = Modifier.padding(8.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                fontSize = 16.sp
            )
            if (viewModel.canGoPrevious) {
                Button(
                    text = "Sebelumnya",
                    borderColor = LitecartesColor.Surface,
                    color = LitecartesColor.Secondary,
                    backgroundColor = LitecartesColor.Surface,
                    onClick = { viewModel.onPrevious() },
                    textModifier = Modifier.padding(4.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewStudyMaterialScreen() {
    LitecartesNativeTheme {
        StudyMaterialScreen(
            navController = rememberNavController(),
            chapterId = 0,
            level = 1
        )
    }
}
