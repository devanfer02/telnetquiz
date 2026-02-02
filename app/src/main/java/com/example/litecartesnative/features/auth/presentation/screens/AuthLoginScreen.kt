package com.example.litecartesnative.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.constants.Screen
import com.example.litecartesnative.features.auth.presentation.components.AuthTopBar
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.features.auth.presentation.components.Input
import com.example.litecartesnative.features.auth.presentation.components.PasswordInput
import com.example.litecartesnative.features.auth.presentation.viewmodel.AuthViewModel
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun AuthLoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.AuthLoginScreen.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            AuthTopBar(
                painter = painterResource(id = R.drawable.login_screen)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.systemBarsPadding()

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LitecartesColor.Surface)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 40.dp,
                        vertical = 50.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Input(
                    value = email,
                    label = "Email",
                    onValueChange = {
                        email = it
                    }
                )
                Spacer(
                    modifier = Modifier.padding(2.dp)
                )
                PasswordInput(
                    value = password,
                    label = "Kata Sandi",
                    onValueChange = {
                        password = it
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 2.dp,
                            bottom = 20.dp,
                            end = 5.dp,
                            start = 5.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "Daftar",
                        textAlign = TextAlign.Start,
                        color = LitecartesColor.Secondary,
                        fontFamily = nunitosFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(
                                vertical = 5.dp
                            )
                            .clickable(
                                onClick = {
                                    navController
                                        .navigate(Screen.AuthRegisterScreen.route)
                                }
                            )
                    )
                    Text(
                        text = "Lupa Kata Sandi?",
                        textAlign = TextAlign.End,
                        color = LitecartesColor.Secondary,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(
                                vertical = 5.dp
                            )
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        text = if (state.isLoading) "" else "masuk".uppercase(),
                        borderColor = LitecartesColor.Secondary,
                        color = LitecartesColor.Surface,
                        backgroundColor = LitecartesColor.Secondary,
                        shadowEnabled = !state.isLoading,
                        shadowHeight = 55.dp,
                        shadowColor = LitecartesColor.DarkBrown,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (!state.isLoading && email.isNotBlank() && password.isNotBlank()) {
                                viewModel.login(email, password)
                            }
                        },
                        textModifier = Modifier.padding(8.dp)
                    )
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = LitecartesColor.Surface
                        )
                    }
                }
                Text(
                    text = "atau".uppercase(),
                    color = LitecartesColor.Secondary,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(
                        vertical = 20.dp
                    )
                )
                Spacer(
                    modifier = Modifier.padding()
                )
                Button(
                    text = "Masuk dengan Google",
                    borderColor = Color.White,
                    color = Color.Black,
                    backgroundColor = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    icon = painterResource(id = R.drawable.google_icon),
                    textModifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAuthLoginScreen() {
    AuthLoginScreen(
        navController = rememberNavController()
    )
}
