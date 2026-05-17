package com.example.telnetquiz.features.user.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.AvatarImage
import com.example.telnetquiz.components.SkeletonBox
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

private data class PodiumVisual(
    val ringColor: Color,
    val pedestalColor: Color,
    val pedestalHeight: androidx.compose.ui.unit.Dp,
    val avatarSize: androidx.compose.ui.unit.Dp
)

private fun podiumFor(rank: Int): PodiumVisual = when (rank) {
    1 -> PodiumVisual(
        ringColor = Color(0xFFFFC93C),
        pedestalColor = Color(0xFFE6A724),
        pedestalHeight = 76.dp,
        avatarSize = 82.dp
    )
    2 -> PodiumVisual(
        ringColor = Color(0xFFC9CDD2),
        pedestalColor = Color(0xFFB6BCC4),
        pedestalHeight = 56.dp,
        avatarSize = 72.dp
    )
    else -> PodiumVisual(
        ringColor = Color(0xFFC58A4E),
        pedestalColor = Color(0xFFA86A2C),
        pedestalHeight = 46.dp,
        avatarSize = 72.dp
    )
}

@Composable
fun Top3Profile(
    user: User,
    rank: Int,
    avatarResIdOverride: Int? = null,
    modifier: Modifier = Modifier
) {
    val visual = podiumFor(rank)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(visual.avatarSize + 10.dp)
                    .shadow(elevation = 6.dp, shape = CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(width = 4.dp, color = visual.ringColor, shape = CircleShape)
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                AvatarImage(
                    localAvatarResId = avatarResIdOverride,
                    gender = user.gender,
                    nameSeed = user.fullname,
                    shape = CircleShape,
                    modifier = Modifier.size(visual.avatarSize)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .offset(y = 2.dp)
                    .size(24.dp)
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .background(visual.ringColor, CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            ) {
                Text(
                    text = "$rank",
                    fontFamily = nunitosFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = user.fullname,
            fontFamily = nunitosFontFamily,
            fontSize = 13.sp,
            color = LitecartesColor.Secondary,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        XpPill(xp = user.exp, dense = true)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(visual.pedestalHeight)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(visual.pedestalColor)
                .shadow(elevation = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "$rank",
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
fun XpPill(xp: Int, dense: Boolean = false, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(LitecartesColor.Primary)
            .padding(
                horizontal = if (dense) 8.dp else 10.dp,
                vertical = if (dense) 3.dp else 5.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Bolt,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(if (dense) 12.dp else 14.dp)
        )
        Text(
            text = "$xp XP",
            color = Color.White,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (dense) 11.sp else 12.sp
        )
    }
}

@Composable
fun Top3ProfileSkeleton() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SkeletonBox(
            height = 90.dp,
            width = 90.dp,
            cornerRadius = 45.dp,
            modifier = Modifier
                .padding(4.dp)
                .shadow(elevation = 12.dp, shape = CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonBox(height = 12.dp, width = 80.dp)
        Spacer(modifier = Modifier.height(6.dp))
        SkeletonBox(height = 18.dp, width = 56.dp, cornerRadius = 10.dp)
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonBox(height = 56.dp, width = 80.dp, cornerRadius = 10.dp)
    }
}
