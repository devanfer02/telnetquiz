package com.example.telnetquiz.features.user.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.features.user.presentation.viewmodel.LeaderboardPeriod
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun CurrentUserRankBox(
    rank: Int,
    totalScore: Int,
    rankDelta: Int? = null,
    period: LeaderboardPeriod = LeaderboardPeriod.WEEK,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LitecartesColor.Primary)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.22f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = if (rank > 0) "#$rank" else "-",
                color = Color.White,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "PERINGKAT KAMU",
                color = Color.White.copy(alpha = 0.85f),
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
            Text(
                text = deltaLine(rankDelta, period),
                color = Color.White,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                maxLines = 1
            )
        }
        XpPill(xp = totalScore)
    }
}

private fun deltaLine(rankDelta: Int?, period: LeaderboardPeriod): String {
    if (period == LeaderboardPeriod.ALL) return "Posisi sepanjang masa"
    if (rankDelta == null) return "Baru di papan ${period.sinceLabel}"
    return when {
        rankDelta > 0 -> "Naik $rankDelta peringkat ${period.sinceLabel}"
        rankDelta < 0 -> "Turun ${-rankDelta} peringkat ${period.sinceLabel}"
        else -> "Stabil ${period.sinceLabel}"
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCurrentUserRankBox() {
    LitecartesNativeTheme {
        CurrentUserRankBox(rank = 18, totalScore = 540, rankDelta = 12)
    }
}
