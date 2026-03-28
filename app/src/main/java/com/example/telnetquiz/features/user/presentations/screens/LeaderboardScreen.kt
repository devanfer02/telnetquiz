package com.example.telnetquiz.features.user.presentations.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.Navbar
import com.example.telnetquiz.data.remote.dto.LeaderboardEntryDto
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.features.user.presentations.components.ActivityEntryCard
import com.example.telnetquiz.features.user.presentations.components.PositionCard
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
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = LitecartesColor.Primary
                                )
                            }
                        }
                        activityState.error != null -> {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = activityState.error ?: "Terjadi kesalahan",
                                    color = LitecartesColor.Secondary,
                                    fontFamily = nunitosFontFamily
                                )
                            }
                        }
                        activityState.days.all { it.entries.isEmpty() } -> {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.start_screen),
                                    contentDescription = "Mascot",
                                    modifier = Modifier.size(160.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Belum ada aktivitas",
                                    color = LitecartesColor.Secondary,
                                    fontFamily = nunitosFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Yuk mainkan level pertamamu!",
                                    color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                                    fontFamily = nunitosFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
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
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.error ?: "Terjadi kesalahan",
                                    color = LitecartesColor.Secondary
                                )
                            }
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
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(LitecartesColor.Secondary.copy(alpha = 0.1f))
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Peringkat kamu: #${currentUser.rank}",
                                                    fontFamily = nunitosFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = LitecartesColor.Secondary,
                                                    fontSize = 14.sp
                                                )
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = "${currentUser.totalScore} XP",
                                                    fontFamily = nunitosFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = LitecartesColor.Primary,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
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

@Composable
private fun SegmentedToggle(
    selectedTab: LeaderboardTab,
    onTabSelected: (LeaderboardTab) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LitecartesColor.DarkerSurface)
            .fillMaxWidth()
    ) {
        LeaderboardTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            val label = when (tab) {
                LeaderboardTab.PROGRESS -> "Aktivitas Harian"
                LeaderboardTab.LEADERBOARD -> "Papan Peringkat"
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) LitecartesColor.Primary else Color.Transparent
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isSelected) Color.White else LitecartesColor.Secondary,
                    textAlign = TextAlign.Center
                )
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
        exp = totalScore
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLeaderboardScreen() {
    LitecartesNativeTheme {
        LeaderboardScreen(navController = rememberNavController())
    }
}
