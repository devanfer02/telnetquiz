package com.example.telnetquiz.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.R
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun EmptyStateBox(
    title: String,
    subtitle: String = "",
    imageResId: Int? = null,
    imageSize: Dp = 160.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageResId != null) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier.size(imageSize)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = title,
                color = LitecartesColor.Secondary,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = subtitle,
                    color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                    fontFamily = nunitosFontFamily,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmptyStateBoxWithImage() {
    LitecartesNativeTheme {
        EmptyStateBox(
            title = "Belum ada aktivitas",
            subtitle = "Yuk mainkan level pertamamu!",
            imageResId = R.drawable.start_screen
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmptyStateBoxNoImage() {
    LitecartesNativeTheme {
        EmptyStateBox(
            title = "Belum ada materi tersedia",
            subtitle = "Silakan coba lagi nanti"
        )
    }
}
