package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.R
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun Top3Profile(
    user: User
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(100.dp)
            )
            .background(
                LitecartesColor.Surface,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(100.dp),
            painter = painterResource(id = R.drawable.template_profile),
            contentDescription = ""
        )
    }
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
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeleton_alpha"
    )
    val shimmerColor = Color.White.copy(alpha = alpha)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(100.dp)
            )
            .background(shimmerColor, shape = CircleShape)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Box(
        modifier = Modifier
            .width(72.dp)
            .height(14.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(shimmerColor)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(16.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(shimmerColor)
    )
}