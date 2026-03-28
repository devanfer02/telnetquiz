package com.example.telnetquiz.features.quiz.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.Navbar
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.repository.Result
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.telnetquiz.features.quiz.domain.model.LevelData
import com.example.telnetquiz.features.quiz.presentation.components.LevelButton
import com.example.telnetquiz.features.quiz.presentation.components.ProfileTopBar
import com.example.telnetquiz.features.quiz.presentation.singletons.LearnFirstHolder
import com.example.telnetquiz.features.quiz.presentation.singletons.ProfileCache
import com.example.telnetquiz.features.user.presentations.viewmodel.ProfileViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import kotlinx.coroutines.launch

@Composable
fun LevelScreen(
    navController: NavController,
    chapterId: Int,
    viewModel: ChapterViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val detailState by viewModel.detailState.collectAsState()
    val profileState by profileViewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showLevelDialog by remember { mutableStateOf(false) }
    var selectedQuizId by remember { mutableIntStateOf(0) }
    var selectedQuizLevel by remember { mutableIntStateOf(0) }
    var isFetchingMaterials by remember { mutableStateOf(false) }

    var pathAnimationTarget by remember { mutableFloatStateOf(0f) }
    var showButtons by remember { mutableStateOf(false) }
    val pathAnimationProgress by animateFloatAsState(
        targetValue = pathAnimationTarget,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "path_animation"
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (showButtons) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "button_alpha"
    )

    LaunchedEffect(detailState.chapter) {
        if (detailState.chapter != null) {
            pathAnimationTarget = 1f
            showButtons = true
        } else {
            pathAnimationTarget = 0f
            showButtons = false
        }
    }

    LaunchedEffect(chapterId) {
        viewModel.loadChapterById(chapterId)
        profileViewModel.loadProfile()
    }

    Scaffold(
        topBar = {
            ProfileTopBar(
                backgroundColor = LitecartesColor.DarkerSurface,
                name = profileState.profile?.fullname ?: "...",
                school = profileState.profile?.school?.name ?: "",
                imageUrl = profileState.profile?.image,
                totalScore = profileState.profile?.stats?.totalScore ?: 0,
                dailyStreak = profileState.profile?.stats?.dailyStreak ?: 0,
                tag = ProfileCache.getTag()
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
                if (detailState.chapter == null && detailState.error == null) {
                    Image(
                        painter = painterResource(id = R.drawable.level_background_no_path),
                        contentDescription = "bg",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                when {
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
                        val completedQuizIds = chapter.completedQuizIds
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
                                    painter = painterResource(id = R.drawable.level_background_no_path),
                                    contentDescription = "bg",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.fillMaxSize()
                                )

                                val buttonCenterOffset = with(LocalDensity.current) { 25.dp.toPx() }
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val canvasWidth = size.width
                                    val canvasHeight = size.height

                                    clipRect(
                                        top = 0f,
                                        bottom = canvasHeight * pathAnimationProgress,
                                        left = 0f,
                                        right = canvasWidth
                                    ) {
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

                                            translate(top = 4f) {
                                                drawPath(
                                                    path = path,
                                                    color = Color.Black.copy(alpha = 0.12f),
                                                    style = Stroke(
                                                        width = 72f,
                                                        cap = StrokeCap.Round
                                                    )
                                                )
                                            }
                                            drawPath(
                                                path = path,
                                                color = LitecartesColor.PathColor.copy(alpha = 0.5f),
                                                style = Stroke(
                                                    width = 64f,
                                                    cap = StrokeCap.Round
                                                )
                                            )
                                            drawPath(
                                                path = path,
                                                color = LitecartesColor.PathColor,
                                                style = Stroke(
                                                    width = 44f,
                                                    cap = StrokeCap.Round
                                                )
                                            )
                                        }
                                    }
                                }

                                quizzes.forEachIndexed { index, quiz ->
                                    if (index >= dynamicLevels.size) {
                                        return@forEachIndexed
                                    }

                                    val levelPosition = dynamicLevels[index]
                                    val buttonOffset = with(LocalDensity.current) { 25.dp.toPx() }

                                    val isCompleted = quiz.id in completedQuizIds
                                    val isUnlocked = index == 0 ||
                                        quizzes[index - 1].id in completedQuizIds

                                    Box(
                                        modifier = Modifier
                                            .offset(
                                                x = screenWidth * levelPosition.xFraction - with(LocalDensity.current) { buttonOffset.toDp() },
                                                y = contentHeight * levelPosition.yFraction
                                            )
                                            .alpha(buttonAlpha)
                                    ) {
                                        LevelButton(
                                            level = quiz.level,
                                            onClick = {
                                                selectedQuizId = quiz.id
                                                selectedQuizLevel = quiz.level
                                                showLevelDialog = true
                                            },
                                            done = isCompleted,
                                            isLocked = !isUnlocked
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

        if (showLevelDialog) {
            AlertDialog(
                onDismissRequest = { showLevelDialog = false },
                title = {
                    Text(
                        text = "Level $selectedQuizLevel",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                text = {
                    if (isFetchingMaterials) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = LitecartesColor.Secondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Column {
                            Text(
                                text = "Mau belajar materi dulu atau langsung main kuis?",
                                fontFamily = nunitosFontFamily
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    isFetchingMaterials = true
                                    coroutineScope.launch {
                                        when (val result = viewModel.fetchQuizMaterials(selectedQuizId)) {
                                            is Result.Success -> {
                                                val materials = result.data.materials
                                                if (materials.isNotEmpty()) {
                                                    LearnFirstHolder.setup(
                                                        quizId = selectedQuizId,
                                                        chapterId = chapterId,
                                                        level = selectedQuizLevel,
                                                        materials = materials
                                                    )
                                                    val firstMaterial = LearnFirstHolder.next()!!
                                                    showLevelDialog = false
                                                    isFetchingMaterials = false
                                                    navController.navigate(
                                                        "${Screen.FeedbackScreen.route}/${chapterId}/levels/${selectedQuizLevel}/questions/0?materialId=${firstMaterial.id}"
                                                    )
                                                } else {
                                                    showLevelDialog = false
                                                    isFetchingMaterials = false
                                                    navController.navigate(
                                                        "${Screen.QuestionScreen.route}/${selectedQuizId}"
                                                    )
                                                }
                                            }
                                            is Result.Error -> {
                                                showLevelDialog = false
                                                isFetchingMaterials = false
                                                navController.navigate(
                                                    "${Screen.QuestionScreen.route}/${selectedQuizId}"
                                                )
                                            }
                                            is Result.Loading -> {}
                                        }
                                    }
                                },
                                enabled = !isFetchingMaterials,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LitecartesColor.Primary,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Belajar Dulu",
                                    fontFamily = nunitosFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    viewModel.audioManager.playSfx(SfxType.START_LEVEL)
                                    showLevelDialog = false
                                    navController.navigate(
                                        "${Screen.QuestionScreen.route}/${selectedQuizId}"
                                    )
                                },
                                enabled = !isFetchingMaterials,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LitecartesColor.Secondary,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Langsung Main",
                                    fontFamily = nunitosFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {},
            )
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
    val yStart = 0.02f
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
