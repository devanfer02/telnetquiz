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
    val route: String? = null,
    val requiresInteraction: Boolean = false,
    val inPopup: Boolean = false
)

internal fun TutorialStep.expectsBounds(): Boolean = targetKey != null && !inPopup

enum class TutorialStepId {
    WELCOME,
    PROFILE_TOP_BAR,
    CHAPTER_CARD,
    LEVEL_KKM,
    LEVEL_BUTTON_LOCKED,
    LEVEL_BUTTON_FIRST,
    DIALOG_LEARN_FIRST,
    STUDY_INTRO,
    STUDY_AUDIO,
    STUDY_NEXT,
    STUDY_PREV,
    STUDY_EXIT,
    STUDY_FINAL,
    QUIZ_INTRO,
    QUIZ_OPTIONS,
    QUIZ_VERIFY,
    QUIZ_FEEDBACK_INFO,
    REMEDIAL_CTA,
    REMEDIAL_STUDY,
    RETRY_OPTIONS,
    RETRY_VERIFY,
    RESULT_SCORE,
    RESULT_KKM_NOTE,
    RESULT_CONTINUE,
    NAV_LEADERBOARD,
    LEADERBOARD_INTRO,
    NAV_PROFILE,
    PROFILE_STATS,
    PROFILE_ACHIEVEMENTS,
    PROFILE_EDIT,
    EDIT_INFO,
    PROFILE_SETTINGS,
    FINAL_CONGRATS
}

enum class TooltipPosition {
    BELOW_TARGET,
    ABOVE_TARGET,
    CENTER_SCREEN
}

enum class TutorialSegmentId {
    MAIN
}

fun TutorialSegmentId.allowedRoutePrefixes(): List<String> = when (this) {
    TutorialSegmentId.MAIN -> listOf(
        Screen.HomeScreen.route,
        Screen.LevelScreen.route,
        Screen.StudyMaterialScreen.route,
        Screen.QuestionScreen.route,
        Screen.RemedialScreen.route,
        Screen.ResultScreen.route,
        Screen.LeaderboardScreen.route,
        Screen.ProfileScreen.route,
        Screen.EditProfileScreen.route
    )
}

private val mainSteps = listOf(
    TutorialStep(
        id = TutorialStepId.WELCOME,
        title = "Selamat Datang!",
        description = "Halo, Penjelajah! Yuk kenalan dulu sama TelNetQuiz. Ikuti panduan ini sampai selesai ya!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.group_275,
        route = Screen.HomeScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.PROFILE_TOP_BAR,
        title = "Profil & Statistik",
        description = "Ini informasi profilmu: foto, nama, sekolah, skor total, streak harian, dan gelar bermainmu.",
        targetKey = "profile_top_bar",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.HomeScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.CHAPTER_CARD,
        title = "Daftar Bab",
        description = "Ini daftar bab materi. Ayo ketuk tombol \"Yuk Main\" untuk masuk ke level-levelnya!",
        targetKey = "chapter_card_first",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.mascot_wrong,
        route = Screen.HomeScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.LEVEL_KKM,
        title = "Skor Minimum (KKM)",
        description = "Ini KKM bab ini — skor minimum yang harus kamu capai untuk membuka level berikutnya.",
        targetKey = "level_kkm_display",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.LevelScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.LEVEL_BUTTON_LOCKED,
        title = "Level Terkunci",
        description = "Level ini terkunci. Bakal kebuka setelah kamu menyelesaikan level sebelumnya dan lulus KKM.",
        targetKey = "level_button_locked",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.mascot_wrong,
        route = Screen.LevelScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.LEVEL_BUTTON_FIRST,
        title = "Mulai Level 1",
        description = "Ayo tekan tombol level 1 untuk mulai petualanganmu!",
        targetKey = "level_button_first",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.LevelScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.DIALOG_LEARN_FIRST,
        title = "Belajar Dulu",
        description = "Pilih \"Belajar Dulu\" biar kamu paham materinya sebelum mengerjakan kuis.",
        targetKey = "dialog_learn_first_btn",
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        route = Screen.LevelScreen.route,
        requiresInteraction = true,
        inPopup = true
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_INTRO,
        title = "Ruang Belajar",
        description = "Selamat datang di ruang belajar! Pelajari materi di sini sebelum kuis dimulai.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.group_275,
        route = Screen.StudyMaterialScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_AUDIO,
        title = "Putar Audio",
        description = "Tekan tombol ini buat dengerin materinya. Tekan lagi buat berhenti.",
        targetKey = "study_audio_btn",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.StudyMaterialScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_NEXT,
        title = "Lanjut Materi",
        description = "Tombol ini buat lanjut ke materi berikutnya. Di materi terakhir, tombolnya berubah jadi \"Mulai Kuis\".",
        targetKey = "study_next_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        route = Screen.StudyMaterialScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_PREV,
        title = "Materi Sebelumnya",
        description = "Kalau mau baca ulang, tekan tombol \"Sebelumnya\" yang muncul mulai materi ke-2.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        route = Screen.StudyMaterialScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_EXIT,
        title = "Keluar Belajar",
        description = "Mau keluar? Tekan tombol kembali di HP, atau geser dari tepi layar (tergantung mode navigasi HP-mu). Akan muncul konfirmasi biar nggak keluar nggak sengaja.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.mascot_wrong,
        route = Screen.StudyMaterialScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_FINAL,
        title = "Baca Sampai Selesai",
        description = "Lanjutkan membaca sampai kuis dimulai. Saat kuis muncul, aku akan lanjutkan panduannya.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.chap1,
        route = Screen.StudyMaterialScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.QUIZ_INTRO,
        title = "Waktunya Kuis!",
        description = "Saatnya menguji pemahaman. Jawab setiap soal dengan teliti ya!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.group_275,
        route = Screen.QuestionScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.QUIZ_OPTIONS,
        title = "Pilih Jawaban",
        description = "Ayo pilih salah satu opsi jawaban. Tekan opsi yang kamu anggap benar.",
        targetKey = "quiz_option_first",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.QuestionScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.QUIZ_VERIFY,
        title = "Konfirmasi Jawaban",
        description = "Sudah yakin? Tekan tombol ini untuk memeriksa jawabanmu.",
        targetKey = "quiz_verify_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        route = Screen.QuestionScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.QUIZ_FEEDBACK_INFO,
        title = "Umpan Balik",
        description = "Opsi salah jadi merah, benar jadi hijau. Kalau masih ada yang salah, kamu akan diarahkan ke halaman Belajar Lagi untuk review materinya.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        route = Screen.QuestionScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.REMEDIAL_CTA,
        title = "Ayo Belajar Lagi!",
        description = "Tekan tombol ini buat baca ulang materi dari soal yang kamu jawab salah.",
        targetKey = "remedial_cta_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        mascotResId = R.drawable.mascot_wrong,
        route = Screen.RemedialScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.REMEDIAL_STUDY,
        title = "Review Materi",
        description = "Baca ulang materi ini dengan teliti. Setelah selesai, kamu bakal dapet kesempatan satu kali lagi untuk menjawab.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        route = Screen.StudyMaterialScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.RETRY_OPTIONS,
        title = "Coba Lagi",
        description = "Pilih jawabanmu lagi. Kali ini pasti bisa!",
        targetKey = "quiz_option_first",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.QuestionScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.RETRY_VERIFY,
        title = "Konfirmasi Lagi",
        description = "Tekan konfirmasi untuk memeriksa jawaban barunya.",
        targetKey = "quiz_verify_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        route = Screen.QuestionScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.RESULT_SCORE,
        title = "Hasil Kuis",
        description = "Skor dan jumlah jawaban benar/salah kamu tampil di sini. Kalau skormu ≥ KKM, kamu lulus!",
        targetKey = "result_score_section",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.ResultScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.RESULT_KKM_NOTE,
        title = "Kunci Membuka Level",
        description = "Ingat: untuk membuka level berikutnya kamu harus MENYELESAIKAN level ini DAN mencapai skor minimum (KKM).",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.chap1,
        route = Screen.ResultScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.RESULT_CONTINUE,
        title = "Lanjutkan",
        description = "Tekan \"Lanjutkan\" untuk kembali ke peta level.",
        targetKey = "result_continue_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        route = Screen.ResultScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.NAV_LEADERBOARD,
        title = "Papan Peringkat",
        description = "Tekan ikon piala di navbar untuk lihat peringkat pemain.",
        targetKey = "navbar_leaderboard",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        route = Screen.LevelScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.LEADERBOARD_INTRO,
        title = "Aktivitas & Ranking",
        description = "Ada dua tab: \"Aktivitas Harian\" (riwayat kuis) dan \"Papan Peringkat\" (ranking pemain). Tiga teratas tampil di podium.",
        targetKey = "leaderboard_tab_toggle",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.LeaderboardScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.NAV_PROFILE,
        title = "Halaman Profil",
        description = "Tekan ikon profil di navbar untuk buka halaman profilmu.",
        targetKey = "navbar_profile",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        route = Screen.LeaderboardScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.PROFILE_STATS,
        title = "Statistik Main",
        description = "Total skor, jumlah level selesai, dan jumlah bab selesai kamu tampil di sini.",
        targetKey = "profile_stats",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.ProfileScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.PROFILE_ACHIEVEMENTS,
        title = "Pencapaian",
        description = "Daftar pencapaianmu. Selesaikan kuis terus buat mengumpulkan yang lain!",
        targetKey = "profile_achievements",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.ProfileScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.PROFILE_EDIT,
        title = "Edit Profil",
        description = "Tekan ikon pensil di header buat edit foto profil, nama, dan bio-mu.",
        targetKey = "profile_header",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.ProfileScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.EDIT_INFO,
        title = "Halaman Edit Profil",
        description = "Di sini kamu bisa ganti avatar, nama lengkap, dan bio. Tekan \"Simpan\" kalau sudah selesai, lalu kembali ke Profil.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        route = Screen.EditProfileScreen.route
    ),
    TutorialStep(
        id = TutorialStepId.PROFILE_SETTINGS,
        title = "Pengaturan",
        description = "Di sini kamu bisa atur suara, ulangi tutorial, lihat panduan umum, atau keluar akun.",
        targetKey = "profile_settings_btn",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        route = Screen.ProfileScreen.route,
        requiresInteraction = true
    ),
    TutorialStep(
        id = TutorialStepId.FINAL_CONGRATS,
        title = "Selamat, Penjelajah!",
        description = "Tutorial selesai! Sekarang kamu sudah siap menjelajahi TelNetQuiz sepenuhnya. Selamat belajar!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.chap1,
        route = Screen.ProfileScreen.route
    )
)

val tutorialSegments: Map<TutorialSegmentId, List<TutorialStep>> = mapOf(
    TutorialSegmentId.MAIN to mainSteps
)
