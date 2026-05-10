package com.example.telnetquiz.features.auth.presentation.screens

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.R
import com.example.telnetquiz.components.LoadingButton
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.auth.presentation.components.ErrorBottomSheet
import com.example.telnetquiz.features.auth.presentation.components.ForgotPasswordDialog
import com.example.telnetquiz.components.Input
import com.example.telnetquiz.features.auth.presentation.components.PasswordInput
import com.example.telnetquiz.features.auth.presentation.viewmodel.AuthViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import android.app.Activity
import android.util.Patterns
import android.view.WindowManager
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

private fun emailError(value: String, touched: Boolean): String? = when {
    touched && value.isBlank() -> "Email tidak boleh kosong"
    value.isBlank() -> null
    !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Format email tidak valid"
    else -> null
}

private fun passwordError(value: String, touched: Boolean): String? = when {
    touched && value.isBlank() -> "Kata sandi tidak boleh kosong"
    else -> null
}

@Composable
fun AuthLoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as Activity).window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        onDispose {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }

    var showForgotPasswordDialog by remember {
        mutableStateOf(false)
    }

    var showErrorSheet by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val emailErrorMessage = emailError(email, emailTouched)
    val passwordErrorMessage = passwordError(password, passwordTouched)

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
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.start_screen)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(260.dp)
                )
                Text(
                    text = "Mari jelajahi Media dan\nJaringan Telekomunikasi!",
                    fontFamily = nunitosFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LitecartesColor.Secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 40.dp,
                        vertical = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Input(
                    value = email,
                    label = "Email",
                    onValueChange = {
                        email = it
                        if (it.isNotEmpty()) emailTouched = true
                    },
                    leadingIcon = painterResource(id = R.drawable.ic_email),
                    isError = emailErrorMessage != null,
                    errorMessage = emailErrorMessage,
                    onTouched = { emailTouched = true }
                )
                Spacer(
                    modifier = Modifier.padding(4.dp)
                )
                PasswordInput(
                    value = password,
                    label = "Kata Sandi",
                    onValueChange = {
                        password = it
                        if (it.isNotEmpty()) passwordTouched = true
                    },
                    leadingIcon = painterResource(id = R.drawable.ic_lock),
                    isError = passwordErrorMessage != null,
                    errorMessage = passwordErrorMessage,
                    onTouched = { passwordTouched = true }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 4.dp,
                            bottom = 2.dp
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
                LoadingButton(
                    text = "masuk".uppercase(),
                    isLoading = state.isLoading,
                    enabled = email.isNotBlank() && password.isNotBlank()
                        && emailErrorMessage == null && passwordErrorMessage == null,
                    onClick = {
                        emailTouched = true
                        passwordTouched = true
                        if (email.isNotBlank() && password.isNotBlank() &&
                            emailErrorMessage == null) {
                            viewModel.login(email, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
                    .background(LitecartesColor.Primary)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.login_screen)
                        .build(),
                    contentDescription = "Yuk Masuk"
                )
            }
        }

        if (showForgotPasswordDialog) {
            ForgotPasswordDialog(onDismiss = { showForgotPasswordDialog = false })
        }

        if (showErrorSheet) {
            ErrorBottomSheet(
                title = "Login Gagal",
                message = errorMessage,
                onDismiss = { showErrorSheet = false }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAuthLoginScreen() {
    LitecartesNativeTheme {
        AuthLoginScreen(
            navController = rememberNavController()
        )
    }
}
