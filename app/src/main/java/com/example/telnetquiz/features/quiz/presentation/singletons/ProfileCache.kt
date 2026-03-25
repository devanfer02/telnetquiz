package com.example.telnetquiz.features.quiz.presentation.singletons

import com.example.telnetquiz.data.remote.dto.UserProfileDto

object ProfileCache {
    var profile: UserProfileDto? = null

    fun getTag(): String {
        val completed = profile?.stats?.chaptersCompleted ?: 0
        val total = profile?.stats?.totalChapters ?: 0
        if (total == 0) return "Penjelajah"
        val pct = (completed.toDouble() / total) * 100
        return when {
            pct >= 90 -> "Legenda"
            pct >= 50 -> "Veteran"
            pct >= 25 -> "Amatir"
            else -> "Penjelajah"
        }
    }

    fun clear() {
        profile = null
    }
}
