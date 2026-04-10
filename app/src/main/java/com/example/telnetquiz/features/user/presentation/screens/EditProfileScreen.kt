package com.example.telnetquiz.features.user.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.user.presentation.components.AvatarPickerDialog
import com.example.telnetquiz.features.user.presentation.components.CloseHeader
import com.example.telnetquiz.features.user.presentation.components.ProfileImagePicker
import com.example.telnetquiz.features.user.presentation.viewmodel.EditProfileViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showAvatarPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            snackbarHostState.showSnackbar("Profil berhasil disimpan")
            viewModel.clearSaveSuccess()
            navController.popBackStack()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = Modifier
            .systemBarsPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(LitecartesColor.Surface)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = 30.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CloseHeader(
                title = "Edit Profil".uppercase(),
                onClose = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.padding(16.dp))

            ProfileImagePicker(
                currentImageUrl = state.selectedImageUri
                    ?: state.profile?.image
                    ?: AvatarConstants.getAvatarResId(state.selectedAvatarIndex),
                gender = state.profile?.gender,
                onClick = { showAvatarPicker = true }
            )

            if (showAvatarPicker) {
                AvatarPickerDialog(
                    selectedIndex = state.selectedAvatarIndex,
                    onAvatarSelected = { index ->
                        viewModel.onAvatarSelected(index)
                        showAvatarPicker = false
                    },
                    onDismiss = { showAvatarPicker = false }
                )
            }

            Spacer(modifier = Modifier.padding(24.dp))

            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = LitecartesColor.Secondary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = "Nama Lengkap",
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = LitecartesColor.Secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = state.fullname,
                            onValueChange = { viewModel.onFullnameChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LitecartesColor.Primary,
                                unfocusedBorderColor = LitecartesColor.Secondary.copy(alpha = 0.3f),
                                focusedTextColor = LitecartesColor.Secondary,
                                unfocusedTextColor = LitecartesColor.Secondary
                            ),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Masukkan nama lengkap",
                                    color = LitecartesColor.Secondary.copy(alpha = 0.5f)
                                )
                            }
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        Text(
                            text = "Email",
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = LitecartesColor.Secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = state.profile?.email ?: "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = LitecartesColor.Secondary.copy(alpha = 0.2f),
                                disabledTextColor = LitecartesColor.Secondary.copy(alpha = 0.5f)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        Text(
                            text = "Bio",
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = LitecartesColor.Secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = state.bio,
                            onValueChange = { if (it.length <= 500) viewModel.onBioChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LitecartesColor.Primary,
                                unfocusedBorderColor = LitecartesColor.Secondary.copy(alpha = 0.3f),
                                focusedTextColor = LitecartesColor.Secondary,
                                unfocusedTextColor = LitecartesColor.Secondary
                            ),
                            minLines = 3,
                            maxLines = 5,
                            placeholder = {
                                Text(
                                    text = "Tulis bio singkat...",
                                    color = LitecartesColor.Secondary.copy(alpha = 0.5f)
                                )
                            }
                        )
                        Text(
                            text = "${state.bio.length}/500",
                            fontFamily = nunitosFontFamily,
                            fontSize = 11.sp,
                            color = LitecartesColor.Secondary.copy(alpha = 0.5f),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.padding(16.dp))

                        Button(
                            text = if (state.isSaving) "Menyimpan..." else "Simpan",
                            borderColor = LitecartesColor.Secondary,
                            color = LitecartesColor.Surface,
                            backgroundColor = LitecartesColor.Secondary,
                            textModifier = Modifier.padding(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                if (!state.isSaving) {
                                    viewModel.saveProfile()
                                    navController.navigate(
                                        Screen.ProfileScreen.route,
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEditProfileScreen() {
    LitecartesNativeTheme {
        EditProfileScreen(
            navController = rememberNavController()
        )
    }
}
