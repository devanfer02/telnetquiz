package com.example.telnetquiz.components.tutorial

import com.example.telnetquiz.R
import com.example.telnetquiz.constants.Screen

data class TutorialStep(
    val id: TutorialStepId,
    val title: String,
    val description: String,
    val targetKey: String?,
    val tooltipPosition: TooltipPosition,
    val mascotResId: Int = R.drawable.group_276,
    val navigateTo: String? = null
)

enum class TutorialStepId {
    WELCOME,
    PROFILE_HEADER,
    CHAPTER_CARDS,
    BOTTOM_NAVBAR,
    LEVEL_ROAD,
    LEVEL_ACTION,
    FINISH
}

enum class TooltipPosition {
    BELOW_TARGET,
    ABOVE_TARGET,
    CENTER_SCREEN
}

val tutorialSteps = listOf(
    TutorialStep(
        id = TutorialStepId.WELCOME,
        title = "Selamat Datang!",
        description = "Halo! Selamat datang di TelNetQuiz. Yuk, kenalan dulu sama fitur-fiturnya!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.group_275
    ),
    TutorialStep(
        id = TutorialStepId.PROFILE_HEADER,
        title = "Profil & Statistik",
        description = "Di sini kamu bisa lihat foto profil, nama, sekolah, skor total, streak harian, dan gelar bermainmu.",
        targetKey = "profile_top_bar",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.CHAPTER_CARDS,
        title = "Daftar Bab",
        description = "Ini adalah daftar bab materi yang bisa kamu pelajari. Ketuk bab untuk masuk ke level-levelnya.",
        targetKey = "chapter_card_first",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.mascot_wrong
    ),
    TutorialStep(
        id = TutorialStepId.BOTTOM_NAVBAR,
        title = "Menu Navigasi",
        description = "Gunakan menu ini untuk berpindah antara Beranda, Peringkat, dan Profil.",
        targetKey = "bottom_navbar",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.LEVEL_ROAD,
        title = "Level dalam Bab",
        description = "Ini adalah level-level dalam bab. Selesaikan dari bawah ke atas untuk membuka level berikutnya!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.mascot_wrong,
        navigateTo = "${Screen.LevelScreen.route}/1"
    ),
    TutorialStep(
        id = TutorialStepId.LEVEL_ACTION,
        title = "Pilihan Belajar",
        description = "Ketuk level untuk memulai. Kamu bisa pilih \"Belajar Dulu\" untuk mempelajari materi, atau \"Langsung Main\" untuk langsung bermain quiz.",
        targetKey = "level_button_first",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.FINISH,
        title = "Siap Bermain!",
        description = "Tutorial selesai! Sekarang kamu siap menjelajahi dunia TelNetQuiz. Ayo mulai!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.chap1,
        navigateTo = Screen.HomeScreen.route
    )
)
