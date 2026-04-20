package com.example.telnetquiz.features.chapter.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.components.CardWithShadow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.telnetquiz.BuildConfig
import com.example.telnetquiz.data.remote.dto.ChapterDto
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ChapterCardFromApi(
    chapter: ChapterDto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    CardWithShadow(
        modifier = modifier.fillMaxWidth(),
        elevation = 12.dp,
        cornerRadius = 20.dp,
        ambientColor = LitecartesColor.DarkBrown.copy(alpha = 0.3f),
        spotColor = LitecartesColor.DarkBrown.copy(alpha = 0.3f),
        borderStroke = BorderStroke(1.dp, LitecartesColor.Secondary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = chapter.title,
                    color = LitecartesColor.Secondary,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
                Text(
                    text = chapter.description,
                    color = LitecartesColor.Secondary,
                    fontFamily = nunitosFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${chapter.completedQuizzes} / ${chapter.quizCount} kuis selesai",
                    color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                    fontFamily = nunitosFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                SegmentedProgressBar(
                    completed = chapter.completedQuizzes,
                    total = chapter.quizCount,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    text = "Yuk Main".uppercase(),
                    color = LitecartesColor.Secondary,
                    backgroundColor = LitecartesColor.Surface,
                    borderColor = LitecartesColor.DarkBrown,
                    shadowEnabled = true,
                    shadowColor = LitecartesColor.DarkBrown,
                    onClick = onClick,
                    shadowHeight = 5.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${BuildConfig.BASE_URL}/assets/mascot/chap${chapter.mascotId}.png")
                        .crossfade(true)
                        .build(),
                    contentDescription = chapter.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .heightIn(min = 150.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SegmentedProgressBar(
    completed: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val segments = total.coerceAtLeast(1)

    Box(
        modifier = modifier
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(LitecartesColor.Surface)
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(5.dp))
        ) {
            for (i in 0 until segments) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (i < completed) LitecartesColor.Primary
                            else LitecartesColor.Surface
                        )
                )
                if (i < segments - 1) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .fillMaxHeight()
                            .background(LitecartesColor.Secondary.copy(alpha = 0.3f))
                    )
                }
            }
        }
    }
}
