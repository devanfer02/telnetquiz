package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun QuestionHeaderBox(
    title: String,
    description: String,
    imageLink: String?,
    onSpeakClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(
                    bottomEnd = 24.dp,
                    bottomStart = 24.dp
                ),
                clip = false
            )
            .clip(
                RoundedCornerShape(
                    bottomEnd = 24.dp,
                    bottomStart = 24.dp
                )
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        LitecartesColor.Primary,
                        LitecartesColor.Primary.copy(alpha = 0.9f)
                    )
                )
            )
            .padding(top = 18.dp, bottom = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = nunitosFontFamily,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onSpeakClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Baca soal",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            if (!imageLink.isNullOrEmpty()) {
                AsyncImage(
                    model = imageLink,
                    contentDescription = "",
                    modifier = Modifier.size(250.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                text = description,
                textAlign = TextAlign.Justify,
                color = Color.White,
                fontFamily = nunitosFontFamily,
                fontSize = 17.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}
