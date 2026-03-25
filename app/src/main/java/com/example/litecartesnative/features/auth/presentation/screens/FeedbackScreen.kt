package com.example.litecartesnative.features.auth.presentation.screens

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.features.quiz.presentation.singletons.LearnFirstHolder
import com.example.litecartesnative.features.quiz.presentation.singletons.RemedialHolder
import com.example.litecartesnative.features.quiz.presentation.singletons.WrongQuizManager
import com.example.litecartesnative.features.quiz.presentation.viewmodel.StudyMaterialViewModel
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.LitecartesNativeTheme
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun FeedbackScren(
    navController: NavController,
    chapterId: Int,
    level: Int,
    id: Int,
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LitecartesColor.Secondary)
                    }
                }
                state.error != null && materialId > 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "Gagal memuat materi",
                            color = LitecartesColor.Secondary
                        )
                    }
                }
                state.material != null -> {
                    val material = state.material!!
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .fillMaxWidth()
                            .background(LitecartesColor.Primary)
                            .padding(
                                vertical = 10.dp,
                                horizontal = 20.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    val plainContent = material.content.replace(Regex("<[^>]*>"), "")
                                    viewModel.speak("${material.title}. $plainContent")
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Baca materi",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Text(
                            text = material.title,
                            fontFamily = nunitosFontFamily,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                        if (!material.imageLink.isNullOrEmpty()) {
                            AsyncImage(
                                model = material.imageLink,
                                contentDescription = material.title,
                                modifier = Modifier.size(200.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        AndroidView(
                            factory = { ctx ->
                                WebView(ctx).apply {
                                    settings.javaScriptEnabled = false
                                    settings.setSupportZoom(false)
                                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                }
                            },
                            update = { webView ->
                                val html = """
                                    <html>
                                    <head><meta name="viewport" content="width=device-width, initial-scale=1.0"></head>
                                    <body style="color:white;font-family:sans-serif;margin:0;padding:0;">
                                    ${material.content}
                                    </body>
                                    </html>
                                """.trimIndent()
                                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                else -> {
                    // Fallback when no materialId is provided
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .fillMaxWidth()
                            .background(LitecartesColor.Primary)
                            .padding(
                                vertical = 10.dp,
                                horizontal = 20.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Pelajari Materi",
                            fontFamily = nunitosFontFamily,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Materi pembelajaran akan ditampilkan di sini. Silakan coba lagi setelah mempelajari materi.",
                            color = Color.White,
                            fontFamily = nunitosFontFamily,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(30.dp))
            Button(
                text = when {
                    WrongQuizManager.queue.isNotEmpty() -> "Lanjut Belajar"
                    LearnFirstHolder.hasNext() -> "Lanjut Belajar"
                    LearnFirstHolder.isActive() -> "Mulai Kuis"
                    else -> "Coba Lagi"
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
                        // Learn-first flow done: start the quiz
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
            level = 1,
            id = 1
        )
    }
}
