package com.example.litecartesnative.features.user.presentations.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.example.litecartesnative.features.user.domain.model.User
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun PositionCard(
    user: User,
    rank: Int
) {
    Row(
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(LitecartesColor.DarkerSurface)
            .padding(
                vertical = 10.dp,
                horizontal = 20.dp
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$rank",
            color = LitecartesColor.Secondary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = nunitosFontFamily
        )
        Spacer(
            modifier = Modifier
                .padding(10.dp)
        )
        Box(
            modifier = Modifier
                .background(
                    LitecartesColor.Surface,
                    shape = CircleShape
                )
                .size(65.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.template_profile),
                contentDescription = "profile",
                modifier = Modifier
                    .size(65.dp)
            )
        }
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
                text = "${user.handle}",
                fontFamily = nunitosFontFamily,
                fontSize = 12.sp,
                color = LitecartesColor.Secondary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(
            modifier = Modifier
                .padding(20.dp)
        )
        Text(
            text = "${user.exp} XP",
            fontSize = 22.sp,
            color = LitecartesColor.Primary,
            fontWeight = FontWeight.Bold
        )
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