package com.example.telnetquiz.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.telnetquiz.R
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.audio.AudioManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import com.example.telnetquiz.features.auth.presentation.screens.AboutScreen
import com.example.telnetquiz.features.auth.presentation.screens.AuthLoginScreen
import com.example.telnetquiz.features.auth.presentation.screens.AuthRegisterScreen
import com.example.telnetquiz.features.auth.presentation.screens.AuthStartScreen
import com.example.telnetquiz.features.quiz.presentation.screens.StudyMaterialScreen
import com.example.telnetquiz.features.auth.presentation.viewmodel.AuthViewModel
import com.example.telnetquiz.features.auth.presentation.viewmodel.SessionState
import com.example.telnetquiz.features.user.presentation.viewmodel.ProfileViewModel
import com.example.telnetquiz.features.chapter.presentation.screens.ChapterScreen
import com.example.telnetquiz.features.pretest.presentation.screens.PretestResultScreen
import com.example.telnetquiz.features.pretest.presentation.screens.PretestScreen
import com.example.telnetquiz.features.pretest.presentation.screens.QuickCheckScreen
import com.example.telnetquiz.features.quiz.presentation.screens.LevelScreen
import com.example.telnetquiz.features.quiz.presentation.screens.QuestionScreen
import com.example.telnetquiz.features.quiz.presentation.screens.RemedialScreen
import com.example.telnetquiz.features.quiz.presentation.screens.ResultScreen
import com.example.telnetquiz.data.local.FlowResultStore
import com.example.telnetquiz.features.user.presentation.screens.EditProfileScreen
import com.example.telnetquiz.features.user.presentation.screens.LeaderboardScreen
import com.example.telnetquiz.features.user.presentation.screens.ProfileScreen
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.components.tutorial.TutorialController
import com.example.telnetquiz.components.tutorial.TutorialOverlay
import com.example.telnetquiz.data.local.TutorialPreferenceManager
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AudioManagerEntryPoint {
    fun audioManager(): AudioManager
    fun flowResultStore(): FlowResultStore
    fun tutorialPreferenceManager(): TutorialPreferenceManager
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val sessionState by authViewModel.sessionState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.sessionExpiredEvent.collectLatest {
            navController.navigate(Screen.AuthLoginScreen.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    when (sessionState) {
        SessionState.Loading -> {
            SplashLoadingScreen()
        }
        SessionState.Authenticated -> {
            MainNavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.route
            )
        }
        SessionState.Unauthenticated -> {
            MainNavHost(
                navController = navController,
                startDestination = Screen.AuthStartScreen.route
            )
        }
    }
}

@Composable
private fun SplashLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LitecartesColor.Surface),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "TelNetQuiz",
            modifier = Modifier.size(200.dp)
        )
    }
}

private fun tabIndexOf(route: String?): Int? = when {
    route == null -> null
    route.startsWith(Screen.HomeScreen.route) -> 0
    route.startsWith(Screen.LevelScreen.route) -> 0
    route.startsWith(Screen.LeaderboardScreen.route) -> 1
    route.startsWith(Screen.ProfileScreen.route) -> 2
    else -> null
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.tabSlideDirection(): SlideDirection? {
    val from = tabIndexOf(initialState.destination.route)
    val to = tabIndexOf(targetState.destination.route)
    return when {
        from != null && to != null -> {
            if (from == to) null
            else if (to > from) SlideDirection.Left else SlideDirection.Right
        }
        from == null && to != null -> SlideDirection.Right
        from != null && to == null -> SlideDirection.Left
        else -> null
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    startDestination: String
) {
    val context = LocalContext.current
    val entryPoint = remember {
        EntryPointAccessors.fromApplication(context, AudioManagerEntryPoint::class.java)
    }
    val audioManager = remember { entryPoint.audioManager() }
    val flowResultStore = remember { entryPoint.flowResultStore() }
    val tutorialPreferenceManager = remember { entryPoint.tutorialPreferenceManager() }
    val hasCompletedTutorial by tutorialPreferenceManager.hasCompletedTutorial.collectAsState(initial = true)
    val scope = rememberCoroutineScope()
    val tutorialController = remember {
        TutorialController(
            onComplete = { scope.launch { tutorialPreferenceManager.setTutorialCompleted() } },
            onNavigate = { route ->
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }
        )
    }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            destination.route?.let { route ->
                audioManager.onScreenChanged(route)
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navbarRoutes = setOf(
        Screen.HomeScreen.route,
        Screen.LeaderboardScreen.route,
        Screen.ProfileScreen.route
    )
    val showNavbar = currentRoute != null && (
        currentRoute in navbarRoutes ||
        currentRoute.startsWith(Screen.LevelScreen.route)
    )
    val showProfileTopBar = currentRoute == Screen.HomeScreen.route ||
        currentRoute?.startsWith(Screen.LevelScreen.route) == true
    val profileTopBarBg = if (currentRoute?.startsWith(Screen.LevelScreen.route) == true)
        LitecartesColor.DarkerSurface else LitecartesColor.Surface

    CompositionLocalProvider(
        LocalTutorialController provides if (!hasCompletedTutorial) tutorialController else null
    ) {
    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (showProfileTopBar) ProfileTopBar(backgroundColor = profileTopBarBg)
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(LitecartesColor.Surface),
            enterTransition = {
                tabSlideDirection()?.let {
                    slideIntoContainer(towards = it, animationSpec = tween(300))
                } ?: fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                tabSlideDirection()?.let {
                    slideOutOfContainer(towards = it, animationSpec = tween(300))
                } ?: fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                tabSlideDirection()?.let {
                    slideIntoContainer(towards = it, animationSpec = tween(300))
                } ?: fadeIn(animationSpec = tween(200))
            },
            popExitTransition = {
                tabSlideDirection()?.let {
                    slideOutOfContainer(towards = it, animationSpec = tween(300))
                } ?: fadeOut(animationSpec = tween(200))
            }
        ) {
        composable(
            route = Screen.AuthStartScreen.route
        ) {
            AuthStartScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.AboutScreen.route
        ) {
            AboutScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.AuthLoginScreen.route
        ) {
            AuthLoginScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.AuthRegisterScreen.route
        ) {
            AuthRegisterScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.QuickCheckScreen.route
        ) {
            QuickCheckScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.PretestScreen.route
        ) {
            PretestScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.PretestResultScreen.route
        ) {
            PretestResultScreen(
                navController = navController,
                audioManager = audioManager,
                flowResultStore = flowResultStore
            )
        }
        composable(
            route = Screen.HomeScreen.route
        ) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val profileState by profileViewModel.state.collectAsState()

            LaunchedEffect(profileState.isLoading, profileState.profile?.hasTakenPretest) {
                if (!profileState.isLoading && profileState.profile?.hasTakenPretest == false) {
                    navController.navigate(Screen.QuickCheckScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    }
                }
            }

            if (!profileState.isLoading && profileState.profile?.hasTakenPretest != false) {
                ChapterScreen(navController = navController)
            }
        }
        composable(
            route = "${Screen.LevelScreen.route}/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id") ?: 1

            LevelScreen(
                navController = navController,
                chapterId = id
            )
        }
        composable(
            route = "${Screen.QuestionScreen.route}/{quizId}?retry={retry}",
            arguments = listOf(
                navArgument("quizId") {
                    type = NavType.IntType
                },
                navArgument("retry") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            val quizId = it.arguments?.getInt("quizId") ?: 1
            val isRetry = it.arguments?.getBoolean("retry") ?: false

            QuestionScreen(
                quizId = quizId,
                isRetry = isRetry,
                navController = navController
            )
        }
        composable(
            route = "${Screen.StudyMaterialScreen.route}/{chapterId}/levels/{level}/questions/{id}?materialId={materialId}",
            arguments = listOf(
                navArgument("chapterId") {
                    type = NavType.IntType
                },
                navArgument("level") {
                    type = NavType.IntType
                },
                navArgument("id") {
                    type = NavType.IntType
                },
                navArgument("materialId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            val chapterId = it.arguments?.getInt("chapterId") ?: 1
            val level = it.arguments?.getInt("level") ?: 1
            val id = it.arguments?.getInt("id") ?: 1
            val materialId = it.arguments?.getInt("materialId") ?: 0

            StudyMaterialScreen(
                chapterId = chapterId,
                level = level,
                materialId = materialId,
                navController = navController
            )
        }
        composable(
            route = "${Screen.RemedialScreen.route}/{wrongCount}/{totalCount}",
            arguments = listOf(
                navArgument("wrongCount") {
                    type = NavType.IntType
                },
                navArgument("totalCount") {
                    type = NavType.IntType
                }
            )
        ) {
            val wrongCount = it.arguments?.getInt("wrongCount") ?: 0
            val totalCount = it.arguments?.getInt("totalCount") ?: 0

            RemedialScreen(
                navController = navController,
                wrongCount = wrongCount,
                totalCount = totalCount
            )
        }
        composable(
            route = Screen.LeaderboardScreen.route
        ) {
            LeaderboardScreen(navController = navController)
        }
        composable(
            route = Screen.ProfileScreen.route
        ) {
            ProfileScreen(navController = navController)
        }
        composable(
            route = Screen.EditProfileScreen.route
        ) {
            EditProfileScreen(navController = navController)
        }
        composable(
            route = "${Screen.ResultScreen.route}/{chapterId}/levels/{level}",
            arguments = listOf(
                navArgument("chapterId") {
                    type = NavType.IntType
                },
                navArgument("level") {
                    type = NavType.IntType
                }
            )
        ) {
            val chapterId = it.arguments?.getInt("chapterId") ?: 0
            val quizResult = flowResultStore.quizResult

            ResultScreen(
                navController = navController,
                chapterId = chapterId,
                quizResult = quizResult,
                audioManager = audioManager
            )
        }
        }
        }

        if (showNavbar) {
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                Navbar(navController = navController)
            }
        }

        val showTutorial = !hasCompletedTutorial && (
            (currentRoute == Screen.HomeScreen.route && tutorialController.targetBounds.containsKey("profile_top_bar")) ||
            currentRoute?.startsWith(Screen.LevelScreen.route) == true
        )

        if (showTutorial) {
            TutorialOverlay(controller = tutorialController)
        }
    }
    }
}
