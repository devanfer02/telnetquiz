package com.example.telnetquiz.features.quiz.presentation.singletons

import com.example.telnetquiz.data.remote.dto.StudyMaterialDto

object LearnFirstHolder {
    var quizId: Int = 0
    var chapterId: Int = 0
    var level: Int = 0
    private var materials: List<StudyMaterialDto> = emptyList()
    private var currentIndex: Int = 0

    fun setup(quizId: Int, chapterId: Int, level: Int, materials: List<StudyMaterialDto>) {
        this.quizId = quizId
        this.chapterId = chapterId
        this.level = level
        this.materials = materials
        this.currentIndex = 0
    }

    fun isActive(): Boolean = quizId > 0

    fun hasNext(): Boolean = currentIndex < materials.size

    fun next(): StudyMaterialDto? {
        if (currentIndex >= materials.size) return null
        val material = materials[currentIndex]
        currentIndex++
        return material
    }

    fun clear() {
        quizId = 0
        chapterId = 0
        level = 0
        materials = emptyList()
        currentIndex = 0
    }
}
