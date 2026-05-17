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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.components.TopBarContainer
import com.example.telnetquiz.components.EmptyStateBox
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.data.remote.dto.LeaderboardEntryDto
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.features.user.presentation.components.ActivityDateHeader
import com.example.telnetquiz.features.user.presentation.components.ActivityDateHeaderSkeleton
import com.example.telnetquiz.features.user.presentation.components.ActivityEntryCardSkeleton
import com.example.telnetquiz.features.user.presentation.components.ChapterActivityBanner
import com.example.telnetquiz.features.user.presentation.components.ChapterBannerSkeleton
import com.example.telnetquiz.features.user.presentation.components.LevelActivityRow
import com.example.telnetquiz.features.user.presentation.components.CurrentUserRankBox
import com.example.telnetquiz.features.user.presentation.components.LeaderboardPeriodToggle
import com.example.telnetquiz.features.user.presentation.components.PositionCard
import com.example.telnetquiz.features.user.presentation.components.SegmentedToggle
import com.example.telnetquiz.features.user.presentation.components.Top3Profile
import com.example.telnetquiz.features.user.presentation.components.Top3ProfileSkeleton
import com.example.telnetquiz.features.user.presentation.viewmodel.LeaderboardPeriod
import com.example.telnetquiz.features.user.presentation.viewmodel.LeaderboardState
import com.example.telnetquiz.features.user.presentation.viewmodel.LeaderboardTab
import com.example.telnetquiz.features.user.presentation.viewmodel.LeaderboardViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val activityState by viewModel.activityState.collectAsState()
    val localAvatarResId by viewModel.localAvatarResId.collectAsState()
    val tutorialController = LocalTutorialController.current

    LaunchedEffect(Unit) {
        viewModel.loadRecentActivity()
    }

    Column(
        modifier = Modifier
            .background(LitecartesColor.Surface)
            .fillMaxSize()
    ) {
        TopBarContainer(
                modifier = Modifier.fillMaxWidth(),
                elevation = 20.dp,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Aktivitas",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = if (tutorialController != null) Modifier.onGloballyPositioned {
                            tutorialController.registerTarget("leaderboard_tab_toggle", it)
                        } else Modifier
                    ) {
                        SegmentedToggle(
                            selectedTab = selectedTab,
                            onTabSelected = { viewModel.selectTab(it) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                LeaderboardTab.PROGRESS -> {
                    when {
                        activityState.isLoading -> {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(start = 14.dp, end = 14.dp)
                                    .weight(1f),
                                contentPadding = PaddingValues(bottom = 64.dp)
                            ) {
                                item { Spacer(modifier = Modifier.height(12.dp)) }
                                item { ActivityDateHeaderSkeleton() }
                                item { ChapterBannerSkeleton() }
                                items(3) {
                                    ActivityEntryCardSkeleton()
                                }
                            }
                        }
                        activityState.error != null -> {
                            ErrorRetryBox(
                                message = activityState.error ?: "Terjadi kesalahan",
                                onRetry = { viewModel.loadRecentActivity() },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        activityState.days.all { it.chapterGroups.orEmpty().isEmpty() } -> {
                            EmptyStateBox(
                                title = "Belum ada aktivitas",
                                subtitle = "Yuk mainkan level pertamamu!",
                                imageResId = R.drawable.start_screen,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            )
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(start = 14.dp, end = 14.dp)
                                    .weight(1f),
                                contentPadding = PaddingValues(bottom = 64.dp)
                            ) {
                                item { Spacer(modifier = Modifier.height(12.dp)) }
                                activityState.days.forEach { day ->
                                    val groups = day.chapterGroups.orEmpty()
                                    if (groups.isNotEmpty()) {
                                        item {
                                            ActivityDateHeader(
                                                formattedDate = formatDateHeader(day.date),
                                                levelCount = day.levelCount
                                            )
                                        }
                                        groups.forEach { group ->
                                            item {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                ChapterActivityBanner(group = group)
                                                Spacer(modifier = Modifier.height(4.dp))
                                            }
                                            val entries = group.entries.orEmpty()
                                            entries.forEachIndexed { idx, entry ->
                                                item {
                                                    LevelActivityRow(
                                                        entry = entry,
                                                        isFirst = idx == 0,
                                                        isLast = idx == entries.lastIndex
                                                    )
                                                }
                                            }
                                            item { Spacer(modifier = Modifier.height(10.dp)) }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                LeaderboardTab.LEADERBOARD -> {
                    LeaderboardBody(
                        state = state,
                        localAvatarResId = localAvatarResId,
                        onSelectPeriod = { viewModel.selectPeriod(it) },
                        onRetry = { viewModel.loadLeaderboard() },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

        }
    }
}

@Composable
private fun LeaderboardBody(
    state: LeaderboardState,
    localAvatarResId: Int?,
    onSelectPeriod: (LeaderboardPeriod) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.error != null -> {
            ErrorRetryBox(
                message = state.error ?: "Terjadi kesalahan",
                onRetry = onRetry,
                modifier = modifier
            )
        }
        else -> {
            LazyColumn(
                modifier = modifier
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 64.dp)
            ) {
                item {
                    LeaderboardPeriodToggle(
                        selectedPeriod = state.period,
                        onPeriodSelected = onSelectPeriod
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    Top3PodiumCard(
                        state = state,
                        localAvatarResId = localAvatarResId
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                state.currentUser?.let { currentUser ->
                    item {
                        CurrentUserRankBox(
                            rank = currentUser.rank,
                            totalScore = currentUser.totalScore,
                            rankDelta = currentUser.rankDelta,
                            period = state.period
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                if (state.leaderboard.size > 3) {
                    item {
                        PeringkatLainnyaHeader()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    itemsIndexed(
                        state.leaderboard.drop(3),
                        key = { _, entry -> entry.userId }
                    ) { _, entry ->
                        PositionCard(
                            user = entry.toUser(),
                            rank = entry.rank,
                            avatarResIdOverride = if (entry.rank == state.currentUser?.rank) localAvatarResId else null,
                            rankDelta = entry.rankDelta,
                            period = state.period
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else if (!state.isLoading && state.leaderboard.isEmpty()) {
                    item {
                        EmptyStateBox(
                            title = "Belum ada juara di arena ini!",
                            subtitle = "Jadilah Penjelajah pertama yang menaklukkan tantangan!",
                            imageResId = R.drawable.start_screen,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Top3PodiumCard(state: LeaderboardState, localAvatarResId: Int?) {
    CardWithShadow(
        modifier = Modifier.fillMaxWidth(),
        elevation = 6.dp,
        cornerRadius = 18.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 14.dp)
        ) {
            when {
                state.isLoading -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                            Top3ProfileSkeleton()
                        }
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                            Top3ProfileSkeleton()
                        }
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                            Top3ProfileSkeleton()
                        }
                    }
                }
                state.leaderboard.size >= 3 -> {
                    val top3 = state.leaderboard.take(3)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                            Top3Profile(
                                user = top3[1].toUser(),
                                rank = 2,
                                avatarResIdOverride = if (top3[1].rank == state.currentUser?.rank) localAvatarResId else null
                            )
                        }
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                            Top3Profile(
                                user = top3[0].toUser(),
                                rank = 1,
                                avatarResIdOverride = if (top3[0].rank == state.currentUser?.rank) localAvatarResId else null
                            )
                        }
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                            Top3Profile(
                                user = top3[2].toUser(),
                                rank = 3,
                                avatarResIdOverride = if (top3[2].rank == state.currentUser?.rank) localAvatarResId else null
                            )
                        }
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Belum ada penjelajah lainnya",
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = LitecartesColor.Secondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tunggu saja kedatangan mereka!",
                            fontFamily = nunitosFontFamily,
                            fontSize = 12.sp,
                            color = LitecartesColor.Secondary.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PeringkatLainnyaHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.FormatListNumbered,
            contentDescription = null,
            tint = LitecartesColor.Primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "PERINGKAT LAINNYA",
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            color = LitecartesColor.Secondary
        )
    }
}

private fun formatDateHeader(dateString: String): String {
    val date = LocalDate.parse(dateString)
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    return when (date) {
        today -> "Hari ini"
        yesterday -> "Kemarin"
        else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("id", "ID")))
    }
}

private fun LeaderboardEntryDto.toUser(): User {
    return User(
        fullname = fullname ?: "User",
        handle = "",
        exp = totalScore,
        gender = gender
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLeaderboardScreen() {
    LitecartesNativeTheme {
        LeaderboardScreen(navController = rememberNavController())
    }
}
