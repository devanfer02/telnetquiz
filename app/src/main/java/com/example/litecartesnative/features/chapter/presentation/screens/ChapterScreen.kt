package com.example.litecartesnative.features.chapter.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.features.quiz.presentation.components.ProfileTopBar
import com.example.litecartesnative.components.Navbar
import com.example.litecartesnative.features.chapter.presentation.components.ChapterCard
import com.example.litecartesnative.features.chapter.presentation.components.ChapterCardFromApi
import com.example.litecartesnative.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.constants.chaptersData
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun ComingSoonCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(LitecartesColor.DarkerSurface.copy(alpha = 0.5f))
            .border(
                width = 2.dp,
                color = LitecartesColor.Secondary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(vertical = 24.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Coming soon",
                tint = LitecartesColor.Secondary.copy(alpha = 0.7f),
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "Konten baru sedang dalam proses,\nnantikan ya!",
                textAlign = TextAlign.Center,
                color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                fontFamily = nunitosFontFamily,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ChapterScreen(
    navController: NavController,
    viewModel: ChapterViewModel = hiltViewModel()
) {
    val state by viewModel.listState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadChapters()
    }

    // Redirect to pretest if user hasn't taken it
    LaunchedEffect(state.hasTakenPretest) {
        Log.d("CHAPTER", state.hasTakenPretest.toString())
        if (state.hasTakenPretest == false) {
            navController.navigate(Screen.QuickCheckScren.route) {
                popUpTo(Screen.HomeScreen.route) { inclusive = true }
            }
        }
    }
    Scaffold(
        topBar = {
            ProfileTopBar()
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(LitecartesColor.Surface)
            ) {
                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = LitecartesColor.Secondary)
                        }
                    }
                    state.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.error ?: "An error occurred",
                                color = LitecartesColor.Secondary
                            )
                        }
                    }
                    state.chapters.isNotEmpty() -> {
                        LazyColumn {
                            itemsIndexed(state.chapters) { index, chapter ->
                                Spacer(modifier = Modifier.padding(5.dp))
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 10.dp
                                        )
                                ) {
                                    ChapterCardFromApi(
                                        chapter = chapter,
                                        onClick = {
                                            navController.navigate(
                                                "${Screen.LevelScreen.route}/${chapter.id}"
                                            )
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.padding(5.dp))
                            }
                            // Add Coming Soon card if fewer than 3 chapters
                            if (state.chapters.size < 3) {
                                item {
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    ComingSoonCard()
                                    Spacer(modifier = Modifier.padding(10.dp))
                                }
                            }
                        }
                    }
                    else -> {
                        // Fallback to local data
                        LazyColumn {
                            itemsIndexed(chaptersData) { index, chapter ->
                                Spacer(modifier = Modifier.padding(5.dp))
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 10.dp
                                        )
                                ) {
                                    ChapterCard(
                                        chapter = chapter,
                                        onClick = {
                                            navController.navigate(
                                                "${Screen.LevelScreen.route}/${index}"
                                            )
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.padding(5.dp))
                            }
                            // Add Coming Soon card if fewer than 3 chapters
                            if (chaptersData.size < 3) {
                                item {
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    ComingSoonCard()
                                    Spacer(modifier = Modifier.padding(10.dp))
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

@Preview
@Composable
fun PreviewChapterScreen() {
    ChapterScreen(
        navController = rememberNavController()
    )
}
