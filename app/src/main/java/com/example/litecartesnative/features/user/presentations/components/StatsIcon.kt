package com.example.litecartesnative.features.user.presentations.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.litecartesnative.R
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun StatsIcon(
    resId: Int,
    statName: String,
    stat: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
        )
        Text(
            text = "$stat",
            color = LitecartesColor.Secondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = nunitosFontFamily
        )
        Text(
            text = "$statName",
            color = LitecartesColor.Secondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = nunitosFontFamily,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun PreviewStatsIcon() {
    StatsIcon(
        resId = R.drawable.diamond,
        statName = "Peringkat\nNasional",
        stat = 250
    )
}