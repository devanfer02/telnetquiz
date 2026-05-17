package com.example.telnetquiz.features.chapter.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
        elevation = 10.dp,
        cornerRadius = 18.dp,
        ambientColor = LitecartesColor.DarkBrown.copy(alpha = 0.3f),
        spotColor = LitecartesColor.DarkBrown.copy(alpha = 0.3f),
        borderStroke = BorderStroke(1.dp, LitecartesColor.Secondary.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(LitecartesColor.Secondary.copy(alpha = 0.2f))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SkeletonBox(height = 36.dp, width = 36.dp, cornerRadius = 10.dp)
                    SkeletonBox(height = 18.dp, width = 140.dp, cornerRadius = 6.dp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                SkeletonBox(height = 12.dp, modifier = Modifier.fillMaxWidth(0.85f))
                Spacer(modifier = Modifier.height(4.dp))
                SkeletonBox(height = 12.dp, modifier = Modifier.fillMaxWidth(0.6f))
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SkeletonBox(
                        height = 8.dp,
                        cornerRadius = 50.dp,
                        modifier = Modifier.weight(1f)
                    )
                    SkeletonBox(height = 32.dp, width = 100.dp, cornerRadius = 50.dp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    SkeletonBox(height = 10.dp, width = 90.dp)
                    Spacer(modifier = Modifier.weight(1f))
                    SkeletonBox(height = 10.dp, width = 28.dp)
                }
            }
        }
    }
}
