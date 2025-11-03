package com.example.litecartesnative.features.user.presentations.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.litecartesnative.features.user.presentations.components.AddFriendCard
import com.example.litecartesnative.features.user.presentations.components.Input
import com.example.litecartesnative.constants.usersDummy
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun FriendScreen(
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
                        start = 30.dp,
                        end = 30.dp,
                        bottom = 30.dp
                    )
                    .fillMaxWidth(),

            ) {
                Text(
                    text = "Tambah Teman",
                    fontFamily = nunitosFontFamily,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
                Text(
                    text = "Ayo berteman dengan pengguna lainnya",
                    fontFamily = nunitosFontFamily,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
                Spacer(
                    modifier = Modifier
                        .padding(5.dp)
                )
                Input(
                    value = "Maudy",
                    label = "Search",
                    onValueChange = {}
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            bottom = 20.dp
                        )
                ) {
                    Text(
                        text = "Hasil Pencarian",
                        fontFamily = nunitosFontFamily,
                        color = LitecartesColor.Secondary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Terdapat 10 hasil pencarian",
                        fontFamily = nunitosFontFamily,
                        color = LitecartesColor.Secondary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
                LazyColumn {
                    items(usersDummy) { user ->
                        AddFriendCard(user = user)
                        Spacer(
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }
                }
            }
            Navbar(navController = navController)
        }
    }
}

@Preview
@Composable
fun PreviewFriendScreen() {
    FriendScreen(navController = rememberNavController())
}