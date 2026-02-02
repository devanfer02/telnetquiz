package com.example.litecartesnative.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.litecartesnative.features.auth.presentation.screens.AuthLoginScreen
import com.example.litecartesnative.features.auth.presentation.screens.AuthRegisterScreen
import com.example.litecartesnative.features.auth.presentation.screens.AuthStartScreen
import com.example.litecartesnative.features.auth.presentation.viewmodel.AuthViewModel
import com.example.litecartesnative.features.pretest.presentation.screens.PretestScreen
import com.example.litecartesnative.features.pretest.presentation.screens.QuickCheckScreen
import com.example.litecartesnative.features.user.presentations.screens.LeaderboardScreen
import com.example.litecartesnative.features.chapter.presentation.screens.ChapterScreen
import com.example.litecartesnative.features.quiz.presentation.screens.LevelScreen
import com.example.litecartesnative.features.quiz.presentation.screens.QuestionScreen
import com.example.litecartesnative.features.user.presentations.screens.FriendScreen
import com.example.litecartesnative.features.user.presentations.screens.ProfileScreen
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.features.auth.presentation.screens.AboutScreen
import com.example.litecartesnative.features.auth.presentation.screens.FeedbackScren
import com.example.litecartesnative.features.auth.presentation.viewmodel.SessionState
import com.example.litecartesnative.features.quiz.presentation.screens.ResultScreen
import com.example.litecartesnative.features.quiz.presentation.singletons.MarkAsDoneManager
import com.example.litecartesnative.features.quiz.presentation.singletons.WrongQuizManager
import com.example.litecartesnative.ui.theme.LitecartesColor
import kotlinx.coroutines.flow.collectLatest


@Composable
fun Navigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val sessionState by authViewModel.sessionState.collectAsState()

    // Handle session expiration during app usage
    LaunchedEffect(Unit) {
        authViewModel.sessionExpiredEvent.collectLatest {
            Log.d("Navigation", "Session expired, redirecting to login")
            navController.navigate(Screen.AuthLoginScreen.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Conditional rendering based on session state
    Log.d("SESSION", sessionState.toString())
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
        CircularProgressIndicator(color = LitecartesColor.Secondary)
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    startDestination: String
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
            route = "${Screen.QuestionScreen.route}/{quizId}",
            arguments = listOf(
                navArgument("quizId") {
                    type = NavType.IntType
                }
            )
        ) {
            val quizId = it.arguments?.getInt("quizId") ?: 1

            QuestionScreen(
                quizId = quizId,
                navController = navController
            )
        }
        composable(
            route = "${Screen.FeedbackScreen.route}/{chapterId}/levels/{level}/questions/{id}",
            arguments = listOf(
                navArgument("chapterId") {
                    type = NavType.IntType
                },
                navArgument("level") {
                    type = NavType.IntType
                },
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            val chapterId = it.arguments?.getInt("chapterId") ?: 1
            val level = it.arguments?.getInt("level") ?: 1
            val id = it.arguments?.getInt("id") ?: 1

            FeedbackScren(
                chapterId = chapterId,
                level = level,
                id = id,
                navController = navController
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
            route = Screen.FriendScreen.route
        ) {
            FriendScreen(navController = navController)
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
            val level = it.arguments?.getInt("level") ?: 0

            LaunchedEffect(key1 = chapterId) {
                Log.d("QUEUEMANAGER", "${WrongQuizManager.queue.toString()}")
                if (!WrongQuizManager.queue.isEmpty()) {
                    val quizIndex = WrongQuizManager.queue.first()
                    navController.navigate(
                        "${Screen.FeedbackScreen.route}/${quizIndex.chapterId}/levels/${quizIndex.level}/questions/${quizIndex.id}"
                    )
                    WrongQuizManager.queue.removeFirst()
                }

                MarkAsDoneManager.levels[chapterId][level -1] = true
            }

            ResultScreen(
                navController = navController,
                chapterId = chapterId
            )
        }
    }
}