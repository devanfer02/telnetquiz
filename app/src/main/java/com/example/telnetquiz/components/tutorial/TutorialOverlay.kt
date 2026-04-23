package com.example.telnetquiz.components.tutorial

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.R
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import androidx.compose.foundation.Canvas
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun TutorialOverlay(
    controller: TutorialController
) {
    if (!controller.isActive) return

    val step = controller.currentStep ?: return
    val localBounds = if (step.inPopup) null else step.targetKey?.let { controller.getLocalBounds(it) }
    val density = LocalDensity.current
    val paddingPx = with(density) { 8.dp.toPx() }

    val pulseTransition = rememberInfiniteTransition(label = "tutorial_pulse")
    val pulsePadding by pulseTransition.animateFloat(
        initialValue = 0f,
        targetValue = with(density) { 6.dp.toPx() },
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tutorial_pulse_padding"
    )

    val allowPassthrough = step.requiresInteraction && localBounds != null && !controller.isWaitingForBounds
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { controller.registerOverlay(it) }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        ) {
            drawRect(Color.Black.copy(alpha = 0.75f))

            if (localBounds != null && !controller.isWaitingForBounds) {
                val extraPad = if (step.requiresInteraction) pulsePadding else 0f
                drawSpotlight(localBounds, paddingPx + extraPad)
            }
        }

        if (allowPassthrough && localBounds != null) {
            val holeRect = Rect(
                left = localBounds.left - paddingPx,
                top = localBounds.top - paddingPx,
                right = localBounds.right + paddingPx,
                bottom = localBounds.bottom + paddingPx
            )
            SpotlightBlockers(hole = holeRect)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = true)
                            down.consume()
                            while (true) {
                                val event = awaitPointerEvent()
                                event.changes.forEach { if (it.pressed) it.consume() }
                                if (event.changes.all { !it.pressed }) break
                            }
                        }
                    }
            )
        }

        if (!controller.isWaitingForBounds) {
            TooltipCard(
                step = step,
                stepIndex = controller.currentStepIndex,
                totalSteps = controller.currentSteps.size,
                targetBounds = localBounds,
                onNext = { controller.nextStep() },
                onPrev = { controller.previousStep() },
                onSkipStep = { controller.skipCurrentStep() },
                onSkipTutorial = { controller.skip() }
            )
        }
    }
}

@Composable
private fun SpotlightBlockers(hole: Rect) {
    Layout(
        modifier = Modifier.fillMaxSize(),
        content = {
            repeat(4) {
                Box(
                    modifier = Modifier.pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = true)
                            down.consume()
                            while (true) {
                                val event = awaitPointerEvent()
                                event.changes.forEach { if (it.pressed) it.consume() }
                                if (event.changes.all { !it.pressed }) break
                            }
                        }
                    }
                )
            }
        }
    ) { measurables, constraints ->
        val w = constraints.maxWidth
        val h = constraints.maxHeight
        val holeLeft = hole.left.toInt().coerceIn(0, w)
        val holeTop = hole.top.toInt().coerceIn(0, h)
        val holeRight = hole.right.toInt().coerceIn(0, w)
        val holeBottom = hole.bottom.toInt().coerceIn(0, h)
        val bandHeight = (holeBottom - holeTop).coerceAtLeast(0)

        val top = measurables[0].measure(Constraints.fixed(w, holeTop))
        val bottom = measurables[1].measure(Constraints.fixed(w, (h - holeBottom).coerceAtLeast(0)))
        val left = measurables[2].measure(Constraints.fixed(holeLeft, bandHeight))
        val right = measurables[3].measure(Constraints.fixed((w - holeRight).coerceAtLeast(0), bandHeight))

        layout(w, h) {
            top.place(0, 0)
            bottom.place(0, holeBottom)
            left.place(0, holeTop)
            right.place(holeRight, holeTop)
        }
    }
}

private fun DrawScope.drawSpotlight(bounds: Rect, padding: Float) {
    val expandedBounds = Rect(
        left = bounds.left - padding,
        top = bounds.top - padding,
        right = bounds.right + padding,
        bottom = bounds.bottom + padding
    )
    drawRoundRect(
        color = Color.Transparent,
        topLeft = Offset(expandedBounds.left, expandedBounds.top),
        size = Size(expandedBounds.width, expandedBounds.height),
        cornerRadius = CornerRadius(16f, 16f),
        blendMode = BlendMode.Clear
    )
}

@Composable
private fun TooltipCard(
    step: TutorialStep,
    stepIndex: Int,
    totalSteps: Int,
    targetBounds: Rect?,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSkipStep: () -> Unit,
    onSkipTutorial: () -> Unit
) {
    val density = LocalDensity.current

    val containerModifier = when {
        step.tooltipPosition == TooltipPosition.ABOVE_TARGET && targetBounds != null -> {
            val constrainedHeight = with(density) { (targetBounds.top - 16.dp.toPx()).toDp() }
                .coerceAtLeast(100.dp)
            Modifier
                .fillMaxWidth()
                .height(constrainedHeight)
                .padding(horizontal = 20.dp)
        }
        step.tooltipPosition == TooltipPosition.BELOW_TARGET && targetBounds != null -> {
            val yOffset = with(density) { (targetBounds.bottom + 16.dp.toPx()).toInt() }
            Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, yOffset) }
                .padding(horizontal = 20.dp)
        }
        else -> {
            Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        }
    }

    Box(
        modifier = containerModifier,
        contentAlignment = when (step.tooltipPosition) {
            TooltipPosition.ABOVE_TARGET -> Alignment.BottomCenter
            TooltipPosition.CENTER_SCREEN -> Alignment.Center
            else -> Alignment.TopStart
        }
    ) {
        AnimatedContent(
            targetState = stepIndex,
            transitionSpec = {
                (fadeIn(tween(200)) + slideInVertically(tween(250)) { it / 4 })
                    .togetherWith(fadeOut(tween(150)) + slideOutVertically(tween(200)) { -it / 4 })
            },
            label = "tutorial_step"
        ) { _ ->
            Column(
                modifier = Modifier
                    .shadow(12.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(LitecartesColor.Secondary)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = step.mascotResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = step.title,
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = LitecartesColor.Surface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${stepIndex + 1} / $totalSteps",
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = LitecartesColor.Surface.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = step.description,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = LitecartesColor.Surface,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                val isLast = stepIndex >= totalSteps - 1
                val isFirst = stepIndex == 0

                if (step.requiresInteraction && !isLast) {
                    Text(
                        text = if (step.inPopup) "Ikuti instruksi di atas" else "Tap area yang disorot",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = LitecartesColor.Surface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                val hideNext = step.requiresInteraction && !step.inPopup && !isLast
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onPrev,
                        enabled = !isFirst
                    ) {
                        Text(
                            text = "< Sebelum",
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = LitecartesColor.Surface.copy(
                                alpha = if (isFirst) 0.3f else 0.85f
                            )
                        )
                    }

                    if (!hideNext) {
                        TextButton(
                            onClick = onNext,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(LitecartesColor.Surface)
                                .padding(horizontal = 12.dp)
                        ) {
                            Text(
                                text = when {
                                    isFirst -> "Mulai"
                                    isLast -> "Selesai"
                                    else -> "Sesudah >"
                                },
                                fontFamily = nunitosFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = LitecartesColor.Secondary
                            )
                        }
                    }
                }

                if (!isLast) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onSkipStep) {
                            Text(
                                text = "Lewati langkah",
                                fontFamily = nunitosFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp,
                                color = LitecartesColor.Surface.copy(alpha = 0.55f)
                            )
                        }
                        TextButton(onClick = onSkipTutorial) {
                            Text(
                                text = "Lewati tutorial",
                                fontFamily = nunitosFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp,
                                color = LitecartesColor.Surface.copy(alpha = 0.55f)
                            )
                        }
                    }
                }
            }
        }
    }
}
