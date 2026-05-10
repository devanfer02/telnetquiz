package com.example.telnetquiz.features.user.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.TopBarContainer
import com.example.telnetquiz.components.EmptyStateBox
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.data.remote.dto.LeaderboardEntryDto
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.features.user.presentation.components.ActivityDateHeaderSkeleton
import com.example.telnetquiz.features.user.presentation.components.ActivityEntryCard
import com.example.telnetquiz.features.user.presentation.components.ActivityEntryCardSkeleton
import com.example.telnetquiz.features.user.presentation.components.CurrentUserRankBox
import com.example.telnetquiz.features.user.presentation.components.PositionCard
import com.example.telnetquiz.features.user.presentation.components.SegmentedToggle
import com.example.telnetquiz.features.user.presentation.components.Top3Profile
import com.example.telnetquiz.features.user.presentation.components.Top3ProfileSkeleton
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
    var isTop3Expanded by remember { mutableStateOf(true) }
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

                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedVisibility(
                        visible = selectedTab == LeaderboardTab.LEADERBOARD && isTop3Expanded,
                        enter = expandVertically(expandFrom = Alignment.Top),
                        exit = shrinkVertically(shrinkTowards = Alignment.Top)
                    ) {
                        when {
                            state.isLoading -> {
                                Row(
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(top = 18.dp)
                                    ) { Top3ProfileSkeleton() }
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) { Top3ProfileSkeleton() }
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(top = 28.dp)
                                    ) { Top3ProfileSkeleton() }
                                }
                            }
                            state.leaderboard.isNotEmpty() && state.leaderboard.size < 3 -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.property_variant2),
                                        contentDescription = null,
                                        modifier = Modifier.size(100.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Belum ada penjelajah lainnya",
                                        fontFamily = nunitosFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tunggu saja kedatangan mereka!",
                                        fontFamily = nunitosFontFamily,
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        state.leaderboard.size >= 3 -> {
                                val top3 = state.leaderboard.take(3)
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 12.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(top = 18.dp)
                                    ) {
                                        Top3Profile(
                                            user = top3[1].toUser(),
                                            rank = 2,
                                            avatarResIdOverride = if (top3[1].rank == state.currentUser?.rank) localAvatarResId else null
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Top3Profile(
                                            user = top3[0].toUser(),
                                            rank = 1,
                                            avatarResIdOverride = if (top3[0].rank == state.currentUser?.rank) localAvatarResId else null
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(top = 28.dp)
                                    ) {
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
                                    Image(
                                        painter = painterResource(id = R.drawable.property_variant2),
                                        contentDescription = null,
                                        modifier = Modifier.size(100.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Belum ada penjelajah lainnya",
                                        fontFamily = nunitosFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tunggu saja kedatangan mereka!",
                                        fontFamily = nunitosFontFamily,
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = selectedTab == LeaderboardTab.LEADERBOARD
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isTop3Expanded = !isTop3Expanded }
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color.White.copy(alpha = 0.5f))
                            )
                        }
                    }
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
                                items(4) {
                                    ActivityEntryCardSkeleton()
                                    Spacer(modifier = Modifier.height(8.dp))
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
                        activityState.days.all { it.entries.isEmpty() } -> {
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
                                    if (day.entries.isNotEmpty()) {
                                        item {
                                            Text(
                                                text = formatDateHeader(day.date),
                                                fontFamily = nunitosFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = LitecartesColor.Secondary,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                        day.entries.forEachIndexed { idx, entry ->
                                            item {
                                                ActivityEntryCard(
                                                    index = idx + 1,
                                                    entry = entry
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                LeaderboardTab.LEADERBOARD -> {
                    when {
                        state.error != null -> {
                            ErrorRetryBox(
                                message = state.error ?: "Terjadi kesalahan",
                                onRetry = { viewModel.loadLeaderboard() },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        !state.isLoading && state.leaderboard.isEmpty() -> {
                            EmptyStateBox(
                                title = "Belum ada juara di arena ini!",
                                subtitle = "Jadilah Penjelajah pertama yang menaklukkan tantangan!",
                                imageResId = R.drawable.start_screen,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            )
                        }
                        else -> {
                            val remainingEntries = if (state.leaderboard.size > 3) {
                                state.leaderboard.drop(3)
                            } else {
                                emptyList()
                            }

                            LazyColumn(
                                modifier = Modifier
                                    .padding(start = 14.dp, end = 14.dp)
                                    .weight(1f),
                                contentPadding = PaddingValues(bottom = 64.dp)
                            ) {
                                // Show current user's rank if available
                                state.currentUser?.let { currentUser ->
                                    item {
                                        Spacer(modifier = Modifier.padding(6.dp))
                                        CurrentUserRankBox(
                                            rank = currentUser.rank,
                                            totalScore = currentUser.totalScore
                                        )
                                    }
                                }

                                itemsIndexed(
                                    remainingEntries,
                                    key = { _, entry -> entry.userId }
                                ) { _, entry ->
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    PositionCard(
                                        user = entry.toUser(),
                                        rank = entry.rank,
                                        avatarResIdOverride = if (entry.rank == state.currentUser?.rank) localAvatarResId else null
                                    )
                                    Spacer(modifier = Modifier.padding(4.dp))
                                }
                            }
                        }
                    }
                }
            }

        }
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
