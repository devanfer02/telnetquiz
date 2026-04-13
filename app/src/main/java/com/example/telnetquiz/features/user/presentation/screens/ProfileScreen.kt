package com.example.telnetquiz.features.user.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.local.AudioSettings
import com.example.telnetquiz.features.user.presentation.components.AchievementCard
import com.example.telnetquiz.features.user.presentation.components.AchievementCardSkeleton
import com.example.telnetquiz.features.user.presentation.components.ProfileHeaderSection
import com.example.telnetquiz.features.user.presentation.components.SoundSettingsDialog
import com.example.telnetquiz.features.user.presentation.components.StatCard
import com.example.telnetquiz.features.user.presentation.viewmodel.AchievementViewModel
import com.example.telnetquiz.features.user.presentation.viewmodel.ProfileViewModel
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
    val selectedAvatarIndex by viewModel.selectedAvatarIndex.collectAsState()
    var showSoundSettings by remember { mutableStateOf(false) }
    var isHeaderExpanded by remember { mutableStateOf(true) }

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

    Column(
        modifier = Modifier
            .background(LitecartesColor.Surface)
            .fillMaxSize()
    ) {
        Column {
            AnimatedVisibility(
                visible = isHeaderExpanded,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                ProfileHeaderSection(
                    profile = state.profile,
                    isLoading = state.isLoading,
                    error = state.error,
                    localAvatarResId = AvatarConstants.getAvatarResId(selectedAvatarIndex),
                    onSettingsClick = { showSoundSettings = true },
                    onEditProfile = { navController.navigate(Screen.EditProfileScreen.route) }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isHeaderExpanded = !isHeaderExpanded }
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.Gray.copy(alpha = 0.3f))
                )
            }
        }
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
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(
                                label = "Total Skor",
                                value = "${stats.totalScore}",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Level Usai",
                                value = "${stats.levelsCompleted}",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Bab Usai",
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
