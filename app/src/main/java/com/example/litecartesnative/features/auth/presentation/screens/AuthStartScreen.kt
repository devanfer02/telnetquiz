package com.example.litecartesnative.features.auth.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.components.StrokedText
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun AuthStartScreen(
    navController: NavController
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(LitecartesColor.Surface)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.start_screen),
                contentDescription = "start screen",
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
            )
            StrokedText(
                text = "selamat datang\n sobat geomatruiz".uppercase(),
                color = LitecartesColor.Secondary,
                strokeColor = Color.Black,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Mari kita jelajahi bangun datar dan bangun ruang!",
                fontFamily = nunitosFontFamily,
                color = LitecartesColor.Secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(
                        horizontal = 60.dp
                    )
            )
            Column(
                modifier = Modifier
                    .padding(
                        top = 30.dp
                    )
                    .widthIn(
                        max = 300.dp
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    text = "Daftar Sekarang".uppercase(),
                    borderColor = LitecartesColor.Secondary,
                    color = LitecartesColor.Surface ,
                    backgroundColor = LitecartesColor.Secondary,
                    shadowEnabled = true,
                    shadowHeight = 55.dp,
                    shadowColor = LitecartesColor.DarkBrown,
                    onClick = {
                        navController.navigate(
                            route = Screen.AuthRegisterScreen.route
                        )
                    },
                    textModifier = Modifier.padding(8.dp),
                    modifier = Modifier.fillMaxWidth(),

                )
                Button(
                    text = "Saya sudah punya akun".uppercase(),
                    borderColor = LitecartesColor.Secondary,
                    color = LitecartesColor.Secondary,
                    backgroundColor = LitecartesColor.Surface,
                    onClick = {
                        navController.navigate(
                            route = Screen.AuthLoginScreen.route
                        )
                    },
                    textModifier = Modifier.padding(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

    }
}

@Preview
@Composable
fun PreviewAuthStartScreen() {
    AuthStartScreen(
        navController = rememberNavController()
    )
}
