package com.example.telnetquiz.features.user.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.components.LocalAuthViewModel
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.local.AudioSettings
import com.example.telnetquiz.features.user.presentation.components.AchievementCard
import com.example.telnetquiz.features.user.presentation.components.AchievementCardSkeleton
import com.example.telnetquiz.features.user.presentation.components.ProfileHeaderSection
import com.example.telnetquiz.features.user.presentation.components.SettingsDialog
import com.example.telnetquiz.features.user.presentation.components.StatCard
import com.example.telnetquiz.features.user.presentation.viewmodel.AchievementViewModel
import com.example.telnetquiz.features.user.presentation.viewmodel.ProfileViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ProfileScreen(
    navController: NavController,
    onReplayTutorial: () -> Unit = {},
    onOpenPanduanUmum: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
    achievementViewModel: AchievementViewModel = hiltViewModel()
) {
    val authViewModel = LocalAuthViewModel.current
    val state by viewModel.state.collectAsState()
    val achievementState by achievementViewModel.state.collectAsState()
    val audioSettings by viewModel.audioManager.audioSettingsFlow.collectAsState(initial = AudioSettings())
    val selectedAvatarIndex by viewModel.selectedAvatarIndex.collectAsState()
    var showSoundSettings by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val tutorialController = LocalTutorialController.current

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        achievementViewModel.loadAchievements()
    }

    if (showSoundSettings) {
        SettingsDialog(
            audioSettings = audioSettings,
            onDismiss = { showSoundSettings = false },
            onMutedChange = { viewModel.audioManager.toggleMute() },
            onGlobalVolumeChange = { viewModel.audioManager.setGlobalVolume(it) },
            onSfxVolumeChange = { viewModel.audioManager.setSfxVolume(it) },
            onBgMusicVolumeChange = { viewModel.audioManager.setBgMusicVolume(it) },
            onReplayTutorial = {
                showSoundSettings = false
                onReplayTutorial()
            },
            onOpenPanduanUmum = {
                showSoundSettings = false
                onOpenPanduanUmum()
            },
            onLogout = {
                showSoundSettings = false
                showLogoutDialog = true
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Keluar",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Apakah kamu yakin ingin keluar dari akun?",
                    fontFamily = nunitosFontFamily
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    authViewModel.logout()
                    navController.navigate(Screen.AuthStartScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }) {
                    Text(
                        text = "Keluar",
                        color = Color.Red,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(
                        text = "Batal",
                        fontFamily = nunitosFontFamily,
                        color = LitecartesColor.Secondary
                    )
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .background(LitecartesColor.Surface)
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 64.dp)
    ) {
        item {
            Box(
                modifier = if (tutorialController != null) Modifier.onGloballyPositioned {
                    tutorialController.registerTarget("profile_header", it)
                } else Modifier
            ) {
                ProfileHeaderSection(
                    profile = state.profile,
                    isLoading = state.isLoading,
                    error = state.error,
                    localAvatarResId = AvatarConstants.getAvatarResId(selectedAvatarIndex),
                    onSettingsClick = { showSoundSettings = true },
                    onEditProfile = {
                        tutorialController?.notifyTargetClicked("profile_header")
                        navController.navigate(Screen.EditProfileScreen.route)
                    }
                )
            }
        }
        state.profile?.stats?.let { stats ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .then(
                            if (tutorialController != null) Modifier.onGloballyPositioned {
                                tutorialController.registerTarget("profile_stats", it)
                            } else Modifier
                        ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                        StatCard(
                            label = "Total Skor",
                            value = "${stats.totalScore}",
                            icon = Icons.Filled.Bolt,
                            iconTint = Color(0xFF1565C0),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Level Usai",
                            value = "${stats.levelsCompleted}",
                            icon = Icons.Filled.Flag,
                            iconTint = LitecartesColor.Primary,
                            modifier = Modifier.weight(1f)
                        )
                        val chapterValue = if (stats.totalChapters > 0)
                            "${stats.chaptersCompleted}/${stats.totalChapters}"
                        else "${stats.chaptersCompleted}"
                        StatCard(
                            label = "Bab Usai",
                            value = chapterValue,
                            icon = Icons.Filled.AutoStories,
                            iconTint = LitecartesColor.GreenCactus,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            item {
                val totalAch = achievementState.achievements.size
                val unlockedAch = achievementState.achievements.count { it.unlocked }
                PencapaianHeader(
                    unlocked = unlockedAch,
                    total = totalAch,
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .then(
                            if (tutorialController != null) Modifier.onGloballyPositioned {
                                tutorialController.registerTarget("profile_achievements", it)
                            } else Modifier
                        )
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            when {
                achievementState.isLoading -> {
                    items(4) {
                        AchievementCardSkeleton(modifier = Modifier.padding(horizontal = 14.dp))
                    }
                }
                achievementState.error != null -> {
                    item {
                        ErrorRetryBox(
                            message = achievementState.error ?: "Gagal memuat pencapaian",
                            onRetry = { achievementViewModel.loadAchievements() }
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp)
                        )
                    }
                }
                else -> {
                    items(
                        items = achievementState.achievements,
                        key = { it.id }
                    ) { achievement ->
                        AchievementCard(
                            title = achievement.title,
                            description = achievement.description,
                            unlocked = achievement.unlocked,
                            unlockedAt = achievement.unlockedAt,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                    }
                }
            }
        }
    }

@Composable
private fun PencapaianHeader(unlocked: Int, total: Int, modifier: Modifier = Modifier) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.EmojiEvents,
            contentDescription = null,
            tint = LitecartesColor.Primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = "PENCAPAIAN",
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            color = LitecartesColor.Secondary
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(
                    color = LitecartesColor.Primary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$unlocked / $total terbuka",
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                color = LitecartesColor.Primary
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
