package com.example.telnetquiz.features.user.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.components.SkeletonBox
import com.example.telnetquiz.ui.theme.LitecartesColor

@Composable
fun ActivityDateHeaderSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SkeletonBox(height = 28.dp, width = 130.dp)
        Spacer(modifier = Modifier.weight(1f))
        SkeletonBox(height = 12.dp, width = 50.dp)
    }
}

@Composable
fun ChapterBannerSkeleton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LitecartesColor.Primary.copy(alpha = 0.65f))
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(LitecartesColor.Surface.copy(alpha = 0.4f))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                SkeletonBox(height = 14.dp, width = 140.dp)
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonBox(height = 10.dp, width = 100.dp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(LitecartesColor.Surface.copy(alpha = 0.4f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                SkeletonBox(height = 12.dp, width = 28.dp)
            }
        }
    }
}

@Composable
fun ActivityEntryCardSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(LitecartesColor.DarkerSurface, shape = CircleShape)
                    .border(2.dp, LitecartesColor.DarkBrown.copy(alpha = 0.25f), CircleShape)
            )
        }
        CardWithShadow(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 6.dp),
            elevation = 4.dp,
            cornerRadius = 14.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                SkeletonBox(height = 14.dp, width = 80.dp)
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonBox(height = 10.dp, width = 130.dp)
            }
        }
    }
}
