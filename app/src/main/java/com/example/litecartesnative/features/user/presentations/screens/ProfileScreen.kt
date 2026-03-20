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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Navbar
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.features.user.presentations.viewmodel.AchievementViewModel
import com.example.litecartesnative.features.user.presentations.viewmodel.ProfileViewModel
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.LitecartesNativeTheme
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    achievementViewModel: AchievementViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val achievementState by achievementViewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        achievementViewModel.loadAchievements()
    }

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
                        .size(100.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    if (state.profile?.image != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(state.profile?.image)
                                .crossfade(true)
                                .build(),
                            contentDescription = "profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .shadow(
                                    elevation = 20.dp,
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                                .background(
                                    LitecartesColor.Surface,
                                    shape = CircleShape
                                )
                        )
                    } else {
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
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.EditProfileScreen.route)
                        },
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
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    state.error != null -> {
                        Text(
                            text = state.error ?: "Error loading profile",
                            color = Color.White,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    else -> {
                        Text(
                            text = state.profile?.fullname ?: "User",
                            color = Color.White,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                        state.profile?.email?.let { email ->
                            Text(
                                text = email,
                                color = Color.White.copy(alpha = 0.8f),
                                fontFamily = nunitosFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                        // School, Grade, Gender info
                        val infoItems = mutableListOf<String>()
                        state.profile?.school?.let { infoItems.add(it.name) }
                        state.profile?.grade?.let { infoItems.add("Kelas $it") }
                        state.profile?.gender?.let {
                            infoItems.add(if (it) "Laki-Laki" else "Perempuan")
                        }
                        if (infoItems.isNotEmpty()) {
                            Text(
                                text = infoItems.joinToString(" · "),
                                color = Color.White.copy(alpha = 0.7f),
                                fontFamily = nunitosFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        // Bio
                        state.profile?.bio?.let { bio ->
                            if (bio.isNotBlank()) {
                                Text(
                                    text = bio,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontFamily = nunitosFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(
                                        top = 6.dp,
                                        start = 24.dp,
                                        end = 24.dp
                                    )
                                )
                            }
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            // Stats row
            state.profile?.stats?.let { stats ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = "Total Skor",
                        value = "${stats.totalScore}",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Level Selesai",
                        value = "${stats.levelsCompleted}",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Bab Selesai",
                        value = "${stats.chaptersCompleted}",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .weight(1f)
            ) {
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

                when {
                    achievementState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = LitecartesColor.Secondary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    achievementState.error != null -> {
                        Text(
                            text = achievementState.error ?: "Gagal memuat pencapaian",
                            color = LitecartesColor.Secondary,
                            fontSize = 14.sp
                        )
                    }
                    achievementState.achievements.isEmpty() -> {
                        Text(
                            text = "Belum ada pencapaian. Selesaikan quiz untuk mendapatkan pencapaian!",
                            color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                            fontFamily = nunitosFontFamily,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else -> {
                        LazyColumn {
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
            Navbar(
                navController = navController
            )
        }
    }
}

@Composable
private fun AchievementCard(
    title: String,
    description: String,
    unlocked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (unlocked) LitecartesColor.DarkerSurface
                else LitecartesColor.DarkerSurface.copy(alpha = 0.5f)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    if (unlocked) LitecartesColor.Primary.copy(alpha = 0.2f)
                    else Color.Gray.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (unlocked) {
                Image(
                    painter = painterResource(id = R.drawable.medal),
                    contentDescription = title,
                    modifier = Modifier.size(35.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (unlocked) LitecartesColor.Secondary
                else LitecartesColor.Secondary.copy(alpha = 0.5f)
            )
            Text(
                text = description,
                fontFamily = nunitosFontFamily,
                fontSize = 12.sp,
                color = if (unlocked) LitecartesColor.Secondary.copy(alpha = 0.7f)
                else LitecartesColor.Secondary.copy(alpha = 0.4f)
            )
        }
        if (unlocked) {
            Text(
                text = "✓",
                color = LitecartesColor.Primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAchievementCardUnlocked() {
    LitecartesNativeTheme {
        AchievementCard(
            title = "Penjelajah Geometri",
            description = "Selesaikan 5 level pertama",
            unlocked = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAchievementCardLocked() {
    LitecartesNativeTheme {
        AchievementCard(
            title = "Master Bangun Datar",
            description = "Selesaikan semua level di Bab 1",
            unlocked = false
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LitecartesColor.DarkerSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = LitecartesColor.Primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
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
