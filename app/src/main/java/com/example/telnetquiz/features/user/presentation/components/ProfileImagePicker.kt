package com.example.telnetquiz.features.user.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ProfileImagePicker(
    currentImageUrl: Any?,
    gender: Boolean?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (currentImageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(currentImageUrl)
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
                    painter = painterResource(
                        id = AvatarConstants.getDefaultAvatarResId(gender, "")
                    ),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(elevation = 20.dp, shape = CircleShape)
                        .clip(CircleShape)
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
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProfileImagePicker() {
    LitecartesNativeTheme {
        ProfileImagePicker(
            currentImageUrl = null,
            gender = null,
            onClick = {}
        )
    }
}
