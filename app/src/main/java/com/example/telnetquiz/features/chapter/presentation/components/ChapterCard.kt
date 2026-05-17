package com.example.telnetquiz.features.chapter.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.telnetquiz.BuildConfig
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.data.remote.dto.ChapterDto
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ChapterCardFromApi(
    chapter: ChapterDto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val theme = chapterThemeFor(chapter)
    val total = chapter.quizCount.coerceAtLeast(0)
    val completed = chapter.completedQuizzes.coerceIn(0, total)
    val isCompleted = total > 0 && completed >= total
    val percent = if (total > 0) (completed * 100) / total else 0
    var expanded by remember { mutableStateOf(false) }

    CardWithShadow(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = 10.dp,
        cornerRadius = 18.dp,
        ambientColor = LitecartesColor.DarkBrown.copy(alpha = 0.3f),
        spotColor = LitecartesColor.DarkBrown.copy(alpha = 0.3f),
        borderStroke = BorderStroke(1.dp, LitecartesColor.Secondary.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clickable { expanded = !expanded }
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(theme.accent)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${BuildConfig.BASE_URL}/assets/mascot/chap${chapter.mascotId}.png")
                        .crossfade(true)
                        .build(),
                    contentDescription = chapter.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                        .size(90.dp)
                        .alpha(0.35f)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(theme.accent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = theme.icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = chapter.title,
                            color = LitecartesColor.Secondary,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (isCompleted) {
                            TuntasPill()
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = chapter.description,
                        color = LitecartesColor.Secondary.copy(alpha = 0.85f),
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        maxLines = if (expanded) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth(if (expanded) 1f else 0.78f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FlatProgressBar(
                            progress = percent / 100f,
                            color = theme.accent,
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                        )
                        ActionPillButton(
                            text = if (isCompleted) "ULANG" else "YUK MAIN",
                            color = theme.accent,
                            onClick = onClick
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$completed/$total kuis selesai",
                            color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "$percent%",
                            color = theme.accent,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TuntasPill() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(LitecartesColor.GreenCactus)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = "Tuntas",
            color = Color.White,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun FlatProgressBar(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(LitecartesColor.Secondary.copy(alpha = 0.12f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}

@Composable
private fun ActionPillButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp
        )
        Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
    }
}

private data class ChapterTheme(val accent: Color, val icon: ImageVector)

private val ChapterPalette = listOf(
    ChapterTheme(LitecartesColor.Primary, Icons.Filled.SwapHoriz),
    ChapterTheme(LitecartesColor.ScoreRed, Icons.Filled.Wifi),
    ChapterTheme(LitecartesColor.GreenCactus, Icons.Filled.AltRoute),
    ChapterTheme(LitecartesColor.ScoreBlue, Icons.Filled.DeviceHub)
)

private fun chapterThemeFor(chapter: ChapterDto): ChapterTheme {
    val lower = chapter.title.lowercase()
    val keyword = when {
        "tcp" in lower -> 0
        "wlan" in lower || "wifi" in lower || "nirkabel" in lower -> 1
        "alamat" in lower || "subnet" in lower -> 2
        "topologi" in lower -> 3
        else -> null
    }
    val idx = keyword ?: ((chapter.id - 1).coerceAtLeast(0) % ChapterPalette.size)
    return ChapterPalette[idx]
}
