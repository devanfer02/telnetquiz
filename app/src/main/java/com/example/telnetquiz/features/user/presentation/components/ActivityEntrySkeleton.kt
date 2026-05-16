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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.components.SkeletonBox
import com.example.telnetquiz.ui.theme.LitecartesColor

@Composable
fun ActivityDateHeaderSkeleton(modifier: Modifier = Modifier) {
    SkeletonBox(
        height = 16.dp,
        width = 120.dp,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ActivityEntryCardSkeleton(modifier: Modifier = Modifier, cardOnLeft: Boolean = true) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (cardOnLeft) {
            Box(modifier = Modifier.weight(1f)) { ShimmerCard() }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Box(
            modifier = Modifier.width(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(LitecartesColor.DarkerSurface, shape = CircleShape)
                    .border(2.dp, LitecartesColor.DarkBrown.copy(alpha = 0.3f), CircleShape)
            )
        }

        if (!cardOnLeft) {
            Box(modifier = Modifier.weight(1f)) { ShimmerCard() }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ShimmerCard() {
    CardWithShadow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 6.dp,
        cornerRadius = 14.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            SkeletonBox(height = 10.dp, modifier = Modifier.fillMaxWidth(0.3f))
            Spacer(modifier = Modifier.height(6.dp))
            SkeletonBox(height = 14.dp, modifier = Modifier.fillMaxWidth(0.8f))
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonBox(height = 11.dp, modifier = Modifier.fillMaxWidth(0.5f))
            Spacer(modifier = Modifier.height(8.dp))
            SkeletonBox(height = 6.dp, modifier = Modifier.fillMaxWidth())
        }
    }
}
