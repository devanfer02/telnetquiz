package com.example.telnetquiz.features.user.presentations.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.telnetquiz.R
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.features.user.presentations.viewmodel.EditProfileViewModel
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
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onImageSelected(uri)
    }

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

            // Profile image with camera overlay
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.BottomEnd
            ) {
                val imageModel = state.selectedImageUri
                    ?: state.profile?.image

                if (imageModel != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageModel)
                            .crossfade(true)
                            .build(),
                        contentDescription = "profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(
                                elevation = 20.dp,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .background(
                                LitecartesColor.Surface,
                                shape = CircleShape
                            )
                    )
                } else {
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
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(LitecartesColor.Secondary)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change photo",
                        tint = LitecartesColor.Surface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = "Maks 2MB",
                fontFamily = nunitosFontFamily,
                fontSize = 11.sp,
                color = LitecartesColor.Secondary.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 4.dp)
            )

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
