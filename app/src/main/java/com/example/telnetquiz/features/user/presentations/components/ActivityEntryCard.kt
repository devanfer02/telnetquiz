package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.data.remote.dto.ActivityEntryDto
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import com.example.telnetquiz.ui.theme.scoreColor

@Composable
fun ActivityEntryCard(index: Int, entry: ActivityEntryDto) {
    val color = scoreColor(entry.latestScore)

    Row(
        modifier = Modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(LitecartesColor.DarkerSurface)
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${entry.latestScore}",
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = LitecartesColor.PathColor
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.chapterTitle,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = LitecartesColor.Secondary
            )
            Text(
                text = "Level ${entry.quizLevel} · ${entry.retryCount}x percobaan",
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = LitecartesColor.Secondary.copy(alpha = 0.6f)
            )
        }
    }
}
