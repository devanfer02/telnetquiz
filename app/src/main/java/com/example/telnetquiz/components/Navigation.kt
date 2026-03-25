package com.example.telnetquiz.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.telnetquiz.R
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.auth.presentation.screens.AboutScreen
import com.example.telnetquiz.features.auth.presentation.screens.AuthLoginScreen
import com.example.telnetquiz.features.auth.presentation.screens.AuthRegisterScreen
import com.example.telnetquiz.features.auth.presentation.screens.AuthStartScreen
import com.example.telnetquiz.features.auth.presentation.screens.FeedbackScren
import com.example.telnetquiz.features.auth.presentation.viewmodel.AuthViewModel
import com.example.telnetquiz.features.auth.presentation.viewmodel.SessionState
import com.example.telnetquiz.features.chapter.presentation.screens.ChapterScreen
import com.example.telnetquiz.features.pretest.presentation.screens.PretestResultScreen
import com.example.telnetquiz.features.pretest.presentation.screens.PretestScreen
import com.example.telnetquiz.features.pretest.presentation.screens.QuickCheckScreen
import com.example.telnetquiz.features.quiz.presentation.screens.LevelScreen
import com.example.telnetquiz.features.quiz.presentation.screens.QuestionScreen
import com.example.telnetquiz.features.quiz.presentation.screens.RemedialScreen
import com.example.telnetquiz.features.quiz.presentation.screens.ResultScreen
import com.example.telnetquiz.features.quiz.presentation.singletons.QuizResultHolder
import com.example.telnetquiz.features.user.presentations.screens.EditProfileScreen
import com.example.telnetquiz.features.user.presentations.screens.LeaderboardScreen
import com.example.telnetquiz.features.user.presentations.screens.ProfileScreen
import com.example.telnetquiz.ui.theme.LitecartesColor
import kotlinx.coroutines.flow.collectLatest


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

@Composable
private fun MainNavHost(
    navController: NavHostController,
    startDestination: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LitecartesColor.Surface)
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
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
            route = Screen.QuickCheckScren.route
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
                navController = navController
            )
        }
        composable(
            route = Screen.HomeScreen.route
        ) {
            ChapterScreen(navController = navController)
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
            route = "${Screen.FeedbackScreen.route}/{chapterId}/levels/{level}/questions/{id}?materialId={materialId}",
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

            FeedbackScren(
                chapterId = chapterId,
                level = level,
                id = id,
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
            val quizResult = QuizResultHolder.lastResult

            ResultScreen(
                navController = navController,
                chapterId = chapterId,
                quizResult = quizResult
            )
        }
        }
    }
}
