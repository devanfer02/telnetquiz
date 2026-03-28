package com.example.telnetquiz.features.user.presentations.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.EmptyStateBox
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.components.Navbar
import com.example.telnetquiz.data.remote.dto.LeaderboardEntryDto
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.features.user.presentations.components.ActivityDateHeaderSkeleton
import com.example.telnetquiz.features.user.presentations.components.ActivityEntryCard
import com.example.telnetquiz.features.user.presentations.components.ActivityEntryCardSkeleton
import com.example.telnetquiz.features.user.presentations.components.CurrentUserRankBox
import com.example.telnetquiz.features.user.presentations.components.PositionCard
import com.example.telnetquiz.features.user.presentations.components.SegmentedToggle
import com.example.telnetquiz.features.user.presentations.components.Top3Profile
import com.example.telnetquiz.features.user.presentations.components.Top3ProfileSkeleton
import com.example.telnetquiz.features.user.presentations.viewmodel.LeaderboardTab
import com.example.telnetquiz.features.user.presentations.viewmodel.LeaderboardViewModel
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

    LaunchedEffect(Unit) {
        viewModel.loadRecentActivity()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(LitecartesColor.Surface)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Orange Header Box
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .background(LitecartesColor.Primary)
                    .fillMaxWidth(),
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

                    // Segmented Toggle
                    SegmentedToggle(
                        selectedTab = selectedTab,
                        onTabSelected = { viewModel.selectTab(it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Top-3 profiles only when Leaderboard tab is selected
                    AnimatedVisibility(
                        visible = selectedTab == LeaderboardTab.LEADERBOARD
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
                            state.leaderboard.size >= 3 -> {
                                val top3 = state.leaderboard.take(3)
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 12.dp)
                                ) {
                                    // 2nd place
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(top = 18.dp)
                                    ) {
                                        Top3Profile(
                                            user = top3[1].toUser()
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    // 1st place
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Top3Profile(
                                            user = top3[0].toUser()
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    // 3rd place
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(top = 28.dp)
                                    ) {
                                        Top3Profile(
                                            user = top3[2].toUser()
                                        )
                                    }
                                }
                            }
                            else -> {
                                Spacer(modifier = Modifier.padding(12.dp))
                            }
                        }
                    }
                }
            }

            // Content area
            when (selectedTab) {
                LeaderboardTab.PROGRESS -> {
                    when {
                        activityState.isLoading -> {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(start = 14.dp, end = 14.dp)
                                    .weight(1f)
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
                                    .weight(1f)
                            ) {
                                item { Spacer(modifier = Modifier.height(12.dp)) }
                                activityState.days.forEach { day ->
                                    if (!day.entries.isEmpty()) {
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
                                item { Spacer(modifier = Modifier.height(4.dp)) }
                            }
                        }
                    }
                }
                LeaderboardTab.LEADERBOARD -> {
                    when {
                        state.error != null -> {
                            ErrorRetryBox(
                                message = state.error ?: "Terjadi kesalahan",
                                modifier = Modifier.weight(1f)
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
                                    .weight(1f)
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

                                itemsIndexed(remainingEntries) { _, entry ->
                                    Spacer(modifier = Modifier.padding(6.dp))
                                    PositionCard(
                                        user = entry.toUser(),
                                        rank = entry.rank
                                    )
                                    Spacer(modifier = Modifier.padding(2.dp))
                                }
                            }
                        }
                    }
                }
            }

            Navbar(navController = navController)
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
