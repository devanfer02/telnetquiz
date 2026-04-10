package com.example.telnetquiz.features.user.presentation.components

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
import com.example.telnetquiz.components.SkeletonBox
import com.example.telnetquiz.components.shimmerEffect
import com.example.telnetquiz.ui.theme.LitecartesColor

@Composable
fun ProfileHeaderSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SkeletonBox(height = 20.dp, width = 140.dp, cornerRadius = 6.dp, onPrimary = true)
        Spacer(modifier = Modifier.height(4.dp))
        SkeletonBox(height = 14.dp, width = 180.dp, onPrimary = true)
        Spacer(modifier = Modifier.height(4.dp))
        SkeletonBox(height = 12.dp, width = 160.dp, onPrimary = true)
    }
}

@Composable
fun AchievementCardSkeleton(modifier: Modifier = Modifier) {
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
        SkeletonBox(height = 50.dp, width = 50.dp, cornerRadius = 12.dp)
        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            SkeletonBox(height = 16.dp, modifier = Modifier.fillMaxWidth(0.6f))
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonBox(height = 12.dp, modifier = Modifier.fillMaxWidth(0.8f))
        }
        SkeletonBox(height = 20.dp, width = 20.dp, cornerRadius = 10.dp)
    }
}
