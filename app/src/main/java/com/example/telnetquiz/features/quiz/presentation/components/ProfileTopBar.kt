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
import com.example.telnetquiz.R
import com.example.telnetquiz.components.AvatarImage
import com.example.telnetquiz.components.SkeletonBox
import com.example.telnetquiz.components.TopBarContainer
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
    localAvatarResId: Int? = null,
    totalScore: Int = 0,
    dailyStreak: Int = 0,
    tag: String = "Penjelajah"
) {
    TopBarContainer(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor),
        cornerRadius = 24.dp,
        backgroundColor = LitecartesColor.Secondary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 18.dp)
        ) {
            if (isLoading) {
                SkeletonBox(
                    height = 55.dp,
                    width = 55.dp,
                    cornerRadius = 18.dp,
                    onPrimary = true
                )
            } else {
                AvatarImage(
                    imageUrl = imageUrl,
                    localAvatarResId = localAvatarResId,
                    gender = gender,
                    nameSeed = name,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .height(55.dp)
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            if (isLoading) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    SkeletonBox(height = 18.dp, width = 100.dp, cornerRadius = 6.dp, onPrimary = true)
                    Spacer(modifier = Modifier.height(4.dp))
                    SkeletonBox(height = 10.dp, width = 70.dp, onPrimary = true)
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
                        SkeletonBox(height = 22.dp, cornerRadius = 12.dp, onPrimary = true, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        SkeletonBox(height = 22.dp, cornerRadius = 12.dp, onPrimary = true, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    SkeletonBox(height = 20.dp, cornerRadius = 12.dp, onPrimary = true, modifier = Modifier.fillMaxWidth())
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