package com.example.litecartesnative.features.quiz.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Navbar
import com.example.litecartesnative.features.quiz.presentation.components.LevelButton
import com.example.litecartesnative.features.quiz.presentation.components.ProfileTopBar
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.constants.chaptersData
import com.example.litecartesnative.constants.levelsData
import com.example.litecartesnative.features.quiz.presentation.singletons.MarkAsDoneManager
import com.example.litecartesnative.ui.theme.LitecartesColor

@Composable
fun LevelScreen(
    navController: NavController,
    chapterId: Int
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ProfileTopBar(
                backgroundColor = LitecartesColor.DarkerSurface
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .verticalScroll(
                        scrollState
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.level_background),
                    contentDescription = "bg",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
                levelsData.forEachIndexed { index, level ->
                    if (index >= chaptersData[chapterId].levels.size) {
                        return@forEachIndexed
                    }

                    Box(
                        modifier = Modifier
                            .offset(
                                x = level.x,
                                y = level.y
                            )
                    ) {
                        val level = index + 1
                        LevelButton(
                            level = level,
                            onClick = {
                                navController.navigate(
                                    "${Screen.QuestionScreen.route}/$chapterId/levels/$level/questions/1"
                                )
                            },
                            done =MarkAsDoneManager.levels[chapterId][level-1]
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
fun PreviewLevelScreen() {
    LevelScreen(
        navController = rememberNavController(),
        chapterId = 0
    )
}