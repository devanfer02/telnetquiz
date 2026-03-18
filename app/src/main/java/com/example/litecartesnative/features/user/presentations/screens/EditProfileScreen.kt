package com.example.litecartesnative.features.user.presentations.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.features.user.presentations.viewmodel.EditProfileViewModel
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.LitecartesNativeTheme
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
                .padding(
                    top = 30.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(25.dp)
                        .background(
                            color = LitecartesColor.DarkBrown,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "close",
                        tint = LitecartesColor.Surface,
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
                Text(
                    text = "Edit Profil".uppercase(),
                    color = LitecartesColor.DarkBrown,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier)
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Box(
                modifier = Modifier
                    .size(100.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.template_profile),
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(
                            elevation = 20.dp,
                            shape = CircleShape
                        )
                        .background(
                            LitecartesColor.Surface,
                            shape = CircleShape
                        )
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

                        Spacer(modifier = Modifier.padding(24.dp))

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
                                }
                            }
                        )
                    }
                }
            }
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
