package com.example.litecartesnative.features.pretest.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
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
import com.example.litecartesnative.features.pretest.presentation.components.PretestButton
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun QuickCheckScreen(
    navController: NavController
) {
    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Surface),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Column {
                Image(
                    painter = painterResource(id = R.drawable.quickcheck),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(300.dp)
                        ,
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 30.dp,
                                topEnd = 30.dp
                            )
                        )
                        .background(LitecartesColor.Primary)
                        .padding(vertical = 40.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tes Penempatan".uppercase(),
                        fontFamily = nunitosFontFamily,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Text(
                        text = "Sebelum kita mulai petualangan yang seru, jawab 4 pertanyaan singkat berikut untuk menentukan level pengetahuan kamu tentang bangun datar dan bangun ruang",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(
                                horizontal = 50.dp
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Box(
                        modifier = Modifier
                            .padding(
                                horizontal = 20.dp
                            )
                    ) {
                        Button(
                            text = "Yuk Lanjutkan",
                            borderColor = LitecartesColor.Secondary,
                            color = LitecartesColor.Surface,
                            backgroundColor = LitecartesColor.Secondary,
                            shadowEnabled = true,
                            shadowHeight = 55.dp,
                            shadowColor = LitecartesColor.DarkBrown,
                            modifier = Modifier.fillMaxWidth(),
                            textModifier = Modifier.padding(8.dp),
                            onClick = {
                                navController.navigate("${Screen.PretestScreen.route}/${1}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewQuickCheckScreen() {
    QuickCheckScreen(navController = rememberNavController())
}