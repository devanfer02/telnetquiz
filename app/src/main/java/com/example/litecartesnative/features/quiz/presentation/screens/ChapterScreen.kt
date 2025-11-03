package com.example.litecartesnative.features.quiz.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.features.quiz.presentation.components.ProfileTopBar
import com.example.litecartesnative.components.Navbar
import com.example.litecartesnative.features.quiz.presentation.components.ChapterCard
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.constants.chaptersData
import com.example.litecartesnative.ui.theme.LitecartesColor

@Composable
fun ChapterScreen(
    navController: NavController
) {
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