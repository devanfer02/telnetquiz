package com.example.telnetquiz.features.chapter.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ComingSoonCard(
    title: String = "Konten baru sedang dalam proses",
    subtitle: String = "Pengalamatan IP · Topologi Jaringan. Nantikan!",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 1.5.dp.toPx()
            val radius = 18.dp.toPx()
            val inset = strokeWidth / 2f
            drawRoundRect(
                color = LitecartesColor.Secondary.copy(alpha = 0.3f),
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = androidx.compose.ui.geometry.Size(
                    size.width - strokeWidth,
                    size.height - strokeWidth
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius),
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(LitecartesColor.Secondary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.HourglassEmpty,
                    contentDescription = null,
                    tint = LitecartesColor.Secondary.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = LitecartesColor.Secondary,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
                Text(
                    text = subtitle,
                    color = LitecartesColor.Secondary.copy(alpha = 0.65f),
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewComingSoonCard() {
    LitecartesNativeTheme {
        ComingSoonCard()
    }
}
