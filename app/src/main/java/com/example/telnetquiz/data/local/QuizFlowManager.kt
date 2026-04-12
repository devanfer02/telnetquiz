package com.example.telnetquiz.data.local

import com.example.telnetquiz.data.remote.dto.QuizDto
import com.example.telnetquiz.data.remote.dto.StudyMaterialDto
import com.example.telnetquiz.features.quiz.domain.model.QuizIndex
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizFlowManager @Inject constructor() {

    // --- Remedial flow state (was RemedialHolder) ---
    var remedialQuizId: Int = 0
        private set
    var remedialQuizData: QuizDto? = null
        private set
    var wrongQuestionIds: List<Int> = emptyList()
        private set
    var correctAnswers: Map<Int, Int> = emptyMap()
        private set
    var isRetry: Boolean = false
        private set
    var materialsCache: Map<Int, StudyMaterialDto> = emptyMap()
        private set

    // --- Wrong question queue (was WrongQuizManager) ---
    val wrongQueue: ArrayDeque<QuizIndex> = ArrayDeque()

    // --- Learn-first flow state (was LearnFirstHolder) ---
    var learnFirstQuizId: Int = 0
        private set
    var learnFirstChapterId: Int = 0
        private set
    var learnFirstLevel: Int = 0
        private set
    private var learnFirstMaterials: List<StudyMaterialDto> = emptyList()
    private var learnFirstIndex: Int = 0

    fun setupRemedial(
        quizId: Int,
        quizData: QuizDto,
        wrongIds: List<Int>,
        correctAnswerMap: Map<Int, Int>
    ) {
        remedialQuizId = quizId
        remedialQuizData = quizData
        wrongQuestionIds = wrongIds
        correctAnswers = correctAnswerMap
        isRetry = false
    }

    fun setMaterialsCache(cache: Map<Int, StudyMaterialDto>) {
        materialsCache = cache
    }

    fun markAsRetry() {
        isRetry = true
    }

    fun setupLearnFirst(
        quizId: Int,
        chapterId: Int,
        level: Int,
        materials: List<StudyMaterialDto>
    ) {
        learnFirstQuizId = quizId
        learnFirstChapterId = chapterId
        learnFirstLevel = level
        learnFirstMaterials = materials
        learnFirstIndex = 0
    }

    val currentMaterialIndex: Int get() = learnFirstIndex
    val totalMaterials: Int get() = learnFirstMaterials.size

    fun isLearnFirstActive(): Boolean = learnFirstQuizId > 0

    fun hasNextMaterial(): Boolean = learnFirstIndex < learnFirstMaterials.size

    fun nextMaterial(): StudyMaterialDto? {
        if (learnFirstIndex >= learnFirstMaterials.size) return null
        val material = learnFirstMaterials[learnFirstIndex]
        learnFirstIndex++
        return material
    }

    fun previousMaterial(): StudyMaterialDto? {
        if (learnFirstIndex <= 1) return null
        learnFirstIndex -= 2
        val material = learnFirstMaterials[learnFirstIndex]
        learnFirstIndex++
        return material
    }

    fun resetWrongQueue() {
        wrongQueue.clear()
    }

    fun clearRemedial() {
        remedialQuizId = 0
        remedialQuizData = null
        wrongQuestionIds = emptyList()
        correctAnswers = emptyMap()
        isRetry = false
        materialsCache = emptyMap()
    }

    fun clearLearnFirst() {
        learnFirstQuizId = 0
        learnFirstChapterId = 0
        learnFirstLevel = 0
        learnFirstMaterials = emptyList()
        learnFirstIndex = 0
    }

    fun clearAll() {
        clearRemedial()
        clearLearnFirst()
        resetWrongQueue()
    }
}
