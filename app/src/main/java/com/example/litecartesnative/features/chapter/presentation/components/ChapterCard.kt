package com.example.litecartesnative.features.chapter.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.litecartesnative.R
import com.example.litecartesnative.components.Button
import com.example.litecartesnative.data.remote.dto.ChapterDto
import com.example.litecartesnative.features.quiz.domain.model.Chapter
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun ChapterCard(
    chapter: Chapter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
            .clip(RoundedCornerShape(16.dp))
            .background(LitecartesColor.DarkerSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                )

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
    val progress = if (chapter.quizCount > 0) {
        (chapter.completedQuizzes * 100) / chapter.quizCount
    } else 0

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
            .clip(RoundedCornerShape(16.dp))
            .background(LitecartesColor.DarkerSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                )
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
                if (progress > 0) {
                    Text(
                        text = "Progress: $progress%",
                        color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                        fontFamily = nunitosFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
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