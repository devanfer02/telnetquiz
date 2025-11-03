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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.components.Navbar
import com.example.litecartesnative.features.user.presentations.components.PositionCard
import com.example.litecartesnative.features.user.presentations.components.Top3Profile
import com.example.litecartesnative.features.user.domain.model.User
import com.example.litecartesnative.constants.usersDummy
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun LeaderboardScreen(
    navController: NavController
) {
    Scaffold{ innerPadding ->
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
                        text = "Leaderboard",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Row(
                        modifier = Modifier
                            .shadow(
                                elevation = 12.dp,
                                clip = false,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(LitecartesColor.DarkerSurface)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Nasional",
                            color = Color.White,
                            modifier = Modifier
                                .padding(
                                    horizontal = 15.dp
                                ),
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Kota",
                            color = Color.White,
                            modifier = Modifier
                                .padding(
                                    horizontal = 15.dp
                                ),
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Sekolah",
                            modifier = Modifier
                                .padding(
                                    horizontal = 15.dp
                                ),
                            color = Color.White,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.padding(12.dp))
                    Row(
                        modifier = Modifier
                            .padding(
                                bottom = 12.dp
                            )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(
                                top = 18.dp
                            )
                        ) {
                            Top3Profile(
                                user = User(
                                    fullname = "Tasya Gutawa",
                                    handle = "@tasyaguw",
                                    exp = 1500
                                )
                            )
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Top3Profile(
                                user = User(
                                    fullname = "Albert Einstein",
                                    handle = "@albertoo",
                                    exp = 2000
                                )
                            )
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(
                                top = 28.dp
                            )
                        ) {
                            Top3Profile(
                                user = User(
                                    fullname = "Alice Norin",
                                    handle = "@alicee_",
                                    exp = 1200
                                )
                            )
                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .padding(
                        start = 14.dp,
                        end = 14.dp,
                    )
                    .weight(1f)
            ) {
                itemsIndexed(usersDummy) { index, user ->
                    Spacer(modifier = Modifier.padding(6.dp))
                    PositionCard(
                        user = user,
                        rank = index + 4
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                }
            }
            Navbar(navController = navController)
        }
    }
}

@Preview
@Composable
fun PreviewLeaderboardScreen() {
    LeaderboardScreen(navController = rememberNavController())
}