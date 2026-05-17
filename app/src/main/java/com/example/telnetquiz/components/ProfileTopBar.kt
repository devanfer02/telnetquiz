package com.example.telnetquiz.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.telnetquiz.R
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.features.user.presentation.viewmodel.ProfileViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ProfileTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = LitecartesColor.Surface,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val profileState by profileViewModel.state.collectAsState()
    val selectedAvatarIndex by profileViewModel.selectedAvatarIndex.collectAsState()
    val tutorialController = LocalTutorialController.current
    var showTooltip by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .then(
                if (tutorialController != null) Modifier.onGloballyPositioned {
                    tutorialController.registerTarget("profile_top_bar", it)
                } else Modifier
            )
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(LitecartesColor.Secondary)
    ) {
        TopBarSparkleLayer()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            DashedAvatar(
                isLoading = profileState.isLoading,
                imageUrl = profileState.profile?.image,
                localAvatarResId = AvatarConstants.getAvatarResId(selectedAvatarIndex),
                gender = profileState.profile?.gender,
                nameSeed = profileState.profile?.fullname ?: ""
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "SELAMAT DATANG",
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp,
                    maxLines = 1
                )
                if (profileState.isLoading) {
                    SkeletonBox(height = 18.dp, width = 140.dp, cornerRadius = 6.dp, onPrimary = true)
                } else {
                    Text(
                        text = profileState.profile?.fullname ?: "Penjelajah",
                        color = Color.White,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (profileState.isLoading) {
                        SkeletonBox(height = 22.dp, width = 70.dp, cornerRadius = 12.dp, onPrimary = true)
                        SkeletonBox(height = 22.dp, width = 56.dp, cornerRadius = 12.dp, onPrimary = true)
                    } else {
                        ColoredPill(
                            iconRes = R.drawable.diamond,
                            value = "${profileState.profile?.stats?.totalScore ?: 0}",
                            background = Color(0xFFFFC93C)
                        )
                        ColoredPill(
                            iconRes = R.drawable.lightning,
                            value = "${profileState.profile?.stats?.dailyStreak ?: 0}",
                            background = Color(0xFFFF6F3C)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                LegendaButton(onClick = { showTooltip = !showTooltip })
                androidx.compose.animation.AnimatedVisibility(
                    visible = showTooltip,
                    enter = scaleIn(
                        animationSpec = tween(250),
                        transformOrigin = TransformOrigin(0.5f, 0f)
                    ) + fadeIn(tween(200)),
                    exit = scaleOut(
                        animationSpec = tween(150),
                        transformOrigin = TransformOrigin(0.5f, 0f)
                    ) + fadeOut(tween(100))
                ) {
                    Popup(
                        alignment = Alignment.BottomCenter,
                        onDismissRequest = { showTooltip = false },
                        properties = PopupProperties(focusable = true)
                    ) {
                        LegendaTooltip()
                    }
                }
            }
        }
    }
}

@Composable
private fun DashedAvatar(
    isLoading: Boolean,
    imageUrl: String?,
    localAvatarResId: Int?,
    gender: Boolean?,
    nameSeed: String
) {
    val size = 60.dp
    Box(
        modifier = Modifier.size(size + 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size + 8.dp)) {
            val strokeWidth = 1.5.dp.toPx()
            val inset = strokeWidth / 2f
            drawArc(
                color = Color.White.copy(alpha = 0.6f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - strokeWidth, this.size.height - strokeWidth),
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                )
            )
        }
        if (isLoading) {
            SkeletonBox(
                height = size,
                width = size,
                cornerRadius = size / 2,
                onPrimary = true
            )
        } else {
            Box(
                modifier = Modifier
                    .size(size)
                    .shadow(elevation = 6.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                AvatarImage(
                    imageUrl = imageUrl,
                    localAvatarResId = localAvatarResId,
                    gender = gender,
                    nameSeed = nameSeed,
                    shape = CircleShape,
                    modifier = Modifier.size(size - 6.dp)
                )
            }
        }
    }
}

@Composable
private fun ColoredPill(iconRes: Int, value: String, background: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = value,
            color = Color.White,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun LegendaButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                tint = LitecartesColor.Secondary,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = "LEGENDA",
            color = Color.White,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun LegendaTooltip() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(16.dp, 10.dp)
                .background(color = LitecartesColor.Secondary, shape = TriangleUpShape)
        )
        Row(
            modifier = Modifier
                .shadow(12.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(LitecartesColor.Secondary)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.quickcheck),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.diamond),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Skor total dari semua quiz",
                        fontFamily = nunitosFontFamily,
                        fontSize = 11.sp,
                        color = LitecartesColor.Surface
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.lightning),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Streak bermain harian",
                        fontFamily = nunitosFontFamily,
                        fontSize = 11.sp,
                        color = LitecartesColor.Surface
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.TopBarSparkleLayer() {
    SoftGlow(LitecartesColor.Primary.copy(alpha = 0.35f), 110.dp, (-30).dp, (-40).dp, Alignment.TopStart)
    SoftGlow(Color.White.copy(alpha = 0.16f), 130.dp, 40.dp, (-50).dp, Alignment.TopEnd)
    SoftGlow(LitecartesColor.Primary.copy(alpha = 0.22f), 90.dp, (-20).dp, 30.dp, Alignment.BottomStart)
    Sparkle(Alignment.TopStart, 90.dp, 14.dp, 11.dp)
    Sparkle(Alignment.TopEnd, (-40).dp, 28.dp, 8.dp)
    Sparkle(Alignment.BottomStart, 60.dp, (-18).dp, 9.dp)
    Sparkle(Alignment.BottomEnd, (-30).dp, (-10).dp, 12.dp)
}

@Composable
private fun BoxScope.SoftGlow(
    color: Color,
    size: Dp,
    offsetX: Dp,
    offsetY: Dp,
    alignment: Alignment
) {
    Box(
        modifier = Modifier
            .align(alignment)
            .offset(x = offsetX, y = offsetY)
            .size(size)
            .background(
                brush = Brush.radialGradient(colors = listOf(color, Color.Transparent)),
                shape = CircleShape
            )
    )
}

@Composable
private fun BoxScope.Sparkle(
    alignment: Alignment,
    offsetX: Dp,
    offsetY: Dp,
    size: Dp
) {
    Icon(
        imageVector = Icons.Filled.AutoAwesome,
        contentDescription = null,
        tint = Color.White.copy(alpha = 0.55f),
        modifier = Modifier
            .align(alignment)
            .offset(x = offsetX, y = offsetY)
            .size(size)
    )
}

private val TriangleUpShape = object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}

@Preview
@Composable
fun PreviewProfileTopBar() {
    ProfileTopBar()
}
