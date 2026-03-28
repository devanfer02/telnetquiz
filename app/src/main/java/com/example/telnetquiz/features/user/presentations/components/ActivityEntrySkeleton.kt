package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.components.SkeletonBox

@Composable
fun ActivityDateHeaderSkeleton(modifier: Modifier = Modifier) {
    SkeletonBox(
        height = 16.dp,
        width = 120.dp,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ActivityEntryCardSkeleton(modifier: Modifier = Modifier) {
    CardWithShadow(
        modifier = modifier.fillMaxWidth(),
        elevation = 10.dp,
        cornerRadius = 20.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            SkeletonBox(height = 16.dp, modifier = Modifier.fillMaxWidth(0.7f))
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonBox(height = 13.dp, modifier = Modifier.fillMaxWidth(0.5f))
        }
    }
}
