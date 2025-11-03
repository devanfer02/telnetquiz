package com.example.litecartesnative.features.auth.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.constants.chaptersData
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun FeedbackScren(
    navController: NavController,
    chapterId: Int,
    level: Int,
    id: Int
) {
    val material = chaptersData[chapterId].levels[level - 1][id - 1].material

    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(50.dp))
            Box {
                Image(
                    painter = painterResource(id = R.drawable.chap1),
                    contentDescription = "hai",
                    modifier = Modifier.padding(top = 10.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(top = 120.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                        .background(LitecartesColor.Primary)
                        .padding(
                            vertical = 10.dp,
                            horizontal = 20.dp
                        )
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${material?.title}",
                        fontFamily = nunitosFontFamily,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                    if (material?.image != null) {
                        Image(
                            painter = painterResource(id = material.image),
                            contentDescription = "",
                            modifier = Modifier
                                .size(200.dp)
                        )
                    }
                    Text(
                        text = "${material?.description}",
                        color = Color.White,
                        fontFamily = nunitosFontFamily,
                        textAlign = TextAlign.Justify
                    )
                }
            }
            Spacer(modifier = Modifier.padding(30.dp))
            Button(
                text = "Mari perbaiki kesalahanmu",
                borderColor = LitecartesColor.Secondary,
                color = LitecartesColor.Surface,
                backgroundColor = LitecartesColor.Secondary,
                onClick = {
                    navController.navigate(
                        route = "${Screen.QuestionScreen.route}/${chapterId}/levels/${level}/questions/${id}?toquestion=${true}"
                    )
                },
                textModifier = Modifier.padding(8.dp),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewFeedbackScren() {
    FeedbackScren(
        navController = rememberNavController(),
        chapterId = 0,
        level = 1,
        id = 1
    )
}
