package com.example.telnetquiz.features.user.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.AvatarImage
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.features.user.presentation.viewmodel.LeaderboardPeriod
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun PositionCard(
    user: User,
    rank: Int,
    avatarResIdOverride: Int? = null,
    rankDelta: Int? = null,
    period: LeaderboardPeriod = LeaderboardPeriod.WEEK,
    modifier: Modifier = Modifier
) {
    CardWithShadow(
        modifier = modifier.fillMaxWidth(),
        elevation = 5.dp,
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$rank",
                color = LitecartesColor.Secondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = nunitosFontFamily,
                modifier = Modifier.width(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            AvatarImage(
                localAvatarResId = avatarResIdOverride,
                gender = user.gender,
                nameSeed = user.fullname,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = user.fullname,
                    fontFamily = nunitosFontFamily,
                    fontSize = 14.sp,
                    color = LitecartesColor.Secondary,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                TrendLine(rankDelta = rankDelta, period = period)
            }
            Spacer(modifier = Modifier.width(8.dp))
            XpPill(xp = user.exp, dense = true)
        }
    }
}

@Composable
private fun TrendLine(rankDelta: Int?, period: LeaderboardPeriod) {
    if (period == LeaderboardPeriod.ALL || rankDelta == null) return
    val (icon, tint, prefix) = when {
        rankDelta > 0 -> Triple(Icons.Filled.TrendingUp, LitecartesColor.GreenCactus, "+$rankDelta")
        rankDelta < 0 -> Triple(Icons.Filled.TrendingDown, LitecartesColor.ScoreRed, "$rankDelta")
        else -> Triple(Icons.Filled.TrendingFlat, LitecartesColor.Secondary.copy(alpha = 0.6f), "0")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "$prefix ${period.sinceLabel}",
            fontFamily = nunitosFontFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = LitecartesColor.Secondary.copy(alpha = 0.7f)
        )
    }
}

@Preview
@Composable
fun PreviewPositionCard() {
    PositionCard(
        user = User(fullname = "Muhammad Rafael D.", exp = 800),
        rank = 4,
        rankDelta = 16
    )
}
