package com.example.telnetquiz.features.chapter.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.components.SkeletonBox
import com.example.telnetquiz.ui.theme.LitecartesColor

@Composable
fun ChapterCardSkeleton(modifier: Modifier = Modifier) {
    CardWithShadow(
        modifier = modifier.fillMaxWidth(),
        elevation = 12.dp,
        cornerRadius = 20.dp,
        ambientColor = LitecartesColor.DarkBrown.copy(alpha = 0.3f),
        spotColor = LitecartesColor.DarkBrown.copy(alpha = 0.3f),
        borderStroke = BorderStroke(1.dp, LitecartesColor.Secondary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                SkeletonBox(height = 22.dp, cornerRadius = 6.dp, modifier = Modifier.fillMaxWidth(0.6f))
                Spacer(modifier = Modifier.height(8.dp))
                SkeletonBox(height = 14.dp, modifier = Modifier.fillMaxWidth(0.85f))
                Spacer(modifier = Modifier.height(4.dp))
                SkeletonBox(height = 14.dp, modifier = Modifier.fillMaxWidth(0.7f))
                Spacer(modifier = Modifier.height(10.dp))
                SkeletonBox(height = 12.dp, modifier = Modifier.fillMaxWidth(0.4f))
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonBox(height = 8.dp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(10.dp))
                SkeletonBox(height = 36.dp, cornerRadius = 12.dp, modifier = Modifier.fillMaxWidth(0.5f))
            }
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .heightIn(min = 150.dp),
                contentAlignment = Alignment.Center
            ) {
                SkeletonBox(height = 120.dp, cornerRadius = 12.dp, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
