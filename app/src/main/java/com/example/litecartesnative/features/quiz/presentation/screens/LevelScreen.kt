package com.example.litecartesnative.features.quiz.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Navbar
import com.example.litecartesnative.features.quiz.presentation.components.LevelButton
import com.example.litecartesnative.features.quiz.presentation.components.ProfileTopBar
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.constants.levelsData
import com.example.litecartesnative.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.litecartesnative.features.quiz.presentation.singletons.MarkAsDoneManager
import com.example.litecartesnative.ui.theme.LitecartesColor

@Composable
fun LevelScreen(
    navController: NavController,
    chapterId: Int,
    viewModel: ChapterViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(chapterId) {
        viewModel.loadChapterById(chapterId)
    }

    Scaffold(
        topBar = {
            ProfileTopBar(
                backgroundColor = LitecartesColor.DarkerSurface
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                when {
                    detailState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = LitecartesColor.Secondary)
                        }
                    }
                    detailState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = detailState.error ?: "An error occurred",
                                color = LitecartesColor.Secondary
                            )
                        }
                    }
                    detailState.chapter != null -> {
                        val chapter = detailState.chapter!!
                        val quizzes = chapter.quizzes

                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            val screenWidth = maxWidth
                            // Content height is proportional to screen width to maintain aspect ratio
                            // Using 1.94 ratio (800/412) from original Pixel 8 design
                            val contentHeight = screenWidth * 1.94f

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(contentHeight)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.level_background),
                                    contentDescription = "bg",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.fillMaxSize()
                                )

                                quizzes.forEachIndexed { index, quiz ->
                                    if (index >= levelsData.size) {
                                        return@forEachIndexed
                                    }

                                    val levelPosition = levelsData[index]
                                    // Account for button size (approximately 50dp)
                                    val buttonOffset = with(LocalDensity.current) { 25.dp.toPx() }

                                    Box(
                                        modifier = Modifier
                                            .offset(
                                                x = screenWidth * levelPosition.xFraction - with(LocalDensity.current) { buttonOffset.toDp() },
                                                y = contentHeight * levelPosition.yFraction
                                            )
                                    ) {
                                        LevelButton(
                                            level = quiz.level,
                                            onClick = {
                                                navController.navigate(
                                                    "${Screen.QuestionScreen.route}/${quiz.id}"
                                                )
                                            },
                                            done = MarkAsDoneManager.levels.getOrNull(chapterId)?.getOrNull(quiz.level - 1) ?: false
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Navbar(navController = navController)
        }
    }
}

@Preview
@Composable
fun PreviewLevelScreen() {
    LevelScreen(
        navController = rememberNavController(),
        chapterId = 1
    )
}
