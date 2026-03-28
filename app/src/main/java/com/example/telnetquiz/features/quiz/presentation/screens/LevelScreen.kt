package com.example.telnetquiz.features.quiz.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.MascotLoadingScreen
import com.example.telnetquiz.components.Navbar
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.repository.Result
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.telnetquiz.features.quiz.presentation.util.generateLevelPositions
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.features.quiz.presentation.components.LevelButton
import com.example.telnetquiz.features.quiz.presentation.components.LevelOptionMenu
import com.example.telnetquiz.features.quiz.presentation.components.LevelPath
import com.example.telnetquiz.features.quiz.presentation.components.ProfileTopBar
import com.example.telnetquiz.features.quiz.presentation.singletons.LearnFirstHolder
import com.example.telnetquiz.features.quiz.presentation.singletons.ProfileCache
import com.example.telnetquiz.features.user.presentations.viewmodel.ProfileViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
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
    val selectedAvatarIndex by profileViewModel.selectedAvatarIndex.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showLevelDialog by remember { mutableStateOf(false) }
    var selectedQuizId by remember { mutableIntStateOf(0) }
    var selectedQuizLevel by remember { mutableIntStateOf(0) }
    var selectedQuizScore by remember { mutableStateOf<Int?>(null) }
    var isFetchingMaterials by remember { mutableStateOf(false) }

    var pathAnimationTarget by remember { mutableFloatStateOf(0f) }
    var showButtons by remember { mutableStateOf(false) }
    val pathAnimationProgress by animateFloatAsState(
        targetValue = pathAnimationTarget,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "path_animation"
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (showButtons) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
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
                isLoading = profileState.isLoading,
                name = profileState.profile?.fullname ?: "",
                school = profileState.profile?.school?.name ?: "",
                imageUrl = profileState.profile?.image,
                gender = profileState.profile?.gender,
                localAvatarResId = AvatarConstants.getAvatarResId(selectedAvatarIndex),
                totalScore = profileState.profile?.stats?.totalScore ?: 0,
                dailyStreak = profileState.profile?.stats?.dailyStreak ?: 0,
                tag = ProfileCache.getTag()
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (detailState.chapter == null && detailState.error == null) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        val loadingHeight = maxWidth * 1.94f
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
                        val completedQuizIds = chapter.completedQuizIds
                        val quizScores = chapter.quizScores
                        val dynamicLevels = remember(chapterId, quizzes.size) {
                            generateLevelPositions(quizzes.size, chapterId)
                        }

                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            val screenWidth = maxWidth
                            val contentHeight = screenWidth * 1.94f
                            val buttonCenterOffset = with(LocalDensity.current) { 25.dp.toPx() }

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
                                    pathAnimationProgress = pathAnimationProgress,
                                    buttonCenterOffsetPx = buttonCenterOffset,
                                    modifier = Modifier.fillMaxSize()
                                )

                                quizzes.forEachIndexed { index, quiz ->
                                    if (index >= dynamicLevels.size) return@forEachIndexed

                                    val levelPosition = dynamicLevels[index]
                                    val isCompleted = quiz.id in completedQuizIds
                                    val isUnlocked = index == 0 ||
                                        quizzes[index - 1].id in completedQuizIds

                                    Box(
                                        modifier = Modifier
                                            .offset(
                                                x = screenWidth * levelPosition.xFraction - with(LocalDensity.current) { buttonCenterOffset.toDp() },
                                                y = contentHeight * levelPosition.yFraction
                                            )
                                            .alpha(buttonAlpha)
                                    ) {
                                        LevelButton(
                                            level = quiz.level,
                                            onClick = {
                                                selectedQuizId = quiz.id
                                                selectedQuizLevel = quiz.level
                                                selectedQuizScore = quizScores[quiz.id.toString()]
                                                showLevelDialog = true
                                            },
                                            done = isCompleted,
                                            isLocked = !isUnlocked,
                                            score = quizScores[quiz.id.toString()]
                                        )
                                        LevelOptionMenu(
                                            expanded = showLevelDialog && selectedQuizId == quiz.id,
                                            onDismiss = { showLevelDialog = false },
                                            score = quizScores[quiz.id.toString()],
                                            onLearnFirst = {
                                                showLevelDialog = false
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
                                                                isFetchingMaterials = false
                                                                navController.navigate(
                                                                    "${Screen.FeedbackScreen.route}/${chapterId}/levels/${selectedQuizLevel}/questions/0?materialId=${firstMaterial.id}"
                                                                )
                                                            } else {
                                                                isFetchingMaterials = false
                                                                navController.navigate(
                                                                    "${Screen.QuestionScreen.route}/${selectedQuizId}"
                                                                )
                                                            }
                                                        }
                                                        is Result.Error -> {
                                                            isFetchingMaterials = false
                                                            navController.navigate(
                                                                "${Screen.QuestionScreen.route}/${selectedQuizId}"
                                                            )
                                                        }
                                                        is Result.Loading -> {}
                                                    }
                                                }
                                            },
                                            onPlayDirectly = {
                                                viewModel.audioManager.playSfx(SfxType.START_LEVEL)
                                                showLevelDialog = false
                                                navController.navigate(
                                                    "${Screen.QuestionScreen.route}/${selectedQuizId}"
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Navbar(
                    navController = navController,
                    backgroundColor = Color.Transparent
                )
            }
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
