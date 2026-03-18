package com.example.litecartesnative.features.quiz.presentation.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.litecartesnative.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.litecartesnative.features.quiz.domain.model.LevelData
import com.example.litecartesnative.features.quiz.presentation.singletons.MarkAsDoneManager
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.LitecartesNativeTheme

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
                        val dynamicLevels = generateLevelPositions(quizzes.size)

                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            val screenWidth = maxWidth
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

                                // Road path between nodes
                                val buttonCenterOffset = with(LocalDensity.current) { 25.dp.toPx() }
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val canvasWidth = size.width
                                    val canvasHeight = size.height

                                    for (i in 0 until dynamicLevels.size - 1) {
                                        val from = dynamicLevels[i]
                                        val to = dynamicLevels[i + 1]

                                        val startX = canvasWidth * from.xFraction
                                        val startY = canvasHeight * from.yFraction + buttonCenterOffset
                                        val endX = canvasWidth * to.xFraction
                                        val endY = canvasHeight * to.yFraction + buttonCenterOffset

                                        val midY = (startY + endY) / 2f
                                        val path = Path().apply {
                                            moveTo(startX, startY)
                                            cubicTo(startX, midY, endX, midY, endX, endY)
                                        }

                                        // Road border
                                        drawPath(
                                            path = path,
                                            color = LitecartesColor.Secondary.copy(alpha = 0.3f),
                                            style = Stroke(
                                                width = 28f,
                                                cap = StrokeCap.Round
                                            )
                                        )
                                        // Road fill
                                        drawPath(
                                            path = path,
                                            color = LitecartesColor.Secondary.copy(alpha = 0.15f),
                                            style = Stroke(
                                                width = 14f,
                                                cap = StrokeCap.Round
                                            )
                                        )
                                    }
                                }

                                quizzes.forEachIndexed { index, quiz ->
                                    if (index >= dynamicLevels.size) {
                                        return@forEachIndexed
                                    }

                                    val levelPosition = dynamicLevels[index]
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

/**
 * Dynamically generates level positions based on the number of levels.
 * Creates a zigzag path from bottom to top of the screen.
 */
private fun generateLevelPositions(count: Int): List<LevelData> {
    if (count == 0) return emptyList()

    val positions = mutableListOf<LevelData>()
    val yStart = 0.10f
    val yEnd = 0.85f
    val yStep = if (count > 1) (yEnd - yStart) / (count - 1) else 0f

    for (i in 0 until count) {
        val yFraction = yStart + (i * yStep)
        // Zigzag: alternate between left and right sides
        val xFraction = when (i % 4) {
            0 -> 0.25f
            1 -> 0.55f
            2 -> 0.75f
            3 -> 0.45f
            else -> 0.5f
        }
        positions.add(LevelData(level = i + 1, xFraction = xFraction, yFraction = yFraction))
    }

    return positions
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLevelScreen() {
    LitecartesNativeTheme {
        LevelScreen(
            navController = rememberNavController(),
            chapterId = 1
        )
    }
}
