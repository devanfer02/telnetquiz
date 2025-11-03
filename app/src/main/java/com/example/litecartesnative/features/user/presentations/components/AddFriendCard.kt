package com.example.litecartesnative.features.user.presentations.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.litecartesnative.R
import com.example.litecartesnative.features.user.domain.model.User
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun AddFriendCard(user: User) {
    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(LitecartesColor.DarkerSurface)
            .padding(
                horizontal = 14.dp,
                vertical = 6.dp
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.template_profile),
                contentDescription = ""
            )
            Column(
                modifier = Modifier

            ) {
                Text(
                    text = user.fullname,
                    color = LitecartesColor.Secondary,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.handle,
                    color = LitecartesColor.Secondary,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(6.dp)
                )
                .background(LitecartesColor.Secondary)

        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add",
                tint = LitecartesColor.Surface
            )
        }
    }
}

@Preview
@Composable
fun PreviewAddFriendCard() {
    AddFriendCard(
        user = User(
            fullname = "Liam Carter",
            handle = "@LiamLegoMaster",
            exp = 3
        )
    )
}
