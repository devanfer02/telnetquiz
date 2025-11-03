package com.example.litecartesnative.features.quiz.presentation.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.constants.chaptersData
import com.example.litecartesnative.constants.pretestsData
import com.example.litecartesnative.features.pretest.presentation.components.PretestButton
import com.example.litecartesnative.features.quiz.presentation.components.ProgressBar
import com.example.litecartesnative.features.quiz.presentation.components.OptionButton
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.litecartesnative.R
import com.example.litecartesnative.features.quiz.domain.model.QuizIndex
import com.example.litecartesnative.features.quiz.presentation.singletons.WrongQuizManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navController: NavController,
    chapterId: Int,
    level: Int,
    id: Int,
    toquestion: Boolean = false
) {
    val question = chaptersData[chapterId].levels[level - 1][id - 1]
    var selectedOption by remember {
        mutableStateOf("")
    }
    var showDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            ProgressBar(
                navController = navController,
                chapterId = chapterId,
                level = level,
                current = id,
                length = chaptersData[chapterId].levels[level - 1].size
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LitecartesColor.Surface),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier

                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 20.dp,
                            bottomStart = 20.dp
                        )
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        clip = false
                    )
                    .background(LitecartesColor.Primary)
                    .padding(
                        top = 18.dp
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp
                        )
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = question.title,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = nunitosFontFamily
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    if (question.imageId != null) {
                        Image(
                            painter = painterResource(id = question.imageId),
                            contentDescription = "",
                            modifier = Modifier
                                .size(250.dp)
                        )
                    }
                    Text(
                        text = question.description,
                        textAlign = TextAlign.Justify,
                        color = Color.White,
                        fontFamily = nunitosFontFamily,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(
                                vertical = 20.dp
                            )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LitecartesColor.Surface)
                    .padding(top = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 18.dp,
                        )
                ) {
                    Text(
                        text = question.question,
                        textAlign = TextAlign.Center,
                        color = LitecartesColor.Secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    LazyColumn {
                        itemsIndexed(question.options) { index, option ->
                            OptionButton(
                                text = option,
                                isActive = option == selectedOption,
                                onClick = {
                                    selectedOption = option
                                }
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    if(!selectedOption.isEmpty()) {
                        OutlinedButton(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            onClick = {
                                if (selectedOption != question.answer) {
                                    WrongQuizManager.queue.addLast(QuizIndex(
                                        chapterId,
                                        level,
                                        id
                                    ))
                                }
                                showDialog = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, LitecartesColor.DarkBrown),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LitecartesColor.DarkBrown
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
                        ) {
                            Text(
                                text = "Lanjutkan",
                                color = LitecartesColor.Surface
                            )
                        }
                    }

                    if (showDialog) {
                        ModalBottomSheet(
                            onDismissRequest = { showDialog = false },
                            containerColor = LitecartesColor.Surface
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (question.answer == selectedOption) {
                                        "Yey, kamu benar!"
                                    } else {
                                        "Yah, kamu salah"
                                    },
                                    fontFamily = nunitosFontFamily,
                                    fontSize = 20.sp,
                                    color = LitecartesColor.Secondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Image(
                                    painter = painterResource(
                                        id = if(question.answer == selectedOption) {
                                            R.drawable.chap1
                                        } else {
                                            R.drawable.tanya
                                        }
                                    ),
                                    contentDescription = "",
                                    modifier = Modifier.size(200.dp)
                                )

                                Column(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 20.dp
                                        )
                                ) {
                                    PretestButton(
                                        text = "Lanjut",
                                        backgroundColor = LitecartesColor.Secondary,
                                        textColor = LitecartesColor.Surface,
                                        onClick = {
                                            if (id != chaptersData[chapterId].levels[level - 1].size) {
                                                navController.navigate(
                                                    "${Screen.QuestionScreen.route}/$chapterId/levels/$level/questions/${id + 1}?toresult=${toquestion}"
                                                )
                                            } else {
                                                navController.navigate(
                                                    "${Screen.ResultScreen.route}/${chapterId}/levels/${level}"
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }

    }

}

@Preview
@Composable
fun PreviewQuestionScreen() {
    QuestionScreen(
        navController = rememberNavController(),
        chapterId = 0,
        level = 1,
        id = 1
    )
}