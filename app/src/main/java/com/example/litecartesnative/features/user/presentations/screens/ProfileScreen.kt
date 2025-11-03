package com.example.litecartesnative.features.user.presentations.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Navbar
import com.example.litecartesnative.features.user.presentations.components.StatsIcon
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun ProfileScreen(
    navController: NavController
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(LitecartesColor.Surface)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
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
                    .padding(
                        top = 50.dp,
                        bottom = 20.dp
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        ,
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.template_profile),
                        contentDescription = "profile",
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(
                                elevation = 20.dp,
                                shape = CircleShape
                            )
                            .background(
                                LitecartesColor.Surface,
                                shape = CircleShape
                            )
                    )
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(LitecartesColor.Secondary)
                            .padding(4.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Icon",
                            tint = LitecartesColor.Surface,
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(4.dp)
                )
                Text(
                    text = "Maudy Ayunda",
                    color = Color.White,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                Text(
                    text = "SMA Negeri 1 Kota Bogor",
                    color = Color.White,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                Row {
                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(LitecartesColor.DarkerSurface)
                            .padding(
                                vertical = 4.dp,
                                horizontal = 8.dp
                            )
                    ) {
                        Text(
                            text = "10 Mengikuti",
                            color = LitecartesColor.Secondary,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(6.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(LitecartesColor.DarkerSurface)
                            .padding(
                                vertical = 4.dp,
                                horizontal = 8.dp
                            )
                    ) {
                        Text(
                            text = "10 Diikuti",
                            color = LitecartesColor.Secondary,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(
                        top = 20.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .background(LitecartesColor.DarkerSurface)
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatsIcon(
                        resId = R.drawable.diamond,
                        statName = "Reward",
                        stat = 250
                    )
                    StatsIcon(
                        resId = R.drawable.lightning,
                        statName = "XP",
                        stat = 150
                    )
                    StatsIcon(
                        resId = R.drawable.fire,
                        statName = "Streaks",
                        stat = 31
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(LitecartesColor.DarkerSurface)
                            .padding(
                                vertical = 10.dp,
                                horizontal = 20.dp
                            )
                    ) {
                        StatsIcon(
                            resId = R.drawable.nasional,
                            statName = "Peringkat\nNasional",
                            stat = 15
                        )
                    }
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(LitecartesColor.DarkerSurface)
                            .padding(
                                vertical = 10.dp,
                                horizontal = 20.dp
                            )
                    ) {
                        StatsIcon(
                            resId = R.drawable.kota,
                            statName = "Peringkat\nKota",
                            stat = 4
                        )
                    }
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(LitecartesColor.DarkerSurface)
                            .padding(
                                vertical = 10.dp,
                                horizontal = 20.dp
                            )
                    ) {
                        StatsIcon(
                            resId = R.drawable.sekolah,
                            statName = "Peringkat\nSekolah",
                            stat = 2
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
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
                LazyColumn {
                    items((1..2).toList()){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 12.dp,
                                        shape = RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .background(LitecartesColor.DarkerSurface)
                                    .padding(16.dp)
                                    .size(60.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.medal),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(60.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 12.dp,
                                        shape = RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .background(LitecartesColor.DarkerSurface)
                                    .padding(16.dp)
                                    .size(60.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.medal),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(60.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 12.dp,
                                        shape = RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .background(LitecartesColor.DarkerSurface)
                                    .padding(16.dp)
                                    .size(60.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.medal),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(60.dp)
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    }
                }
            }
            Navbar(
                navController = navController
            )
        }
    }
}

@Preview
@Composable
fun PreviewProfileScreen() {
    ProfileScreen(
        navController = rememberNavController()
    )
}