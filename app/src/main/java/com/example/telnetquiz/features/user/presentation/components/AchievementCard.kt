package com.example.telnetquiz.features.user.presentation.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private data class AchievementVisual(val icon: ImageVector, val tint: Color)

private fun visualFor(title: String): AchievementVisual {
    val lower = title.lowercase()
    return when {
        listOf("sempurna", "perfect", "skor").any { it in lower } ->
            AchievementVisual(Icons.Filled.EmojiEvents, LitecartesColor.ScoreYellow)
        listOf("cepat", "kilat", "speed", "fast").any { it in lower } ->
            AchievementVisual(Icons.Filled.Rocket, LitecartesColor.ScoreOrange)
        listOf("streak", "beruntun", "berturut").any { it in lower } ->
            AchievementVisual(Icons.Filled.LocalFireDepartment, LitecartesColor.ScoreRed)
        listOf("bab", "chapter").any { it in lower } ->
            AchievementVisual(Icons.Filled.MenuBook, LitecartesColor.GreenCactus)
        listOf("level", "penjelajah", "explorer").any { it in lower } ->
            AchievementVisual(Icons.Filled.Stars, LitecartesColor.Primary)
        else ->
            AchievementVisual(Icons.Filled.EmojiEvents, LitecartesColor.Primary)
    }
}

@Composable
fun AchievementCard(
    title: String,
    description: String,
    unlocked: Boolean,
    unlockedAt: String? = null,
    modifier: Modifier = Modifier
) {
    val visual = visualFor(title)
    val tint = if (unlocked) visual.tint else Color.Gray
    val dateLabel = unlockedAt?.let { formatUnlockedDate(it) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 4.dp)
            .shadow(
                elevation = if (unlocked) 5.dp else 0.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (unlocked) LitecartesColor.Surface
                else LitecartesColor.DarkerSurface.copy(alpha = 0.6f)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(tint)
        )
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(tint.copy(alpha = if (unlocked) 0.18f else 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (unlocked) visual.icon else Icons.Default.Lock,
                contentDescription = title,
                tint = if (unlocked) tint else Color.Gray.copy(alpha = 0.55f),
                modifier = Modifier.size(20.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Text(
                text = title,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = if (unlocked) LitecartesColor.Secondary
                else LitecartesColor.Secondary.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = description,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = if (unlocked) LitecartesColor.Secondary.copy(alpha = 0.65f)
                else LitecartesColor.Secondary.copy(alpha = 0.4f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(end = 12.dp)
        ) {
            if (unlocked) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(LitecartesColor.GreenCactus),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
                if (!dateLabel.isNullOrBlank()) {
                    Text(
                        text = dateLabel,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        color = LitecartesColor.Secondary.copy(alpha = 0.65f)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Terkunci",
                    tint = Color.Gray.copy(alpha = 0.55f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

private fun formatUnlockedDate(iso: String): String =
    runCatching {
        OffsetDateTime.parse(iso)
            .atZoneSameInstant(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd MMM", Locale("id", "ID")))
    }.getOrDefault("")

@Preview(showBackground = true)
@Composable
private fun PreviewAchievementCardUnlocked() {
    LitecartesNativeTheme {
        AchievementCard(
            title = "Penjelajah Pretest",
            description = "Menyelesaikan pretest",
            unlocked = true,
            unlockedAt = "2026-05-11T00:00:00Z"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAchievementCardLocked() {
    LitecartesNativeTheme {
        AchievementCard(
            title = "Master Topologi",
            description = "Selesaikan semua level di Bab 1",
            unlocked = false
        )
    }
}
