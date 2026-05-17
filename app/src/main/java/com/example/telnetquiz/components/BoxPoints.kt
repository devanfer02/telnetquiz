package com.example.telnetquiz.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun BoxPoints(
    modifier: Modifier = Modifier,
    imageId: Int,
    points: String
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .shadow(
                elevation = 6.dp,
                clip = false,
                shape = RoundedCornerShape(14.dp)
            )
            .background(LitecartesColor.Surface)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
        AutoSizeText(
            text = points,
            fontFamily = nunitosFontFamily,
            color = LitecartesColor.Secondary,
            fontWeight = FontWeight.ExtraBold,
            maxFontSize = 12.sp,
            minFontSize = 8.sp,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxPoints() {
    BoxPoints(
        imageId = android.R.drawable.star_on,
        points = "150"
    )
}
