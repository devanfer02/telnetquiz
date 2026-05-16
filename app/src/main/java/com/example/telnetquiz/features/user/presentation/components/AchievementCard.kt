package com.example.telnetquiz.features.user.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

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
    modifier: Modifier = Modifier
) {
    val visual = visualFor(title)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = if (unlocked) 8.dp else 0.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (unlocked) LitecartesColor.DarkerSurface
                else LitecartesColor.DarkerSurface.copy(alpha = 0.5f)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val badgeTint = if (unlocked) visual.tint else Color.Gray
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    if (unlocked) badgeTint.copy(alpha = 0.18f)
                    else badgeTint.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (unlocked) {
                Icon(
                    imageVector = visual.icon,
                    contentDescription = title,
                    tint = badgeTint,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (unlocked) LitecartesColor.Secondary
                else LitecartesColor.Secondary.copy(alpha = 0.5f)
            )
            Text(
                text = description,
                fontFamily = nunitosFontFamily,
                fontSize = 12.sp,
                color = if (unlocked) LitecartesColor.Secondary.copy(alpha = 0.7f)
                else LitecartesColor.Secondary.copy(alpha = 0.4f)
            )
        }
        if (unlocked) {
            Text(
                text = "✓",
                color = LitecartesColor.Primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAchievementCardUnlocked() {
    LitecartesNativeTheme {
        AchievementCard(
            title = "Penjelajah Jaringan",
            description = "Selesaikan 5 level pertama",
            unlocked = true
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
