package com.example.litecartesnative.features.auth.presentation.screens

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthLoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val errorSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var showForgotPasswordDialog by remember {
        mutableStateOf(false)
    }

    var showErrorSheet by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.AuthLoginScreen.route) { inclusive = true }
            }
        }
    }

    // Show error sheet immediately when error changes
    if (state.error != null && !showErrorSheet) {
        errorMessage = state.error ?: ""
        showErrorSheet = true
        viewModel.clearError()
    }

    Scaffold(
        topBar = {
            AuthTopBar(
                painter = painterResource(id = R.drawable.login_screen)
            )
        },
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
                        vertical = 30.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Input(
                    value = email,
                    label = "Email",
                    onValueChange = {
                        email = it
                    },
                    leadingIcon = painterResource(id = R.drawable.ic_email)
                )
                Spacer(
                    modifier = Modifier.padding(4.dp)
                )
                PasswordInput(
                    value = password,
                    label = "Kata Sandi",
                    onValueChange = {
                        password = it
                    },
                    leadingIcon = painterResource(id = R.drawable.ic_lock)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 4.dp,
                            bottom = 16.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    TextButton(
                        onClick = {
                            navController.navigate(
                                route = Screen.AuthRegisterScreen.route
                            )
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = LitecartesColor.Secondary
                        )
                    ) {
                        Text(
                            text = "Daftar",
                            fontFamily = nunitosFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                    TextButton(
                        onClick = { showForgotPasswordDialog = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = LitecartesColor.Secondary
                        )
                    ) {
                        Text(
                            text = "Lupa Kata Sandi?",
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    }
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
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        if (showForgotPasswordDialog) {
            AlertDialog(
                onDismissRequest = { showForgotPasswordDialog = false },
                title = {
                    Text(
                        text = "Lupa Kata Sandi?",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = LitecartesColor.Secondary
                    )
                },
                text = {
                    Text(
                        text = "Kontak tim admin apabila lupa password",
                        fontFamily = nunitosFontFamily,
                        color = LitecartesColor.DarkBrown
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { showForgotPasswordDialog = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = LitecartesColor.Secondary
                        )
                    ) {
                        Text(
                            text = "OK",
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                containerColor = LitecartesColor.Surface
            )
        }

        if (showErrorSheet) {
            ModalBottomSheet(
                onDismissRequest = { showErrorSheet = false },
                sheetState = errorSheetState,
                containerColor = LitecartesColor.Surface,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login Gagal",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = LitecartesColor.Secondary
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = if (errorMessage.contains("password")) {
                            "Email atau kata sandi tidak sesuai"
                        } else {
                            "Email yang digunakan tidak valid"
                        },
                        fontFamily = nunitosFontFamily,
                        fontSize = 14.sp,
                        color = LitecartesColor.DarkBrown,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    Button(
                        text = "OK",
                        borderColor = LitecartesColor.Secondary,
                        color = LitecartesColor.Surface,
                        backgroundColor = LitecartesColor.Secondary,
                        shadowEnabled = true,
                        shadowHeight = 45.dp,
                        shadowColor = LitecartesColor.DarkBrown,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showErrorSheet = false },
                        textModifier = Modifier.padding(8.dp)
                    )
                }
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
