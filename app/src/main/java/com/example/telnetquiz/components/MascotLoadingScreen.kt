package com.example.telnetquiz.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.R
import com.example.telnetquiz.ui.theme.LitecartesColor

private val mascotDrawables = listOf(
    R.drawable.splash,
    R.drawable.start_screen,
    R.drawable.quickcheck,
    R.drawable.result,
    R.drawable.chap1,
    R.drawable.chap2
)

@Composable
fun MascotLoadingScreen(
    modifier: Modifier = Modifier
) {
    val mascotId = remember { mascotDrawables.random() }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = mascotId),
                contentDescription = "Loading",
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = LitecartesColor.Primary,
                trackColor = LitecartesColor.DarkerSurface
            )
        }
    }
}
