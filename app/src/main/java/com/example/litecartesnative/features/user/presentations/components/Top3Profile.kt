package com.example.litecartesnative.features.user.presentations.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.litecartesnative.R
import com.example.litecartesnative.features.user.domain.model.User
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun Top3Profile(
    user: User
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(100.dp)
            )
            .background(
                LitecartesColor.Surface,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(100.dp),
            painter = painterResource(id = R.drawable.template_profile),
            contentDescription = ""
        )
    }
    Text(
        text = user.fullname,
        fontFamily = nunitosFontFamily,
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
    Text(
        text = user.handle,
        fontFamily = nunitosFontFamily,
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.padding(1.dp))
    Text(
        text = "${user.exp} XP",
        fontFamily = nunitosFontFamily,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold
    )
}