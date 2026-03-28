package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.telnetquiz.R
import com.example.telnetquiz.components.GenderAvatar
import com.example.telnetquiz.components.shimmerOnPrimary
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ProfileTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = LitecartesColor.Surface,
    isLoading: Boolean = false,
    name: String = "",
    school: String = "",
    imageUrl: String? = null,
    gender: Boolean? = null,
    totalScore: Int = 0,
    dailyStreak: Int = 0,
    tag: String = "Penjelajah"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clip(
                RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp,
                )
            )
            .background(LitecartesColor.Secondary)
            .padding(
                vertical = 20.dp,
                horizontal = 18.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .height(55.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .shimmerOnPrimary()
                )
            } else if (imageUrl != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(18.dp),
                            clip = false
                        )
                        .background(LitecartesColor.Primary),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "profile image",
                        modifier = Modifier
                            .height(55.dp)
                            .aspectRatio(1f)
                    )
                }
            } else {
                GenderAvatar(gender = gender, size = 55.dp)
            }
            Spacer(modifier = Modifier.padding(4.dp))
            if (isLoading) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .shimmerOnPrimary()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerOnPrimary()
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name,
                        fontFamily = nunitosFontFamily,
                        color = LitecartesColor.Surface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = school,
                        fontFamily = nunitosFontFamily,
                        color = LitecartesColor.Surface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp
                    )
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp
                    )
            ) {
                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(22.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmerOnPrimary()
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(22.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmerOnPrimary()
                        )
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .shimmerOnPrimary()
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BoxPoints(
                            modifier = Modifier.weight(1f),
                            imageId = R.drawable.diamon,
                            points = "$totalScore",
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        BoxPoints(
                            modifier = Modifier.weight(1f),
                            imageId = R.drawable.lightning,
                            points = "$dailyStreak"
                        )
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(20.dp),
                                clip = false
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(LitecartesColor.Surface)
                            .fillMaxWidth()
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tag,
                            fontFamily = nunitosFontFamily,
                            color = LitecartesColor.Secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewProfileTopBar() {
    ProfileTopBar()
}