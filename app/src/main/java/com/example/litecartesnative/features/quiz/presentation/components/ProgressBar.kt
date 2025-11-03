package com.example.litecartesnative.features.quiz.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.constants.pretestsData
import com.example.litecartesnative.ui.theme.LitecartesColor

@Composable
fun ProgressBar(
    navController: NavController,
    chapterId: Int,
    level: Int,
    current: Int,
    length: Int
) {
    var progress by remember {
        mutableStateOf(current.toFloat() / length.toFloat())
    }

    Column {
        Row(
            modifier = Modifier
                .background(LitecartesColor.Primary)
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.prev),
                contentDescription = "prev",
                modifier = Modifier
                    .weight(0.2f)
                    .size(30.dp)
                    .clickable {
                        if (current != 1) {
                            navController.navigate(
                                "${Screen.QuestionScreen.route}/$chapterId/levels/$level/questions/${current - 1}"
                            )
                        } else {
                            navController.navigate(
                                "${Screen.LevelScreen.route}/$chapterId"
                            )
                        }
                    }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)

                    .background(LitecartesColor.Primary)
                    .drawBehind {
                        drawRoundRect(
                            color = LitecartesColor.Surface,
                            cornerRadius = CornerRadius(40.dp.toPx()),
                            style = Stroke(
                                width = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                        )
                    }

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progress)
                        .background(LitecartesColor.Surface, RoundedCornerShape(40))
                )
            }
            Image(
                painter = painterResource(id = R.drawable.next),
                contentDescription = "next",
                modifier = Modifier
                    .weight(0.2f)
                    .size(30.dp)
                    .clickable {
                        if (current != length) {
                            navController.navigate(
                                "${Screen.QuestionScreen.route}/$chapterId/levels/$level/questions/${current + 1}"
                            )
                        } else {
                            navController.navigate(
                                "${Screen.ResultScreen.route}/${chapterId}"
                            )
                        }

                    }
            )
        }
        Divider(
            color = Color(0xFFFFD4B8).copy(alpha = 0.25f),
            thickness = 0.5.dp,

        )
    }
}

@Preview
@Composable
fun PreviewProgressBar() {
    ProgressBar(
        navController = rememberNavController(),
        chapterId = 0,
        level = 1,
        current = 1,
        length = 2
    )
}