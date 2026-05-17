package com.example.telnetquiz.features.quiz.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.quiz.presentation.viewmodel.QuizViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun RemedialScreen(
    navController: NavController,
    wrongCount: Int,
    totalCount: Int,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val tutorialController = LocalTutorialController.current

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = LitecartesColor.Surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Surface)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.4f))

                Text(
                    text = "YUK BELAJAR LAGI!",
                    color = LitecartesColor.Secondary,
                    fontSize = 28.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(0.4f))

                Image(
                    painter = painterResource(id = R.drawable.mascot_wrong),
                    contentDescription = "Mascot menyemangati",
                    modifier = Modifier.size(220.dp)
                )

                Spacer(modifier = Modifier.weight(0.4f))

                Text(
                    text = "$wrongCount dari $totalCount soal perlu diperbaiki",
                    color = LitecartesColor.Secondary,
                    fontSize = 16.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tenang, kamu bisa pelajari materinya dulu sebelum mencoba lagi",
                    color = LitecartesColor.Secondary.copy(alpha = 0.75f),
                    fontSize = 13.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    text = "Ayo Pelajari!",
                    color = LitecartesColor.Surface,
                    backgroundColor = LitecartesColor.Primary,
                    borderColor = LitecartesColor.Primary,
                    textModifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .then(
                            if (tutorialController != null) Modifier.onGloballyPositioned {
                                tutorialController.registerTarget("remedial_cta_btn", it)
                            } else Modifier
                        ),
                    onClick = {
                        tutorialController?.notifyTargetClicked("remedial_cta_btn")
                        val quizIndex = viewModel.startRemedialReview()
                        if (quizIndex != null) {
                            navController.navigate(
                                "${Screen.StudyMaterialScreen.route}/${quizIndex.chapterId}/levels/${quizIndex.level}/questions/${quizIndex.id}?materialId=${quizIndex.materialId}"
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
