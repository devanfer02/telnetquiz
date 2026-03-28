package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.AvatarImage
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
    Column(
        modifier = modifier
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .background(LitecartesColor.Primary)
            .padding(
                top = 35.dp,
                bottom = 20.dp
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(LitecartesColor.Secondary)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Sound Settings",
                    tint = LitecartesColor.Surface,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AvatarImage(
                imageUrl = profile?.image,
                localAvatarResId = localAvatarResId,
                gender = profile?.gender,
                nameSeed = profile?.fullname ?: "",
                shape = CircleShape,
                modifier = Modifier
                    .size(100.dp)
                    .shadow(elevation = 20.dp, shape = CircleShape)
            )
            IconButton(
                onClick = onEditProfile,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(LitecartesColor.Secondary)
                    .padding(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Icon",
                    tint = LitecartesColor.Surface,
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
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
                    fontSize = 14.sp
                )
            }
            else -> {
                Text(
                    text = profile?.fullname ?: "User",
                    color = Color.White,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                profile?.email?.let { email ->
                    Text(
                        text = email,
                        color = Color.White.copy(alpha = 0.8f),
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
                val infoItems = mutableListOf<String>()
                profile?.school?.let { infoItems.add(it.name) }
                profile?.grade?.let { infoItems.add("Kelas $it") }
                profile?.gender?.let {
                    infoItems.add(if (it) "Laki-Laki" else "Perempuan")
                }
                if (infoItems.isNotEmpty()) {
                    Text(
                        text = infoItems.joinToString(" · "),
                        color = Color.White.copy(alpha = 0.7f),
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                profile?.bio?.let { bio ->
                    if (bio.isNotBlank()) {
                        Text(
                            text = bio,
                            color = Color.White.copy(alpha = 0.8f),
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                top = 6.dp,
                                start = 24.dp,
                                end = 24.dp
                            )
                        )
                    }
                }
            }
        }
    }
}
