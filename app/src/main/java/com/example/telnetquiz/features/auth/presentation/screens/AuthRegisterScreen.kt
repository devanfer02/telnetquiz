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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
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
import com.example.telnetquiz.components.Input
import com.example.telnetquiz.features.auth.presentation.components.PasswordInput
import com.example.telnetquiz.features.auth.presentation.components.GenderToggleButton
import com.example.telnetquiz.features.auth.presentation.components.SchoolPickerDialog
import com.example.telnetquiz.features.auth.presentation.viewmodel.AuthViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import android.app.Activity
import android.view.WindowManager
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun AuthRegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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

    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedSchoolId by remember { mutableIntStateOf(0) }
    var selectedSchoolName by remember { mutableStateOf("") }
    var showSchoolPicker by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf<Boolean?>(null) }
    var grade by remember { mutableStateOf("") }

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
                    onValueChange = { fullname = it },
                    leadingIcon = painterResource(id = R.drawable.ic_person)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Input(
                    value = email,
                    label = "Email",
                    onValueChange = { email = it },
                    leadingIcon = painterResource(id = R.drawable.ic_email)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                PasswordInput(
                    value = password,
                    label = "Kata Sandi",
                    onValueChange = { password = it },
                    leadingIcon = painterResource(id = R.drawable.ic_lock)
                )
                Spacer(modifier = Modifier.padding(4.dp))

                // School picker
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

                // Gender toggle
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
                Spacer(modifier = Modifier.padding(4.dp))

                // Grade input
                Input(
                    value = grade,
                    label = "Kelas",
                    onValueChange = { grade = it },
                    leadingIcon = painterResource(id = R.drawable.ic_person)
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
                LoadingButton(
                    text = "daftar".uppercase(),
                    isLoading = state.isLoading,
                    enabled = fullname.isNotBlank() && email.isNotBlank()
                        && password.isNotBlank() && selectedSchoolId > 0
                        && gender != null && grade.isNotBlank(),
                    onClick = {
                        viewModel.register(
                            fullname, email, password,
                            selectedSchoolId, gender!!, grade
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))
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
