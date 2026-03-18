package com.example.litecartesnative.features.user.presentations.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.components.Navbar
import com.example.litecartesnative.data.remote.dto.LeaderboardEntryDto
import com.example.litecartesnative.features.user.domain.model.User
import com.example.litecartesnative.features.user.presentations.components.PositionCard
import com.example.litecartesnative.features.user.presentations.components.Top3Profile
import com.example.litecartesnative.features.user.presentations.viewmodel.LeaderboardViewModel
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.LitecartesNativeTheme
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLeaderboard()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(LitecartesColor.Surface)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
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
                        .padding(
                            top = 16.dp
                        )
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Papan Peringkat",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(12.dp))

                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(bottom = 12.dp)
                            )
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
                            .padding(
                                start = 14.dp,
                                end = 14.dp,
                            )
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
            Navbar(navController = navController)
        }
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
