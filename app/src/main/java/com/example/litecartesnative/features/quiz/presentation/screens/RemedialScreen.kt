package com.example.litecartesnative.features.quiz.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.litecartesnative.features.quiz.presentation.singletons.RemedialHolder
import com.example.litecartesnative.features.quiz.presentation.singletons.WrongQuizManager
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.LitecartesNativeTheme
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun RemedialScreen(
    navController: NavController,
    wrongCount: Int,
    totalCount: Int
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
                    .clip(RoundedCornerShape(12.dp))
                    .background(LitecartesColor.Primary)
                    .padding(vertical = 40.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SAYANG SEKALI!",
                    color = LitecartesColor.Surface,
                    fontSize = 28.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )
                Image(
                    painter = painterResource(id = R.drawable.result),
                    contentDescription = "remedial",
                    modifier = Modifier.size(250.dp)
                )
                Text(
                    text = "$wrongCount dari $totalCount jawabanmu kurang tepat",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pelajari materi berikut, lalu coba lagi!",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    text = "Pelajari Materi",
                    borderColor = LitecartesColor.Secondary,
                    color = LitecartesColor.Surface,
                    backgroundColor = LitecartesColor.Secondary,
                    textModifier = Modifier.padding(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    onClick = {
                        if (WrongQuizManager.queue.isNotEmpty()) {
                            val quizIndex = WrongQuizManager.queue.first()
                            WrongQuizManager.queue.removeFirst()
                            navController.navigate(
                                "${Screen.FeedbackScreen.route}/${quizIndex.chapterId}/levels/${quizIndex.level}/questions/${quizIndex.id}?materialId=${quizIndex.materialId}"
                            ) {
                                popUpTo(Screen.RemedialScreen.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRemedialScreen() {
    LitecartesNativeTheme {
        RemedialScreen(
            navController = rememberNavController(),
            wrongCount = 3,
            totalCount = 5
        )
    }
}
