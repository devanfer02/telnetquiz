package com.example.telnetquiz.features.user.presentation.components

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.R
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun AchievementCard(
    title: String,
    description: String,
    unlocked: Boolean,
    modifier: Modifier = Modifier
) {
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
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    if (unlocked) LitecartesColor.Primary.copy(alpha = 0.2f)
                    else Color.Gray.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (unlocked) {
                Image(
                    painter = painterResource(id = R.drawable.medal),
                    contentDescription = title,
                    modifier = Modifier.size(35.dp)
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
            title = "Penjelajah Geometri",
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
            title = "Master Bangun Datar",
            description = "Selesaikan semua level di Bab 1",
            unlocked = false
        )
    }
}
