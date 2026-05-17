package com.example.telnetquiz.features.user.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.AvatarImage
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.data.remote.dto.UserProfileDto
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ProfileHeaderSection(
    profile: UserProfileDto?,
    isLoading: Boolean,
    error: String?,
    localAvatarResId: Int? = null,
    onSettingsClick: () -> Unit,
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tutorialController = LocalTutorialController.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(LitecartesColor.Primary)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-30).dp)
                .size(140.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f))
        )

        IconButton(
            onClick = {
                tutorialController?.notifyTargetClicked("profile_settings_btn")
                onSettingsClick()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 12.dp)
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f))
                .then(
                    if (tutorialController != null) Modifier.onGloballyPositioned {
                        tutorialController.registerTarget("profile_settings_btn", it)
                    } else Modifier
                )
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Pengaturan",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 16.dp, start = 14.dp, end = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(96.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(elevation = 10.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(width = 3.dp, color = Color.White, shape = CircleShape)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AvatarImage(
                        imageUrl = profile?.image,
                        localAvatarResId = localAvatarResId,
                        gender = profile?.gender,
                        nameSeed = profile?.fullname ?: "",
                        shape = CircleShape,
                        modifier = Modifier.size(88.dp)
                    )
                }
                IconButton(
                    onClick = onEditProfile,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(LitecartesColor.Secondary)
                        .border(width = 2.dp, color = Color.White, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit profil",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            when {
                isLoading -> {
                    ProfileHeaderSkeleton()
                }
                error != null -> {
                    Text(
                        text = error,
                        color = Color.White,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
                else -> {
                    Text(
                        text = profile?.fullname ?: "User",
                        color = Color.White,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    profile?.email?.let { email ->
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = email,
                                color = Color.White.copy(alpha = 0.85f),
                                fontFamily = nunitosFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    val infoItems = mutableListOf<String>()
                    profile?.school?.let { infoItems.add(it.name) }
                    profile?.grade?.let { infoItems.add("Kelas $it") }
                    profile?.gender?.let {
                        infoItems.add(if (it) "Laki-Laki" else "Perempuan")
                    }
                    if (infoItems.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(LitecartesColor.Secondary.copy(alpha = 0.6f))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = infoItems.joinToString(" • "),
                                color = Color.White,
                                fontFamily = nunitosFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
