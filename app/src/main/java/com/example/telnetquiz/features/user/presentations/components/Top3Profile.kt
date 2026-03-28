package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.AvatarImage
import com.example.telnetquiz.components.SkeletonBox
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun Top3Profile(
    user: User,
    avatarResIdOverride: Int? = null
) {
    AvatarImage(
        localAvatarResId = avatarResIdOverride,
        gender = user.gender,
        nameSeed = user.fullname,
        shape = CircleShape,
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .shadow(elevation = 12.dp, shape = CircleShape, clip = false)
    )
    Text(
        text = user.fullname,
        fontFamily = nunitosFontFamily,
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
    Text(
        text = user.handle,
        fontFamily = nunitosFontFamily,
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.padding(1.dp))
    Text(
        text = "${user.exp} XP",
        fontFamily = nunitosFontFamily,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun Top3ProfileSkeleton() {
    SkeletonBox(
        height = 100.dp,
        width = 100.dp,
        cornerRadius = 50.dp,
        onPrimary = true,
        modifier = Modifier
            .padding(4.dp)
            .shadow(elevation = 12.dp, shape = CircleShape)
    )
    Spacer(modifier = Modifier.height(4.dp))
    SkeletonBox(height = 14.dp, width = 72.dp, onPrimary = true)
    Spacer(modifier = Modifier.height(4.dp))
    SkeletonBox(height = 16.dp, width = 48.dp, onPrimary = true)
}