package com.example.telnetquiz.features.user.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.data.remote.dto.ActivityEntryDto
import com.example.telnetquiz.data.remote.dto.ChapterActivityGroupDto
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import com.example.telnetquiz.ui.theme.scoreColor

private val SpineWidth = 56.dp
private val MilestoneSize = 44.dp

@Composable
fun ActivityDateHeader(
    formattedDate: String,
    levelCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = LitecartesColor.Secondary.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = null,
                tint = LitecartesColor.Secondary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = formattedDate,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = LitecartesColor.Secondary
            )
        }
        Text(
            text = "$levelCount level",
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = LitecartesColor.Secondary.copy(alpha = 0.55f)
        )
    }
}

@Composable
fun ChapterActivityBanner(
    group: ChapterActivityGroupDto,
    modifier: Modifier = Modifier
) {
    val icon = chapterIconFor(group.chapterId, group.chapterTitle)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LitecartesColor.Primary)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = group.chapterTitle,
                color = Color.White,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${group.levelsCompletedToday}/${group.totalLevels} level · rata-rata ${group.averageScore}",
                color = Color.White.copy(alpha = 0.85f),
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.22f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${group.completionPercentage}%",
                color = Color.White,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun LevelActivityRow(
    entry: ActivityEntryDto,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    val milestoneColor = scoreColor(entry.latestScore)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(SpineWidth)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
            ) {
                val midY = size.height / 2f
                val lineColor = LitecartesColor.Secondary.copy(alpha = 0.25f)
                if (!isFirst) {
                    drawLine(
                        color = lineColor,
                        start = Offset(size.width / 2f, 0f),
                        end = Offset(size.width / 2f, midY),
                        strokeWidth = 2f
                    )
                }
                if (!isLast) {
                    drawLine(
                        color = lineColor,
                        start = Offset(size.width / 2f, midY),
                        end = Offset(size.width / 2f, size.height),
                        strokeWidth = 2f
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(MilestoneSize)
                    .clip(CircleShape)
                    .background(milestoneColor)
                    .border(2.5.dp, LitecartesColor.DarkBrown, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${entry.latestScore}",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LitecartesColor.DarkBrown
                )
            }
        }

        LevelCard(entry = entry, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun LevelCard(entry: ActivityEntryDto, modifier: Modifier = Modifier) {
    val showTrophy = entry.latestScore == 100 && entry.retryCount == 1
    val labelColor = scoreColor(entry.latestScore)

    CardWithShadow(
        modifier = modifier.padding(vertical = 6.dp),
        elevation = 4.dp,
        cornerRadius = 14.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Level ${entry.quizLevel}",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LitecartesColor.Secondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = scoreLabel(entry.latestScore),
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        color = labelColor
                    )
                    Text(
                        text = "·",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = LitecartesColor.Secondary.copy(alpha = 0.45f)
                    )
                    Text(
                        text = "${entry.retryCount}x percobaan",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = LitecartesColor.Secondary.copy(alpha = 0.65f)
                    )
                }
            }
            if (showTrophy) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(LitecartesColor.ScoreYellow.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = "Penghargaan sempurna",
                        tint = LitecartesColor.ScoreYellow,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

private fun scoreLabel(score: Int): String = when {
    score >= 100 -> "Sempurna"
    score >= 80 -> "Bagus"
    score >= 60 -> "Cukup"
    else -> "Perlu Belajar"
}

private fun chapterIconFor(chapterId: Int, title: String): ImageVector {
    val lower = title.lowercase()
    return when {
        "tcp" in lower || "ip" in lower || "protokol" in lower -> Icons.Filled.SwapHoriz
        "wlan" in lower || "wifi" in lower || "nirkabel" in lower -> Icons.Filled.Wifi
        "router" in lower || "switch" in lower -> Icons.Filled.Router
        "kabel" in lower || "cable" in lower -> Icons.Filled.Cable
        else -> when (chapterId % 3) {
            0 -> Icons.Filled.DeviceHub
            1 -> Icons.Filled.NetworkCheck
            else -> Icons.Filled.SwapHoriz
        }
    }
}
