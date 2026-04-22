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
    private val onStepChange: (TutorialSegmentId, Int) -> Unit,
    private val onSegmentComplete: (TutorialSegmentId) -> Unit,
    private val onSegmentSkipped: (TutorialSegmentId) -> Unit
) {
    var currentSegment: TutorialSegmentId? by mutableStateOf(null)
        private set

    var currentStepIndex by mutableIntStateOf(0)
        private set

    var currentRoute: String? by mutableStateOf(null)
        private set

    val currentSteps: List<TutorialStep>
        get() = currentSegment?.let { tutorialSegments[it] } ?: emptyList()

    val currentStep: TutorialStep?
        get() = currentSteps.getOrNull(currentStepIndex)

    val isActive: Boolean
        get() {
            val step = currentStep ?: return false
            return stepMatchesRoute(step, currentRoute)
        }

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
            if (isWaitingForBounds && currentStep?.targetKey == key) {
                isWaitingForBounds = false
            }
        }
    }

    fun unregisterTarget(key: String) {
        _targetBounds.remove(key)
    }

    fun getLocalBounds(key: String): Rect? {
        val windowBounds = _targetBounds[key] ?: return null
        return windowBounds.translate(-overlayOffset.x, -overlayOffset.y)
    }

    fun startSegment(id: TutorialSegmentId, startIndex: Int = 0) {
        val steps = tutorialSegments[id] ?: return
        if (steps.isEmpty()) return
        val safeIndex = startIndex.coerceIn(0, steps.size - 1)
        currentSegment = id
        currentStepIndex = safeIndex
        _targetBounds.clear()
        val step = steps[safeIndex]
        isWaitingForBounds = step.expectsBounds() && stepMatchesRoute(step, currentRoute)
    }

    fun onRouteChanged(newRoute: String?) {
        currentRoute = newRoute
        val seg = currentSegment ?: return
        val steps = currentSteps
        if (steps.isEmpty()) return
        val cur = steps.getOrNull(currentStepIndex) ?: return
        if (stepMatchesRoute(cur, newRoute)) {
            isWaitingForBounds = cur.expectsBounds() && !_targetBounds.containsKey(cur.targetKey)
            return
        }
        for (i in (currentStepIndex + 1) until steps.size) {
            val s = steps[i]
            if (stepMatchesRoute(s, newRoute)) {
                currentStepIndex = i
                onStepChange(seg, i)
                isWaitingForBounds = s.expectsBounds() && !_targetBounds.containsKey(s.targetKey)
                return
            }
        }
        isWaitingForBounds = false
    }

    fun nextStep() {
        val seg = currentSegment ?: return
        val steps = currentSteps
        if (currentStepIndex >= steps.size - 1) {
            onSegmentComplete(seg)
            clearInternal()
            return
        }

        val nextIndex = currentStepIndex + 1
        val nextStep = steps[nextIndex]

        isWaitingForBounds = nextStep.expectsBounds() &&
            !_targetBounds.containsKey(nextStep.targetKey) &&
            stepMatchesRoute(nextStep, currentRoute)

        currentStepIndex = nextIndex
        onStepChange(seg, nextIndex)
    }

    fun notifyTargetClicked(key: String) {
        val step = currentStep ?: return
        if (!step.requiresInteraction) return
        if (step.targetKey != key) return
        if (!stepMatchesRoute(step, currentRoute)) return
        nextStep()
    }

    fun skip() {
        val seg = currentSegment ?: return
        onSegmentSkipped(seg)
        clearInternal()
    }

    fun clearSegment() {
        clearInternal()
    }

    private fun clearInternal() {
        currentSegment = null
        currentStepIndex = 0
        isWaitingForBounds = false
        _targetBounds.clear()
    }

    private fun stepMatchesRoute(step: TutorialStep, route: String?): Boolean {
        val stepRoute = step.route ?: return true
        val cur = route ?: return false
        return cur.startsWith(stepRoute)
    }
}
