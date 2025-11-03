package com.example.litecartesnative.features.auth.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun AboutScreen(
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(50.dp))
            Image(
                painter = painterResource(id = R.drawable.chap1),
                contentDescription = "hai"
            )
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth()
                    .background(LitecartesColor.Primary)
                    .padding(
                        vertical = 10.dp,
                        horizontal = 20.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tentang Geomatruiz",
                    fontFamily = nunitosFontFamily,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    text = "Aplikasi Geomatruiz adalah aplikasi pembelajaran bagi Anak-anak yang sedang menduduki bangku sekolah dasar. Aplikasi Geomatruiz dibuat untuk membantu user nya untuk memahami elemen Geometri dan Pengukuran dari Mata Pelajaran Matematika pada tingkat Sekolah Dasar, tepatnya Fase A hingga Fase C. Pengembang aplikasi ini mengacu pada Capaian Pembelajaran mata pelajaran Matematika di Fase A hingga Fase C, yang kami pinjam dari referensi penerapan kurikulum pada Platform Merdeka Mengajar (PMM).",
                    color = Color.White,
                    fontFamily = nunitosFontFamily,
                    textAlign = TextAlign.Justify
                )
                Text(
                    text = "Aplikasi Geomatruiz menggunakan pendekatan pembelajaran adaptif, di mana setiap pengguna akan mendapatkan materi yang sesuai dengan tingkat kemampuannya. Melalui diagnosa pengetahuan berbentuk placement test di awal permainan, aplikasi akan menentukan level yang tepat untuk memulai pembelajaran.",
                    color = Color.White,
                    fontFamily = nunitosFontFamily,
                    textAlign = TextAlign.Justify
                )
            }
            Spacer(modifier = Modifier.padding(30.dp))
            Button(
                text = "Yuk Lanjutkan",
                borderColor = LitecartesColor.Secondary,
                color = LitecartesColor.Surface,
                backgroundColor = LitecartesColor.Secondary,
                onClick = {
                    navController.navigate(
                        route = Screen.HomeScreen.route
                    )
                },
                textModifier = Modifier.padding(8.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }

    }
}

@Preview
@Composable
fun PreviewAboutScreen() {
    AboutScreen(navController = rememberNavController())
}
