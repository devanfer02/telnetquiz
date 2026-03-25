package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.data.remote.dto.ActivityEntryDto
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ActivityEntryCard(index: Int, entry: ActivityEntryDto) {
    Column(
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(LitecartesColor.DarkerSurface)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "$index. ${entry.chapterTitle}, Level ${entry.quizLevel}",
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = LitecartesColor.Secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Percobaan: ${entry.retryCount}, Skor: ${entry.latestScore}",
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = LitecartesColor.Secondary.copy(alpha = 0.7f)
        )
    }
}
