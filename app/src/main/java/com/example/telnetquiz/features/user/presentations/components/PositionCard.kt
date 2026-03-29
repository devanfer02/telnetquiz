package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.AvatarImage
import com.example.telnetquiz.components.CardWithShadow
import com.example.telnetquiz.features.user.domain.model.User
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun PositionCard(
    user: User,
    rank: Int,
    avatarResIdOverride: Int? = null
) {
    CardWithShadow(
        modifier = Modifier.fillMaxWidth(),
        elevation = 10.dp,
        cornerRadius = 20.dp
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
        Text(
            text = "$rank",
            color = LitecartesColor.Secondary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = nunitosFontFamily
        )
        Spacer(
            modifier = Modifier
                .padding(5.dp)
        )
        AvatarImage(
            localAvatarResId = avatarResIdOverride,
            gender = user.gender,
            nameSeed = user.fullname,
            shape = CircleShape,
            modifier = Modifier.size(65.dp)
        )
        Spacer(
            modifier = Modifier
                .padding(10.dp)
        )
        Column {
            Text(
                text = "${user.fullname}",
                fontFamily = nunitosFontFamily,
                fontSize = 16.sp,
                color = LitecartesColor.Secondary,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "${user.exp} XP",
                fontSize = 18.sp,
                color = LitecartesColor.Primary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(
            modifier = Modifier
                .padding(20.dp)
        )
        }
    }
}

@Preview
@Composable
fun PreviewPositionCard() {
    PositionCard(
        user = User(
            fullname = "Bella Belinda",
            handle = "@belibeli",
            exp = 1250
        ),
        rank = 1
    )
}