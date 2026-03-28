package com.example.telnetquiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Woman
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val MaleBg = Color(0xFF4A90D9)
private val FemaleBg = Color(0xFFD94A8A)
private val NeutralBg = Color(0xFFA0734A)

private val MaleBorder = Color(0xFF6BB3FF)
private val FemaleBorder = Color(0xFFFF6BB3)
private val NeutralBorder = Color(0xFFC9956B)

@Composable
fun GenderAvatar(
    gender: Boolean?,
    modifier: Modifier = Modifier,
    size: Dp = 55.dp,
    shape: Shape = RoundedCornerShape(18.dp),
    elevation: Dp = 12.dp
) {
    val icon = when (gender) {
        true -> Icons.Filled.Man
        false -> Icons.Filled.Woman
        null -> Icons.Filled.Person
    }
    val bg = when (gender) {
        true -> MaleBg
        false -> FemaleBg
        null -> NeutralBg
    }
    val borderColor = when (gender) {
        true -> MaleBorder
        false -> FemaleBorder
        null -> NeutralBorder
    }

    Box(
        modifier = modifier
            .size(size)
            .shadow(elevation = elevation, shape = shape, clip = false)
            .clip(shape)
            .background(bg)
            .border(width = 2.dp, color = borderColor, shape = shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "avatar",
            tint = Color.White,
            modifier = Modifier.size(size * 0.65f)
        )
    }
}
