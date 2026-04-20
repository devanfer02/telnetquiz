package com.example.telnetquiz.features.quiz.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.example.telnetquiz.R
import com.example.telnetquiz.components.MascotLoadingScreen
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.telnetquiz.features.chapter.presentation.viewmodel.LevelNavEvent
import com.example.telnetquiz.features.quiz.presentation.util.generateLevelPositions
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.features.quiz.presentation.components.LevelButton
import com.example.telnetquiz.features.quiz.presentation.components.LevelOptionMenu
import com.example.telnetquiz.features.quiz.presentation.components.LevelPath
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
    val snackbarHostState = remember { SnackbarHostState() }

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

    LaunchedEffect(Unit) {
        pathAnimationTarget = 1f
    }

    LaunchedEffect(detailState.chapter) {
        if (detailState.chapter != null) {
            showButtons = true
        }
    }

    LaunchedEffect(chapterId) {
        viewModel.loadChapterById(chapterId)
    }

    LaunchedEffect(detailState.error) {
        detailState.error?.let {
            snackbarHostState.showSnackbar("Gagal memuat level. Coba lagi.")
        }
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

    Box(modifier = Modifier.fillMaxSize()) {
        val chapter = detailState.chapter
        val hasChapter = chapter != null
        val hasError = detailState.error != null
        val isLoading = !hasChapter && !hasError

        val placeholderLevels = remember(chapterId) {
            generateLevelPositions(4, chapterId)
        }
        val levelModels = remember(detailState.chapter) {
            if (hasChapter) viewModel.getLevelModels() else emptyList()
        }
        val dynamicLevels = remember(chapterId, chapter?.quizzes?.size ?: 0) {
            if (chapter != null) generateLevelPositions(chapter.quizzes.size, chapterId)
            else placeholderLevels
        }
        val displayLevels = if (hasChapter) dynamicLevels else placeholderLevels

        val visibleCount = remember { mutableIntStateOf(0) }
        LaunchedEffect(Unit) {
            for (i in 1..4) {
                delay(250L)
                visibleCount.intValue = i
            }
        }

        val pulseTransition = rememberInfiniteTransition(label = "pulse")
        val pulseAlpha by pulseTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.7f,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_alpha"
        )

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
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.level_background_no_path)
                        .build(),
                    contentDescription = "bg",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                LevelPath(
                    levels = displayLevels,
                    pathAnimationProgress = { pathAnimationProgress },
                    buttonCenterOffsetPx = buttonCenterOffset,
                    modifier = Modifier.fillMaxSize()
                )

                if (hasChapter) {
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
                } else if (isLoading) {
                    placeholderLevels.forEachIndexed { index, levelPosition ->
                        val itemAlpha by animateFloatAsState(
                            targetValue = if (index < visibleCount.intValue) 1f else 0f,
                            animationSpec = tween(durationMillis = 400),
                            label = "item_$index"
                        )

                        Box(
                            modifier = Modifier
                                .offset(
                                    x = screenWidth * levelPosition.xFraction - buttonCenterDp,
                                    y = contentHeight * levelPosition.yFraction
                                )
                                .graphicsLayer { alpha = itemAlpha }
                        ) {
                            Surface(
                                modifier = Modifier.size(50.dp),
                                shape = CircleShape,
                                color = Color.Gray.copy(alpha = pulseAlpha),
                                shadowElevation = 0.dp
                            ) {}
                        }
                    }
                }
            }
        }

        if (hasError) {
            ErrorRetryBox(
                message = detailState.error ?: "Terjadi kesalahan",
                onRetry = { viewModel.loadChapterById(chapterId) }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

            IconButton(
                onClick = {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 72.dp)
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
