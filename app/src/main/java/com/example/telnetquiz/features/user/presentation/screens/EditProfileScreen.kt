package com.example.telnetquiz.features.user.presentation.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.features.user.presentation.components.AvatarPickerDialog
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
    val tutorialController = LocalTutorialController.current

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

    val isLoading = state.isLoading
    val dots by rememberLoadingDots(active = isLoading)

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(LitecartesColor.Surface)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                ProfileHeaderBand(
                    onClose = { navController.popBackStack() }
                )

                Box(modifier = Modifier.offset(y = 80.dp)) {
                    AvatarHero(
                        imageUrl = state.selectedImageUri
                            ?: state.profile?.image
                            ?: AvatarConstants.getAvatarResId(state.selectedAvatarIndex),
                        gender = state.profile?.gender,
                        onClick = { showAvatarPicker = true },
                        modifier = if (tutorialController != null) Modifier.onGloballyPositioned {
                            tutorialController.registerTarget("edit_avatar_picker", it)
                        } else Modifier
                    )
                }
            }

            Spacer(modifier = Modifier.height(90.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .clickable { showAvatarPicker = true }
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FileUpload,
                    contentDescription = null,
                    tint = LitecartesColor.Primary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "GANTI FOTO",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    color = LitecartesColor.Primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionLabel(
                icon = Icons.Default.Badge,
                text = "INFO PRIBADI"
            )

            Spacer(modifier = Modifier.height(8.dp))

            FieldsCard {
                FieldRow(
                    icon = Icons.Default.Person,
                    label = "NAMA LENGKAP",
                    value = if (isLoading) "Memuat nama$dots" else state.fullname,
                    placeholder = "Masukkan nama lengkap",
                    enabled = !isLoading,
                    onValueChange = { viewModel.onFullnameChanged(it) },
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = LitecartesColor.Primary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = if (tutorialController != null) Modifier.onGloballyPositioned {
                        tutorialController.registerTarget("edit_fullname", it)
                    } else Modifier
                )

                FieldDivider()

                FieldRow(
                    icon = Icons.Default.Email,
                    label = "EMAIL",
                    value = if (isLoading) "Memuat email$dots" else (state.profile?.email ?: ""),
                    placeholder = "",
                    enabled = false,
                    onValueChange = {},
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = LitecartesColor.Secondary.copy(alpha = 0.4f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )

                FieldDivider()

                BioFieldRow(
                    value = if (isLoading) "" else state.bio,
                    displayPlaceholder = if (isLoading) "Memuat bio$dots" else "Ceritakan tentang dirimu — opsional",
                    counterText = if (isLoading) "…/500" else "${state.bio.length}/500",
                    progress = (state.bio.length.coerceAtMost(500)) / 500f,
                    nearLimit = state.bio.length >= 400,
                    enabled = !isLoading,
                    onValueChange = { if (it.length <= 500) viewModel.onBioChanged(it) },
                    modifier = if (tutorialController != null) Modifier.onGloballyPositioned {
                        tutorialController.registerTarget("edit_bio", it)
                    } else Modifier
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, LitecartesColor.DarkBrown),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = LitecartesColor.Surface,
                        contentColor = LitecartesColor.DarkBrown
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = "Batal",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = LitecartesColor.DarkBrown
                    )
                }

                Button(
                    onClick = {
                        if (!state.isSaving && !isLoading) {
                            viewModel.saveProfile()
                            navController.navigate(Screen.ProfileScreen.route)
                        }
                    },
                    modifier = Modifier
                        .weight(1.6f)
                        .heightIn(min = 48.dp)
                        .then(
                            if (tutorialController != null) Modifier.onGloballyPositioned {
                                tutorialController.registerTarget("edit_save_btn", it)
                            } else Modifier
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LitecartesColor.Primary,
                        contentColor = LitecartesColor.Surface
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = when {
                            isLoading -> "Memuat$dots"
                            state.isSaving -> "Menyimpan..."
                            else -> "Simpan Perubahan"
                        },
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = LitecartesColor.Surface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileHeaderBand(
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = LitecartesColor.DarkerSurface,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        Text(
            text = "EDIT PROFIL",
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = LitecartesColor.DarkBrown,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 22.dp)
        )

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 14.dp)
                .size(34.dp)
                .background(
                    color = LitecartesColor.DarkBrown,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "close",
                tint = LitecartesColor.Surface,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun AvatarHero(
    imageUrl: Any?,
    gender: Boolean?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(132.dp)
            .shadow(elevation = 16.dp, shape = CircleShape, clip = false)
            .background(LitecartesColor.Surface, CircleShape)
            .border(width = 4.dp, color = LitecartesColor.Primary, shape = CircleShape)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        ProfileImagePicker(
            currentImageUrl = imageUrl,
            gender = gender,
            onClick = onClick
        )
    }
}

@Composable
private fun SectionLabel(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = LitecartesColor.Primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = LitecartesColor.Primary
        )
    }
}

@Composable
private fun FieldsCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp), clip = false)
            .background(LitecartesColor.Surface, RoundedCornerShape(20.dp))
            .padding(vertical = 4.dp)
    ) {
        content()
    }
}

@Composable
private fun FieldDivider() {
    Divider(
        color = LitecartesColor.Secondary.copy(alpha = 0.12f),
        thickness = 1.dp,
        modifier = Modifier.padding(start = 64.dp, end = 16.dp)
    )
}

@Composable
private fun FieldIconBadge(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = LitecartesColor.DarkerSurface.copy(alpha = 0.6f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = LitecartesColor.Secondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun FieldRow(
    icon: ImageVector,
    label: String,
    value: String,
    placeholder: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    trailing: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FieldIconBadge(icon = icon)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                color = LitecartesColor.Primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (enabled) LitecartesColor.DarkBrown else LitecartesColor.Secondary.copy(alpha = 0.6f)
                ),
                modifier = modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            color = LitecartesColor.Secondary.copy(alpha = 0.45f)
                        )
                    }
                    inner()
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        trailing()
    }
}

@Composable
private fun BioFieldRow(
    value: String,
    displayPlaceholder: String,
    counterText: String,
    progress: Float,
    nearLimit: Boolean,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val counterColor = if (nearLimit) LitecartesColor.Tertiary else LitecartesColor.Primary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FieldIconBadge(icon = Icons.Default.EditNote)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "BIO",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    color = LitecartesColor.Primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = displayPlaceholder,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = LitecartesColor.Secondary.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = counterColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = counterText,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    color = counterColor
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 90.dp)
                .background(
                    color = LitecartesColor.DarkerSurface.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = LitecartesColor.Secondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                textStyle = TextStyle(
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = if (enabled) LitecartesColor.DarkBrown else LitecartesColor.Secondary.copy(alpha = 0.6f)
                ),
                modifier = modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Tulis bio singkat...",
                            fontFamily = nunitosFontFamily,
                            fontSize = 14.sp,
                            color = LitecartesColor.Secondary.copy(alpha = 0.4f)
                        )
                    }
                    inner()
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = progress.coerceIn(0f, 1f),
            color = counterColor,
            trackColor = LitecartesColor.Secondary.copy(alpha = 0.12f),
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
        )
    }
}

@Composable
private fun rememberLoadingDots(active: Boolean): State<String> {
    val transition = rememberInfiniteTransition(label = "dots")
    val tick by transition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tick"
    )
    return remember(active) {
        derivedStateOf {
            if (active) ".".repeat(tick.toInt().coerceIn(0, 3)) else ""
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
