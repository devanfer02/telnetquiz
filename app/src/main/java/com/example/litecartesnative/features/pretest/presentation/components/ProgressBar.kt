package com.example.litecartesnative.features.pretest.presentation.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litecartesnative.constants.pretestsData
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun ProgressBar(
    navController: NavController,
    current: Int
) {
    var progress by remember {
        mutableStateOf(current.toFloat() / pretestsData.size.toFloat())
    }

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
            text = "${current}/${pretestsData.size}",
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.Bold,
            color = LitecartesColor.DarkBrown
        )
    }
}

@Preview
@Composable
fun PreviewProgressBar() {
    ProgressBar(
        navController = rememberNavController(),
        1
    )
}