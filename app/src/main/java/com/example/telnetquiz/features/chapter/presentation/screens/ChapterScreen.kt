package com.example.telnetquiz.features.chapter.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.components.EmptyStateBox
import com.example.telnetquiz.components.ErrorRetryBox
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.chapter.presentation.components.ChapterCardFromApi
import com.example.telnetquiz.features.chapter.presentation.components.ChapterCardSkeleton
import com.example.telnetquiz.features.chapter.presentation.components.ComingSoonCard
import com.example.telnetquiz.features.chapter.presentation.viewmodel.ChapterViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ChapterScreen(
    navController: NavController,
    viewModel: ChapterViewModel = hiltViewModel()
) {
    val state by viewModel.listState.collectAsState()
    val tutorialController = LocalTutorialController.current

    LaunchedEffect(Unit) {
        viewModel.loadChapters()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LitecartesColor.Surface)
    ) {
        when {
            state.isLoading -> {
                LazyColumn(contentPadding = PaddingValues(bottom = 64.dp)) {
                    item {
                        Spacer(modifier = Modifier.padding(6.dp))
                        BabKamuHeader(modifier = Modifier.padding(horizontal = 14.dp))
                    }
                    items(3) {
                        Spacer(modifier = Modifier.padding(5.dp))
                        Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                            ChapterCardSkeleton()
                        }
                    }
                }
            }
            state.error != null -> {
                ErrorRetryBox(
                    message = state.error ?: "Terjadi kesalahan",
                    onRetry = { viewModel.loadChapters() }
                )
            }
            state.chapters.isNotEmpty() -> {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 64.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.padding(6.dp))
                        BabKamuHeader(modifier = Modifier.padding(horizontal = 14.dp))
                        Spacer(modifier = Modifier.padding(2.dp))
                    }
                    itemsIndexed(
                        state.chapters,
                        key = { _, chapter -> chapter.id }
                    ) { index, chapter ->
                        Spacer(modifier = Modifier.padding(5.dp))
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .then(
                                    if (index == 0 && tutorialController != null)
                                        Modifier.onGloballyPositioned {
                                            tutorialController.registerTarget("chapter_card_first", it)
                                        }
                                    else Modifier
                                )
                        ) {
                            ChapterCardFromApi(
                                chapter = chapter,
                                onClick = {
                                    if (index == 0) {
                                        tutorialController?.notifyTargetClicked("chapter_card_first")
                                    }
                                    navController.navigate(
                                        "${Screen.LevelScreen.route}/${chapter.id}"
                                    )
                                }
                            )
                        }
                    }
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
                EmptyStateBox(
                    title = "Belum ada materi tersedia",
                    subtitle = "Silakan coba lagi nanti"
                )
            }
        }
    }
}

@Composable
private fun BabKamuHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.MenuBook,
            contentDescription = null,
            tint = LitecartesColor.Primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "BAB KAMU",
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            color = LitecartesColor.Secondary
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChapterScreen() {
    LitecartesNativeTheme {
        ChapterScreen(
            navController = rememberNavController()
        )
    }
}
