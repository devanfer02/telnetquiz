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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.data.remote.dto.ActivityEntryDto
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import com.example.telnetquiz.ui.theme.scoreColor
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpineWidth = 56.dp
private val MilestoneSize = 44.dp

@Composable
fun JourneyPathEntry(
    index: Int,
    entry: ActivityEntryDto,
    isFirstInDay: Boolean,
    isLastInDay: Boolean,
    modifier: Modifier = Modifier
) {
    val cardOnLeft = index % 2 == 0
    val milestoneColor = scoreColor(entry.latestScore)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (cardOnLeft) {
            Box(modifier = Modifier.weight(1f)) { EntryCard(entry) }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Spine(
            milestoneColor = milestoneColor,
            score = entry.latestScore,
            isFirstInDay = isFirstInDay,
            isLastInDay = isLastInDay
        )

        if (!cardOnLeft) {
            Box(modifier = Modifier.weight(1f)) { EntryCard(entry) }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun Spine(
    milestoneColor: Color,
    score: Int,
    isFirstInDay: Boolean,
    isLastInDay: Boolean
) {
    Box(
        modifier = Modifier
            .width(SpineWidth)
            .fillMaxHeight()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
        ) {
            val midY = size.height / 2f
            val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
            val lineColor = LitecartesColor.Secondary.copy(alpha = 0.35f)
            if (!isFirstInDay) {
                drawLine(
                    color = lineColor,
                    start = Offset(size.width / 2f, 0f),
                    end = Offset(size.width / 2f, midY),
                    strokeWidth = 2f,
                    pathEffect = dash
                )
            }
            if (!isLastInDay) {
                drawLine(
                    color = lineColor,
                    start = Offset(size.width / 2f, midY),
                    end = Offset(size.width / 2f, size.height),
                    strokeWidth = 2f,
                    pathEffect = dash
                )
            }
        }

        Box(
            modifier = Modifier
                .size(MilestoneSize)
                .clip(CircleShape)
                .background(milestoneColor)
                .border(2.dp, LitecartesColor.DarkBrown, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$score",
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = LitecartesColor.DarkBrown
            )
        }
    }
}

@Composable
private fun EntryCard(entry: ActivityEntryDto) {
    val barColor = scoreColor(entry.latestScore)
    val progress = entry.latestScore.coerceIn(0, 100) / 100f

    CardWithShadow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 6.dp,
        cornerRadius = 14.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val timeLabel = formatEntryTime(entry.latestTime)
            if (timeLabel.isNotEmpty()) {
                Text(
                    text = timeLabel,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = LitecartesColor.Secondary.copy(alpha = 0.65f)
                )
            }
            Text(
                text = entry.chapterTitle,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = LitecartesColor.Secondary,
                maxLines = 2
            )
            Text(
                text = buildAnnotatedString {
                    val highlightStyle = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = LitecartesColor.Secondary
                    )
                    withStyle(highlightStyle) { append("Level") }
                    append(" ${entry.quizLevel} · ${entry.retryCount}x ")
                    withStyle(highlightStyle) { append("Percobaan") }
                },
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = LitecartesColor.Secondary.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(LitecartesColor.Secondary.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(barColor)
                )
            }
        }
    }
}

private fun formatEntryTime(iso: String?): String {
    if (iso.isNullOrBlank()) return ""
    return runCatching {
        OffsetDateTime.parse(iso)
            .atZoneSameInstant(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm", Locale("id", "ID")))
    }.getOrDefault("")
}
