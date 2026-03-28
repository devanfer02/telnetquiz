package com.example.telnetquiz.features.user.presentations.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.components.Navbar
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.local.AudioSettings
import com.example.telnetquiz.features.user.presentations.components.AchievementCard
import com.example.telnetquiz.features.user.presentations.components.AchievementCardSkeleton
import com.example.telnetquiz.features.user.presentations.components.ProfileHeaderSection
import com.example.telnetquiz.features.user.presentations.components.SoundSettingsDialog
import com.example.telnetquiz.features.user.presentations.components.StatCard
import com.example.telnetquiz.features.user.presentations.viewmodel.AchievementViewModel
import com.example.telnetquiz.features.user.presentations.viewmodel.ProfileViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    achievementViewModel: AchievementViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val achievementState by achievementViewModel.state.collectAsState()
    val audioSettings by viewModel.audioManager.audioSettingsFlow.collectAsState(initial = AudioSettings())
    var showSoundSettings by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        achievementViewModel.loadAchievements()
    }

    if (showSoundSettings) {
        SoundSettingsDialog(
            audioSettings = audioSettings,
            onDismiss = { showSoundSettings = false },
            onMutedChange = { viewModel.audioManager.toggleMute() },
            onGlobalVolumeChange = { viewModel.audioManager.setGlobalVolume(it) },
            onSfxVolumeChange = { viewModel.audioManager.setSfxVolume(it) },
            onBgMusicVolumeChange = { viewModel.audioManager.setBgMusicVolume(it) }
        )
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(LitecartesColor.Surface)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileHeaderSection(
                profile = state.profile,
                isLoading = state.isLoading,
                error = state.error,
                onSettingsClick = { showSoundSettings = true },
                onEditProfile = { navController.navigate(Screen.EditProfileScreen.route) }
            )
            LazyColumn(
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .weight(1f)
            ) {
                state.profile?.stats?.let { stats ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(
                                label = "Total Skor",
                                value = "${stats.totalScore}",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Level Selesai",
                                value = "${stats.levelsCompleted}",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Bab Selesai",
                                value = "${stats.chaptersCompleted}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                item {
                    Text(
                        text = "Pencapaian",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = LitecartesColor.Secondary,
                        modifier = Modifier
                            .padding(
                                bottom = 12.dp
                            )
                    )
                }

                when {
                    achievementState.isLoading -> {
                        items(4) {
                            AchievementCardSkeleton()
                        }
                    }
                    achievementState.error != null -> {
                        item {
                            ErrorRetryBox(
                                message = achievementState.error ?: "Gagal memuat pencapaian"
                            )
                        }
                    }
                    achievementState.achievements.isEmpty() -> {
                        item {
                            Text(
                                text = "Belum ada pencapaian. Selesaikan quiz untuk mendapatkan pencapaian!",
                                color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                                fontFamily = nunitosFontFamily,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    else -> {
                        items(achievementState.achievements) { achievement ->
                            AchievementCard(
                                title = achievement.title,
                                description = achievement.description,
                                unlocked = achievement.unlocked
                            )
                        }
                    }
                }
            }
            Navbar(
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewProfileScreen() {
    LitecartesNativeTheme {
        ProfileScreen(
            navController = rememberNavController()
        )
    }
}
