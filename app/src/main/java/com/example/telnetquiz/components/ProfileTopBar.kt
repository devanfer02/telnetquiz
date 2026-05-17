package com.example.telnetquiz.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.telnetquiz.R
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.features.user.presentation.viewmodel.ProfileViewModel
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import androidx.compose.ui.layout.onGloballyPositioned

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
    val tag by profileViewModel.tag.collectAsState()
    val tutorialController = LocalTutorialController.current

    TopBarContainer(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .then(
                if (tutorialController != null) Modifier.onGloballyPositioned {
                    tutorialController.registerTarget("profile_top_bar", it)
                } else Modifier
            ),
        cornerRadius = 24.dp,
        backgroundColor = LitecartesColor.Secondary
    ) {
        TopBarSparkleLayer()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 18.dp)
        ) {
            if (profileState.isLoading) {
                SkeletonBox(
                    height = 55.dp,
                    width = 55.dp,
                    cornerRadius = 18.dp,
                    onPrimary = true
                )
            } else {
                AvatarImage(
                    imageUrl = profileState.profile?.image,
                    localAvatarResId = AvatarConstants.getAvatarResId(selectedAvatarIndex),
                    gender = profileState.profile?.gender,
                    nameSeed = profileState.profile?.fullname ?: "",
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .height(55.dp)
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            if (profileState.isLoading) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    SkeletonBox(height = 18.dp, width = 100.dp, cornerRadius = 6.dp, onPrimary = true)
                    Spacer(modifier = Modifier.height(4.dp))
                    SkeletonBox(height = 10.dp, width = 70.dp, onPrimary = true)
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = profileState?.profile?.fullname ?: "",
                        fontFamily = nunitosFontFamily,
                        color = LitecartesColor.Surface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = profileState.profile?.school?.name ?: "",
                        fontFamily = nunitosFontFamily,
                        color = LitecartesColor.Surface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp
                    )
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp
                    )
            ) {
                if (profileState.isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SkeletonBox(height = 22.dp, cornerRadius = 12.dp, onPrimary = true, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        SkeletonBox(height = 22.dp, cornerRadius = 12.dp, onPrimary = true, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    SkeletonBox(height = 20.dp, cornerRadius = 12.dp, onPrimary = true, modifier = Modifier.fillMaxWidth())
                } else {
                    var showTooltip by remember { mutableStateOf(false) }

                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTooltip = !showTooltip },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BoxPoints(
                                modifier = Modifier.weight(1f),
                                imageId = R.drawable.diamond,
                                points = "${ profileState.profile?.stats?.totalScore ?: 0 }",
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                            BoxPoints(
                                modifier = Modifier.weight(1f),
                                imageId = R.drawable.lightning,
                                points = "${profileState.profile?.stats?.dailyStreak ?: 0}"
                            )
                        }
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
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        // Arrow pointing up
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp, 10.dp)
                                                .background(
                                                    color = LitecartesColor.Secondary,
                                                    shape = TriangleUpShape
                                                )
                                        )
                                        // Speech bubble with mascot
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
                            }
                        }
                    Spacer(modifier = Modifier.padding(2.dp))
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(20.dp),
                                clip = false
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(LitecartesColor.Surface)
                            .fillMaxWidth()
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tag,
                            fontFamily = nunitosFontFamily,
                            color = LitecartesColor.Secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBarSparkleLayer() {
    Box(modifier = Modifier.fillMaxSize()) {
        SoftGlow(LitecartesColor.Primary.copy(alpha = 0.35f), 110.dp, (-30).dp, (-40).dp, Alignment.TopStart)
        SoftGlow(Color.White.copy(alpha = 0.16f), 130.dp, 40.dp, (-50).dp, Alignment.TopEnd)
        SoftGlow(LitecartesColor.Primary.copy(alpha = 0.22f), 90.dp, (-20).dp, 30.dp, Alignment.BottomStart)
        Sparkle(Alignment.TopStart, 90.dp, 14.dp, 11.dp)
        Sparkle(Alignment.TopEnd, (-40).dp, 28.dp, 8.dp)
        Sparkle(Alignment.BottomStart, 60.dp, (-18).dp, 9.dp)
        Sparkle(Alignment.BottomEnd, (-30).dp, (-10).dp, 12.dp)
    }
}

@Composable
private fun BoxScope.SoftGlow(
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    offsetX: androidx.compose.ui.unit.Dp,
    offsetY: androidx.compose.ui.unit.Dp,
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
    offsetX: androidx.compose.ui.unit.Dp,
    offsetY: androidx.compose.ui.unit.Dp,
    size: androidx.compose.ui.unit.Dp
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