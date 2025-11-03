package com.example.litecartesnative.features.pretest.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.features.pretest.domain.model.Pretest
import com.example.litecartesnative.features.pretest.presentation.components.PretestButton
import com.example.litecartesnative.features.pretest.presentation.components.ProgressBar
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.constants.pretestsData
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PretestScreen(
    navController: NavController,
    pretestId: Int
) {
    val pretest = pretestsData[pretestId - 1]
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
                current = pretestId
            )
        },
        modifier = Modifier
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(LitecartesColor.Surface)
                .padding(
                    horizontal = 12.dp
                )
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding(5.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(LitecartesColor.Primary)
                    .padding(
                        20.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (pretest.imageId != null) {
                    Image(
                        painter = painterResource(id = pretest.imageId),
                        contentDescription = "",
                        modifier = Modifier
                            .size(250.dp)
                    )
                }
                Text(
                    text = pretest.question,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.padding(14.dp))
            LazyColumn {
                items(pretest.options) { option ->
                    PretestButton(
                        text = option,
                        onClick = {
                            selectedOption = option
                        },
                        isActive = selectedOption == option
                    )
                }
            }
            Spacer(modifier = Modifier.padding(20.dp))
            PretestButton(
                text = "Lanjutkan",
                backgroundColor = LitecartesColor.Secondary,
                textColor = LitecartesColor.Surface,
                onClick = {
                    val route = if (pretestId >= pretestsData.size) {
                        "${Screen.AboutScreen.route}"
                    } else {
                        "${Screen.PretestScreen.route}/${pretestId+1}"
                    }

                    navController.navigate(
                        route
                    )
                }
            )


        }
    }
}

@Preview
@Composable
fun PreviewPretestScreen() {
    PretestScreen(
        navController = rememberNavController(),
        pretestId = 1
    )
}