package com.example.telnetquiz.components.tutorial

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.positionInWindow

val LocalTutorialController = staticCompositionLocalOf<TutorialController?> { null }

@Stable
class TutorialController(
    private val onComplete: () -> Unit,
    private val onNavigate: (String) -> Unit
) {
    var currentStepIndex by mutableIntStateOf(0)
        private set

    val currentStep: TutorialStep
        get() = tutorialSteps[currentStepIndex.coerceIn(tutorialSteps.indices)]

    val isActive: Boolean
        get() = currentStepIndex in tutorialSteps.indices

    var isWaitingForBounds by mutableStateOf(false)
        private set

    private val _targetBounds = mutableStateMapOf<String, Rect>()
    val targetBounds: Map<String, Rect> get() = _targetBounds

    var overlayOffset by mutableStateOf(Offset.Zero)

    fun registerOverlay(coordinates: LayoutCoordinates) {
        if (coordinates.isAttached) {
            overlayOffset = coordinates.positionInWindow()
        }
    }

    fun registerTarget(key: String, coordinates: LayoutCoordinates) {
        if (coordinates.isAttached) {
            _targetBounds[key] = coordinates.boundsInWindow()
            if (isWaitingForBounds && currentStep.targetKey == key) {
                isWaitingForBounds = false
            }
        }
    }

    fun getLocalBounds(key: String): Rect? {
        val windowBounds = _targetBounds[key] ?: return null
        return windowBounds.translate(-overlayOffset.x, -overlayOffset.y)
    }

    fun nextStep() {
        if (currentStepIndex >= tutorialSteps.size - 1) {
            currentStepIndex = tutorialSteps.size
            onComplete()
            return
        }

        val nextIndex = currentStepIndex + 1
        val nextStep = tutorialSteps[nextIndex]

        if (nextStep.navigateTo != null) {
            _targetBounds.clear()
            isWaitingForBounds = nextStep.targetKey != null
            onNavigate(nextStep.navigateTo)
        }

        currentStepIndex = nextIndex
    }

    fun skip() {
        currentStepIndex = tutorialSteps.size
        onComplete()
    }
}
