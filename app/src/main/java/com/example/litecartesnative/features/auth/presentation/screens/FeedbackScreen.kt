package com.example.litecartesnative.features.auth.presentation.screens

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.features.quiz.presentation.components.TtsManager
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
    val context = LocalContext.current

    val ttsManager = remember { TtsManager(context) }
    DisposableEffect(Unit) {
        onDispose { ttsManager.shutdown() }
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
                                    ttsManager.speak("${material.title}. ${material.content}")
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
                        Text(
                            text = material.content,
                            color = Color.White,
                            fontFamily = nunitosFontFamily,
                            textAlign = TextAlign.Justify
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
                text = "Mari perbaiki kesalahanmu",
                borderColor = LitecartesColor.Secondary,
                color = LitecartesColor.Surface,
                backgroundColor = LitecartesColor.Secondary,
                onClick = {
                    navController.navigate(
                        route = "${Screen.QuestionScreen.route}/${chapterId}/levels/${level}/questions/${id}?toquestion=${true}"
                    )
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
