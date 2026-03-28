package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.components.rememberShimmerColor
import com.example.telnetquiz.components.rememberSurfaceShimmerColor
import com.example.telnetquiz.ui.theme.LitecartesColor

@Composable
fun ProfileHeaderSkeleton(modifier: Modifier = Modifier) {
    val shimmer = rememberShimmerColor()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(shimmer)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmer)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmer)
        )
    }
}

@Composable
fun AchievementCardSkeleton(modifier: Modifier = Modifier) {
    val shimmer = rememberSurfaceShimmerColor()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(LitecartesColor.DarkerSurface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(shimmer)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmer)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmer)
            )
        }
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(shimmer)
        )
    }
}
