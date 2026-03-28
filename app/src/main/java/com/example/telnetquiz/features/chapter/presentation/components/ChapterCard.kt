package com.example.telnetquiz.features.chapter.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.R
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.data.remote.dto.ChapterDto
import com.example.telnetquiz.features.quiz.domain.model.Chapter
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ChapterCard(
    chapter: Chapter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
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
                modifier = Modifier.weight(1f)
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
                Button(
                    text = "Yuk Main".uppercase(),
                    color = LitecartesColor.Secondary,
                    backgroundColor = LitecartesColor.Surface,
                    borderColor = LitecartesColor.DarkBrown,
                    shadowEnabled = true,
                    shadowColor = LitecartesColor.DarkBrown,
                    onClick = onClick,
                    shadowHeight = 5.dp
                )
            }
            Box(
                modifier = Modifier.weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .heightIn(min = 150.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = chapter.imageLink),
                    contentDescription = chapter.title
                )
            }
        }
    }
}

@Composable
fun ChapterCardFromApi(
    chapter: ChapterDto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val progressFraction = if (chapter.quizCount > 0) {
        chapter.completedQuizzes.toFloat() / chapter.quizCount.toFloat()
    } else 0f

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
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = LitecartesColor.Primary,
                    trackColor = LitecartesColor.Surface,
                )
                Button(
                    text = "Yuk Main".uppercase(),
                    color = LitecartesColor.Secondary,
                    backgroundColor = LitecartesColor.Surface,
                    borderColor = LitecartesColor.DarkBrown,
                    shadowEnabled = true,
                    shadowColor = LitecartesColor.DarkBrown,
                    onClick = onClick,
                    shadowHeight = 5.dp
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .heightIn(min = 150.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.chap1), // Default image for API chapters
                    contentDescription = chapter.title
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewChapterCard() {
    ChapterCard(
        chapter = Chapter(
            title = "Mengenal Bangun Datar",
            description = "Mulailah perjalanan literasimu dengan membaca kata-kata sederhana dan memahami ide dari paragraf pendek.",
            levels = mutableListOf(),
            imageLink = R.drawable.chap1
        )
    )
}