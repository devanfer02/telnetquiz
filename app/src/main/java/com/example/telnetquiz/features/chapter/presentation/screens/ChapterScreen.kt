package com.example.telnetquiz.features.chapter.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.components.EmptyStateBox
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.components.Navbar
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.chapter.presentation.components.ChapterCardFromApi
import com.example.telnetquiz.features.chapter.presentation.components.ChapterCardSkeleton
import com.example.telnetquiz.features.chapter.presentation.components.ComingSoonCard
import com.example.telnetquiz.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.telnetquiz.components.ProfileTopBar
import com.example.telnetquiz.features.user.presentation.viewmodel.ProfileViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme

@Composable
fun ChapterScreen(
    navController: NavController,
    viewModel: ChapterViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.listState.collectAsState()
    val profileState by profileViewModel.state.collectAsState()
    val selectedAvatarIndex by profileViewModel.selectedAvatarIndex.collectAsState()
    val tag by profileViewModel.tag.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadChapters()
        profileViewModel.loadProfile()
    }

    // Redirect to pretest if user hasn't taken it (wait for fresh profile data)
    LaunchedEffect(profileState.isLoading, profileState.profile?.hasTakenPretest) {
        if (!profileState.isLoading && profileState.profile?.hasTakenPretest == false) {
            navController.navigate(Screen.QuickCheckScreen.route) {
                popUpTo(Screen.HomeScreen.route) { inclusive = true }
            }
        }
    }
    Scaffold(
        topBar = {
                ProfileTopBar(
                    isLoading = profileState.isLoading,
                    name = profileState.profile?.fullname ?: "",
                    school = profileState.profile?.school?.name ?: "",
                    imageUrl = profileState.profile?.image,
                    gender = profileState.profile?.gender,
                    localAvatarResId = AvatarConstants.getAvatarResId(selectedAvatarIndex),
                    totalScore = profileState.profile?.stats?.totalScore ?: 0,
                    dailyStreak = profileState.profile?.stats?.dailyStreak ?: 0,
                    tag = tag
                )
            },
            modifier = Modifier.systemBarsPadding()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(LitecartesColor.Surface)
            ) {
                when {
                    state.isLoading -> {
                        LazyColumn {
                            items(3) {
                                Spacer(modifier = Modifier.padding(5.dp))
                                Box(
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                ) {
                                    ChapterCardSkeleton()
                                }
                                Spacer(modifier = Modifier.padding(5.dp))
                            }
                        }
                    }
                    state.error != null -> {
                        ErrorRetryBox(
                            message = state.error ?: "Terjadi kesalahan",
                            onRetry = { viewModel.loadChapters() }
                        )
                    }
                    state.chapters.isNotEmpty() -> {
                        LazyColumn {
                            itemsIndexed(state.chapters) { index, chapter ->
                                Spacer(modifier = Modifier.padding(5.dp))
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 10.dp
                                        )
                                ) {
                                    ChapterCardFromApi(
                                        chapter = chapter,
                                        onClick = {
                                            navController.navigate(
                                                "${Screen.LevelScreen.route}/${chapter.id}"
                                            )
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.padding(5.dp))
                            }
                            // Add Coming Soon card if fewer than 3 chapters
                            if (state.chapters.size < 3) {
                                item {
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    ComingSoonCard()
                                    Spacer(modifier = Modifier.padding(10.dp))
                                }
                            }
                        }
                    }
                    else -> {
                        EmptyStateBox(
                            title = "Belum ada materi tersedia",
                            subtitle = "Silakan coba lagi nanti"
                        )
                    }
                }
            }
            Navbar(navController = navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChapterScreen() {
    LitecartesNativeTheme {
        ChapterScreen(
            navController = rememberNavController()
        )
    }
}
