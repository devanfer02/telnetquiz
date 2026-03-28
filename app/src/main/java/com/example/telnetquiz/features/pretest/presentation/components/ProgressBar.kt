package com.example.telnetquiz.features.pretest.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ProgressBarFromApi(
    current: Int,
    total: Int
) {
    val progress = if (total > 0) current.toFloat() / total.toFloat() else 0f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LitecartesColor.Surface)
            .padding(
                horizontal = 20.dp,
                vertical = 40.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(30.dp)
                .background(LitecartesColor.Surface)
                .border(
                    BorderStroke(2.dp, LitecartesColor.Secondary),
                    RoundedCornerShape(40)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progress)
                    .background(LitecartesColor.Primary, RoundedCornerShape(40))
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "${current}/${total}",
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.Bold,
            color = LitecartesColor.DarkBrown
        )
    }
}

@Preview
@Composable
fun PreviewProgressBarFromApi() {
    ProgressBarFromApi(
        current = 1,
        total = 10
    )
}