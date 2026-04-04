package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.AvatarImage
import com.example.telnetquiz.components.SkeletonBox
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun Top3Profile(
    user: User,
    rank: Int,
    avatarResIdOverride: Int? = null
) {
    Box(contentAlignment = Alignment.BottomCenter) {
        AvatarImage(
            localAvatarResId = avatarResIdOverride,
            gender = user.gender,
            nameSeed = user.fullname,
            shape = CircleShape,
            modifier = Modifier
                .padding(4.dp)
                .size(75.dp)
                .shadow(elevation = 12.dp, shape = CircleShape, clip = false)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(y = 4.dp)
                .size(24.dp)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .background(LitecartesColor.Secondary, CircleShape)
        ) {
            Text(
                text = "$rank",
                fontFamily = nunitosFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
    Text(
        text = user.fullname,
        fontFamily = nunitosFontFamily,
        fontSize = 14.sp,
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
    Text(
        text = "${user.exp} XP",
        fontFamily = nunitosFontFamily,
        color = Color.White,
        fontSize = 12.sp,
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