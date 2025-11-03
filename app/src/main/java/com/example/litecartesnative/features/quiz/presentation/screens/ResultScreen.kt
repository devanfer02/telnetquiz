package com.example.litecartesnative.features.quiz.presentation.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun ResultScreen(
    navController: NavController,
    chapterId: Int
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Surface),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false
                    )
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .background(LitecartesColor.Primary)
                    .padding(
                        vertical = 40.dp,
                        horizontal = 20.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sempurna".uppercase(),
                    color = LitecartesColor.Surface,
                    fontSize = 28.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )
                Image(
                    painter = painterResource(id = R.drawable.result),
                    contentDescription = "uwaw",
                    modifier = Modifier
                        .size(300.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(LitecartesColor.Surface)
                            .padding(
                                horizontal = 30.dp,
                                vertical = 10.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_benar),
                            contentDescription = "right icon",
                            modifier = Modifier
                                .size(35.dp)
                        )
                        Text(
                            text = "5",
                            color = LitecartesColor.Primary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(24.dp)
                    )
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(LitecartesColor.Surface)
                            .padding(
                                horizontal = 30.dp,
                                vertical = 10.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_salah),
                            contentDescription = "right icon",
                            modifier = Modifier
                                .size(35.dp)
                        )
                        Text(
                            text = "0",
                            color = LitecartesColor.Primary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(LitecartesColor.Surface)
                        .padding(
                            vertical = 10.dp,
                            horizontal = 14.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Yeay kamu mendapatkan ",
                        color = LitecartesColor.Secondary,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = " +15 ",
                        color = LitecartesColor.Primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Image(
                        painter = painterResource(id = R.drawable.diamon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Button(
                    text = "Lanjutkan",
                    borderColor = LitecartesColor.Secondary,
                    color = LitecartesColor.Surface,
                    backgroundColor = LitecartesColor.Secondary,
                    textModifier = Modifier.padding(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 32.dp
                        ),
                    onClick = {
                        navController.navigate(
                            "${Screen.LevelScreen.route}/${chapterId}"
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewResultScreen() {
    ResultScreen(
        navController = rememberNavController(),
        chapterId = 0
    )
}