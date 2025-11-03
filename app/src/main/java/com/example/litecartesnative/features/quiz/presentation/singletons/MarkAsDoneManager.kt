package com.example.litecartesnative.features.quiz.presentation.singletons

import com.example.litecartesnative.constants.chaptersData

object MarkAsDoneManager {
    val levels: Array<BooleanArray> = Array(chaptersData.size) { chapterIndex ->

        val chapter = chaptersData[chapterIndex]
        BooleanArray(chapter.levels.size) { levelIndex ->
            false
        }
    }
}