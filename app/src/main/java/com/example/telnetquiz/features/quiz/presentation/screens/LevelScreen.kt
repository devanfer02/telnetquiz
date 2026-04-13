package com.example.telnetquiz.features.quiz.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.MascotLoadingScreen
import com.example.telnetquiz.components.ProfileTopBar
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.telnetquiz.features.chapter.presentation.viewmodel.LevelNavEvent
import com.example.telnetquiz.features.quiz.presentation.util.generateLevelPositions
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.features.quiz.presentation.components.LevelButton
import com.example.telnetquiz.features.quiz.presentation.components.LevelOptionMenu
import com.example.telnetquiz.features.quiz.presentation.components.LevelPath
import androidx.compose.foundation.layout.Column
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun LevelScreen(
    navController: NavController,
    chapterId: Int,
    viewModel: ChapterViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val tutorialController = LocalTutorialController.current
    val detailState by viewModel.detailState.collectAsState()
    val isFetchingMaterials by viewModel.isFetchingMaterials.collectAsState()

    var showLevelDialog by remember { mutableStateOf(false) }
    var selectedQuizId by remember { mutableIntStateOf(0) }
    var selectedQuizLevel by remember { mutableIntStateOf(0) }
    var selectedQuizScore by remember { mutableStateOf<Int?>(null) }
    var showLockedDialog by remember { mutableStateOf(false) }
    var lockedDialogMessage by remember { mutableStateOf("") }

    var pathAnimationTarget by remember { mutableFloatStateOf(0f) }
    var showButtons by remember { mutableStateOf(false) }
    val pathAnimationProgress by animateFloatAsState(
        targetValue = pathAnimationTarget,
        animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
        label = "path_animation"
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (showButtons) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 800),
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
    }

    LaunchedEffect(Unit) {
        viewModel.levelNavEvent.collect { event ->
            when (event) {
                is LevelNavEvent.GoToFeedback -> {
                    navController.navigate(
                        "${Screen.StudyMaterialScreen.route}/${event.chapterId}/levels/${event.level}/questions/0?materialId=${event.materialId}"
                    )
                }
                is LevelNavEvent.GoToQuiz -> {
                    navController.navigate(
                        "${Screen.QuestionScreen.route}/${event.quizId}"
                    )
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ProfileTopBar(backgroundColor = LitecartesColor.DarkerSurface)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
                if (detailState.chapter == null && detailState.error == null) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        val loadingHeight = maxWidth * 1.94f + 72.dp
                        Image(
                            painter = painterResource(id = R.drawable.level_background_no_path),
                            contentDescription = "bg",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(loadingHeight)
                        )
                    }
                }
                when {
                    detailState.error != null -> {
                        ErrorRetryBox(
                            message = detailState.error ?: "Terjadi kesalahan",
                            onRetry = { viewModel.loadChapterById(chapterId) }
                        )
                    }
                    detailState.chapter != null -> {
                        val chapter = detailState.chapter!!
                        val quizzes = chapter.quizzes
                        val levelModels = remember(detailState.chapter) { viewModel.getLevelModels() }
                        val dynamicLevels = remember(chapterId, quizzes.size) {
                            generateLevelPositions(quizzes.size, chapterId)
                        }

                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            val screenWidth = maxWidth
                            val contentHeight = screenWidth * 1.94f + 72.dp
                            val density = LocalDensity.current
                            val buttonCenterOffset = remember(density) {
                                with(density) { 25.dp.toPx() }
                            }
                            val buttonCenterDp = remember(density) {
                                with(density) { buttonCenterOffset.toDp() }
                            }

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

                                LevelPath(
                                    levels = dynamicLevels,
                                    pathAnimationProgress = { pathAnimationProgress },
                                    buttonCenterOffsetPx = buttonCenterOffset,
                                    modifier = Modifier.fillMaxSize()
                                )

                                levelModels.forEachIndexed { index, levelModel ->
                                    if (index >= dynamicLevels.size) return@forEachIndexed

                                    val levelPosition = dynamicLevels[index]

                                    Box(
                                        modifier = Modifier
                                            .offset(
                                                x = screenWidth * levelPosition.xFraction - buttonCenterDp,
                                                y = contentHeight * levelPosition.yFraction
                                            )
                                            .graphicsLayer { alpha = buttonAlpha }
                                            .then(
                                                if (index == 0 && tutorialController != null)
                                                    Modifier.onGloballyPositioned {
                                                        tutorialController.registerTarget("level_button_first", it)
                                                    }
                                                else Modifier
                                            )
                                    ) {
                                        LevelButton(
                                            level = index + 1,
                                            onClick = {
                                                selectedQuizId = levelModel.quizId
                                                selectedQuizLevel = levelModel.level
                                                selectedQuizScore = levelModel.score
                                                showLevelDialog = true
                                            },
                                            onLockedClick = {
                                                levelModel.lockedMessage?.let { msg ->
                                                    lockedDialogMessage = msg
                                                    showLockedDialog = true
                                                }
                                            },
                                            done = levelModel.isCompleted,
                                            isLocked = !levelModel.isUnlocked,
                                            score = levelModel.score
                                        )
                                    }
                                }

                                if (showLevelDialog) {
                                    val selectedIndex = levelModels.indexOfFirst { it.quizId == selectedQuizId }
                                    if (selectedIndex >= 0 && selectedIndex in dynamicLevels.indices) {
                                        val levelPosition = dynamicLevels[selectedIndex]
                                        Box(
                                            modifier = Modifier.offset(
                                                x = screenWidth * levelPosition.xFraction - buttonCenterDp,
                                                y = contentHeight * levelPosition.yFraction
                                            )
                                        ) {
                                            LevelOptionMenu(
                                                expanded = true,
                                                onDismiss = { showLevelDialog = false },
                                                score = selectedQuizScore,
                                                onLearnFirst = {
                                                    showLevelDialog = false
                                                    viewModel.startLearnFirst(selectedQuizId, chapterId, selectedQuizLevel)
                                                },
                                                onPlayDirectly = {
                                                    showLevelDialog = false
                                                    viewModel.playDirectly(selectedQuizId)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            IconButton(
                onClick = {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
                    .size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = LitecartesColor.Primary,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to chapters"
                )
            }
        }

        if (showLockedDialog) {
            AlertDialog(
                onDismissRequest = { showLockedDialog = false },
                title = {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = "Level Terkunci",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.chap2),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .padding(bottom = 8.dp)
                        )
                        Text(lockedDialogMessage)
                    }
                },
                confirmButton = {
                    TextButton(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(LitecartesColor.Primary)
                            .fillMaxWidth(),
                        onClick = { showLockedDialog = false }
                    ) {
                        Text(
                            text = "OKAY!",
                            color = Color.White
                        )
                    }
                },
            )
        }

        if (isFetchingMaterials) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LitecartesColor.Surface)
            ) {
                MascotLoadingScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
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
