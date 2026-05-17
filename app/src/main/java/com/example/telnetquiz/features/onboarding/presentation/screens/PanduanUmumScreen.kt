package com.example.telnetquiz.features.onboarding.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.telnetquiz.R
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.data.local.OnboardingPreferenceManager
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import kotlinx.coroutines.launch

private data class GuidePage(
    val mascotResId: Int,
    val headline: String,
    val bullets: List<String>
)

private val guidePages = listOf(
    GuidePage(
        mascotResId = R.drawable.group_275,
        headline = "Apa itu TelNetQuiz?",
        bullets = listOf(
            "Game kuis belajar Media & Jaringan Telekomunikasi",
            "Catat progres belajarmu sendiri",
            "Adu skor di Papan Peringkat bareng teman lain"
        )
    ),
    GuidePage(
        mascotResId = R.drawable.chap1,
        headline = "Materi yang Bisa Kamu Jelajahi",
        bullets = listOf(
            "2 Bab: Prinsip TCP/IP dan Prinsip Sistem WLAN",
            "4 Level di tiap bab, dari mudah ke sulit",
            "Tiap level punya KKM, skor minimum untuk lulus"
        )
    ),
    GuidePage(
        mascotResId = R.drawable.group_276,
        headline = "Belajar → Main → Pencapaian",
        bullets = listOf(
            "Belajar: baca materi di ruang belajar",
            "Main: jawab kuis tiap level",
            "Kumpulkan 8 pencapaian dan naik peringkat"
        )
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PanduanUmumScreen(
    navController: NavController,
    onboardingPreferenceManager: OnboardingPreferenceManager,
    fromProfile: Boolean = false
) {
    val pagerState = rememberPagerState(pageCount = { guidePages.size })
    val scope = rememberCoroutineScope()

    val finish: () -> Unit = {
        if (fromProfile) {
            navController.popBackStack()
        } else {
            scope.launch {
                onboardingPreferenceManager.markPanduanUmumSeen()
                navController.navigate(Screen.QuickCheckScreen.route) {
                    popUpTo(Screen.PanduanUmumScreen.route) { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LitecartesColor.Surface)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = finish) {
                Text(
                    text = "Lewati",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = LitecartesColor.Secondary.copy(alpha = 0.7f)
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            GuidePageContent(guidePages[page])
        }

        PageIndicator(
            pageCount = guidePages.size,
            currentPage = pagerState.currentPage
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage > 0) {
                TextButton(onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                }) {
                    Text(
                        text = "Sebelum",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = LitecartesColor.Secondary
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            val isLast = pagerState.currentPage == guidePages.size - 1
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(LitecartesColor.Primary)
                    .clickable {
                        if (isLast) {
                            finish()
                        } else {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        }
                    }
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = when {
                        !isLast -> "Selanjutnya"
                        fromProfile -> "Selesai"
                        else -> "Mulai"
                    },
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun GuidePageContent(page: GuidePage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ) {
        Image(
            painter = painterResource(id = page.mascotResId),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Fit
        )
        Text(
            text = page.headline,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = LitecartesColor.Secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            page.bullets.forEach { bullet ->
                BulletPoint(text = bullet)
            }
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(LitecartesColor.Primary)
        )
        Text(
            text = text,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = LitecartesColor.Secondary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PageIndicator(pageCount: Int, currentPage: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(10.dp)
                    .width(if (isActive) 24.dp else 10.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) LitecartesColor.Primary
                        else LitecartesColor.Secondary.copy(alpha = 0.25f)
                    )
            )
        }
    }
}
