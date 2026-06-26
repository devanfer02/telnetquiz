package com.example.telnetquiz.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.telnetquiz.features.auth.presentation.components.AuthTopBar
import com.example.telnetquiz.features.auth.presentation.components.ErrorBottomSheet
import com.example.telnetquiz.components.Input
import com.example.telnetquiz.features.auth.presentation.components.PasswordInput
import com.example.telnetquiz.features.auth.presentation.components.GenderToggleButton
import com.example.telnetquiz.features.auth.presentation.components.SchoolPickerDialog
import com.example.telnetquiz.features.auth.presentation.viewmodel.AuthViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import android.util.Patterns
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
    value.isBlank() -> null
    value.length < 8 -> "Kata sandi minimal 8 karakter"
    !value.any { it.isDigit() } -> "Kata sandi harus mengandung angka"
    else -> null
}

private fun blankError(label: String, value: String, touched: Boolean): String? =
    if (touched && value.isBlank()) "$label tidak boleh kosong" else null

@Composable
fun AuthRegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showErrorSheet by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedSchoolId by remember { mutableIntStateOf(0) }
    var selectedSchoolName by remember { mutableStateOf("") }
    var showSchoolPicker by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf<Boolean?>(null) }
    var grade by remember { mutableStateOf("") }

    var fullnameTouched by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var gradeTouched by remember { mutableStateOf(false) }

    val fullnameErrorMessage = blankError("Nama lengkap", fullname, fullnameTouched)
    val emailErrorMessage = emailError(email, emailTouched)
    val passwordErrorMessage = passwordError(password, passwordTouched)
    val gradeErrorMessage = blankError("Kelas", grade, gradeTouched)

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            viewModel.clearSuccessMessage()
            navController.navigate(Screen.PanduanUmumScreen.route) {
                popUpTo(Screen.AuthRegisterScreen.route) { inclusive = true }
            }
        }
    }

    if (state.error != null && !showErrorSheet) {
        errorMessage = state.error ?: ""
        showErrorSheet = true
        viewModel.clearError()
    }

    Scaffold(
        topBar = {
            AuthTopBar(
                imageId = R.drawable.register_screen,
                contentAlignment = Alignment.TopEnd
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LitecartesColor.Surface)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
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
                        if (it.isNotEmpty()) fullnameTouched = true
                    },
                    leadingIcon = painterResource(id = R.drawable.ic_person),
                    isError = fullnameErrorMessage != null,
                    errorMessage = fullnameErrorMessage,
                    onTouched = { fullnameTouched = true }
                )
                Spacer(modifier = Modifier.padding(4.dp))
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
                Spacer(modifier = Modifier.padding(4.dp))
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
                Spacer(modifier = Modifier.padding(4.dp))

                Input(
                    value = grade,
                    label = "Kelas",
                    onValueChange = {
                        grade = it
                        if (it.isNotEmpty()) gradeTouched = true
                    },
                    leadingIcon = painterResource(id = R.drawable.ic_person),
                    isError = gradeErrorMessage != null,
                    errorMessage = gradeErrorMessage,
                    onTouched = { gradeTouched = true }
                )
                Spacer(modifier = Modifier.padding(4.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Sekolah",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = LitecartesColor.Secondary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                LitecartesColor.Secondary.copy(alpha = 0.3f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { showSchoolPicker = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedSchoolName.ifEmpty { "Pilih sekolah" },
                            color = if (selectedSchoolName.isEmpty())
                                LitecartesColor.Secondary.copy(alpha = 0.5f)
                            else LitecartesColor.Secondary,
                            fontFamily = nunitosFontFamily,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select school",
                            tint = LitecartesColor.Secondary.copy(alpha = 0.5f)
                        )
                    }
                }

                if (showSchoolPicker) {
                    SchoolPickerDialog(
                        onDismiss = { showSchoolPicker = false },
                        onSchoolSelected = { school ->
                            selectedSchoolId = school.id
                            selectedSchoolName = school.name
                        },
                        selectedSchoolId = if (selectedSchoolId > 0) selectedSchoolId else null,
                        viewModel = viewModel
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Jenis Kelamin",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = LitecartesColor.Secondary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GenderToggleButton(
                            text = "Laki-Laki",
                            icon = Icons.Default.Male,
                            selected = gender == true,
                            onClick = { gender = true },
                            modifier = Modifier.weight(1f)
                        )
                        GenderToggleButton(
                            text = "Perempuan",
                            icon = Icons.Default.Female,
                            selected = gender == false,
                            onClick = { gender = false },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

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
                LoadingButton(
                    text = "daftar".uppercase(),
                    isLoading = state.isLoading,
                    enabled = fullname.isNotBlank() && email.isNotBlank()
                        && password.isNotBlank() && selectedSchoolId > 0
                        && gender != null && grade.isNotBlank()
                        && fullnameErrorMessage == null
                        && emailErrorMessage == null && passwordErrorMessage == null
                        && gradeErrorMessage == null,
                    onClick = {
                        fullnameTouched = true
                        emailTouched = true
                        passwordTouched = true
                        gradeTouched = true
                        if (fullname.isNotBlank() && email.isNotBlank() &&
                            password.isNotBlank() && selectedSchoolId > 0 &&
                            gender != null && grade.isNotBlank() &&
                            emailErrorMessage == null && passwordErrorMessage == null) {
                            viewModel.register(
                                fullname, email, password,
                                selectedSchoolId, gender!!, grade
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))
        }

        if (showErrorSheet) {
            ErrorBottomSheet(
                title = "Pendaftaran Gagal",
                message = errorMessage,
                onDismiss = { showErrorSheet = false }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAuthRegisterScreen() {
    LitecartesNativeTheme {
        AuthRegisterScreen(
            navController = rememberNavController()
        )
    }
}
