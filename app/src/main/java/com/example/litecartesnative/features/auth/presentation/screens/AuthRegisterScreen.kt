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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@Composable
fun AuthRegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var fullname by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    // Navigate to QuickCheckScreen on successful registration
    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            viewModel.clearSuccessMessage()
            navController.navigate(Screen.QuickCheckScren.route) {
                popUpTo(Screen.AuthRegisterScreen.route) { inclusive = true }
            }
        }
    }

    // Show error in snackbar
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            AuthTopBar(
                painter = painterResource(id = R.drawable.register_screen),
                contentAlignment = Alignment.TopEnd
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
                        horizontal = 35.dp,
                        vertical = 30.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Input(
                    value = fullname,
                    label = "Nama Lengkap",
                    onValueChange = {
                        fullname = it
                    },
                    leadingIcon = painterResource(id = R.drawable.ic_person)
                )
                Spacer(
                    modifier = Modifier.padding(4.dp)
                )
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
                            bottom = 12.dp
                        ),
                    horizontalArrangement = Arrangement.Start
                ){
                    TextButton(
                        onClick = {
                            navController.navigate(Screen.AuthLoginScreen.route)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = LitecartesColor.Secondary
                        )
                    ) {
                        Text(
                            text = "Sudah punya akun? Masuk",
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
                        text = if (state.isLoading) "" else "daftar".uppercase(),
                        borderColor = LitecartesColor.Secondary,
                        color = LitecartesColor.Surface,
                        backgroundColor = LitecartesColor.Secondary,
                        shadowEnabled = !state.isLoading,
                        shadowHeight = 55.dp,
                        shadowColor = LitecartesColor.DarkBrown,
                        modifier = Modifier.fillMaxWidth(),
                        textModifier = Modifier.padding(8.dp),
                        onClick = {
                            if (!state.isLoading && fullname.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                                viewModel.register(fullname, email, password)
                            }
                        }
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
    }
}

@Preview
@Composable
fun PreviewAuthRegisterScreen() {
    AuthRegisterScreen(
        navController = rememberNavController()
    )
}
