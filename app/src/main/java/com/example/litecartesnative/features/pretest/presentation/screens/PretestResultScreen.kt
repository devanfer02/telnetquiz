package com.example.litecartesnative.features.pretest.presentation.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.features.pretest.presentation.singletons.PretestResultHolder
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun PretestResultScreen(
    navController: NavController
) {
    val result = PretestResultHolder.lastResult
    val correctCount = result?.correctAnswers ?: 0
    val wrongCount = result?.incorrectAnswers ?: 0
    val scorePercentage = result?.scorePercentage ?: 0.0
    val weaknesses = result?.chapterWeaknesses ?: emptyList()

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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = false
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .background(LitecartesColor.Primary)
                        .padding(
                            vertical = 40.dp,
                            horizontal = 20.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HASIL PRETEST",
                        color = LitecartesColor.Surface,
                        fontSize = 28.sp,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Image(
                        painter = painterResource(id = R.drawable.result),
                        contentDescription = "result",
                        modifier = Modifier.size(300.dp)
                    )
                    Text(
                        text = "${scorePercentage.toInt()}%",
                        color = Color.White,
                        fontSize = 48.sp,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(LitecartesColor.Surface)
                                .padding(horizontal = 30.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_benar),
                                contentDescription = "correct icon",
                                modifier = Modifier.size(35.dp)
                            )
                            Text(
                                text = "$correctCount",
                                color = LitecartesColor.Primary,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.padding(24.dp))
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(LitecartesColor.Surface)
                                .padding(horizontal = 30.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_salah),
                                contentDescription = "wrong icon",
                                modifier = Modifier.size(35.dp)
                            )
                            Text(
                                text = "$wrongCount",
                                color = LitecartesColor.Primary,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (weaknesses.isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(12.dp))
                        Text(
                            text = "Bab yang perlu dipelajari lagi:",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.padding(6.dp))
                        weaknesses.forEach { weakness ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LitecartesColor.Surface.copy(alpha = 0.15f))
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = weakness.chapterTitle,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontFamily = nunitosFontFamily,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${weakness.wrongCount}/${weakness.totalQuestions} salah",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    fontFamily = nunitosFontFamily
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(
                        text = "Lanjutkan",
                        borderColor = LitecartesColor.Secondary,
                        color = LitecartesColor.Surface,
                        backgroundColor = LitecartesColor.Secondary,
                        textModifier = Modifier.padding(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        onClick = {
                            PretestResultHolder.clear()
                            navController.navigate(Screen.HomeScreen.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
