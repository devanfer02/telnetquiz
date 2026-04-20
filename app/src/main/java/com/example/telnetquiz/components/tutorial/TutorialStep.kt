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
    // HOME_INTRO
    WELCOME,
    PROFILE_HEADER,
    CHAPTER_CARDS,
    BOTTOM_NAVBAR,
    LEVEL_ROAD,
    LEVEL_ACTION,
    FINISH,

    // STUDY_MATERIAL_INTRO
    STUDY_WELCOME,
    STUDY_AUDIO,
    STUDY_NEXT,
    STUDY_PREV,
    STUDY_EXIT,
    STUDY_CLOSING,

    // QUIZ_INTRO
    QUIZ_WELCOME,
    QUIZ_OPTIONS,
    QUIZ_VERIFY,
    QUIZ_FEEDBACK,
    QUIZ_EXIT,

    // REMEDIAL_INTRO
    REMEDIAL_WELCOME,
    REMEDIAL_CTA,

    // PROFILE_INTRO
    PROFILE_HEADER_INFO,
    PROFILE_STATS,
    PROFILE_ACHIEVEMENTS,
    PROFILE_LOGOUT,

    // EDIT_PROFILE_INTRO
    EDIT_AVATAR,
    EDIT_FULLNAME,
    EDIT_BIO,
    EDIT_SAVE,

    // LEADERBOARD_INTRO
    LEADERBOARD_TABS,
    LEADERBOARD_PODIUM,
    LEADERBOARD_ACTIVITY
}

enum class TooltipPosition {
    BELOW_TARGET,
    ABOVE_TARGET,
    CENTER_SCREEN
}

enum class TutorialSegmentId {
    HOME_INTRO,
    STUDY_MATERIAL_INTRO,
    QUIZ_INTRO,
    REMEDIAL_INTRO,
    PROFILE_INTRO,
    EDIT_PROFILE_INTRO,
    LEADERBOARD_INTRO
}

fun TutorialSegmentId.allowedRoutePrefixes(): List<String> = when (this) {
    TutorialSegmentId.HOME_INTRO -> listOf(Screen.HomeScreen.route, Screen.LevelScreen.route)
    TutorialSegmentId.STUDY_MATERIAL_INTRO -> listOf(Screen.StudyMaterialScreen.route)
    TutorialSegmentId.QUIZ_INTRO -> listOf(Screen.QuestionScreen.route)
    TutorialSegmentId.REMEDIAL_INTRO -> listOf(Screen.RemedialScreen.route)
    TutorialSegmentId.PROFILE_INTRO -> listOf(Screen.ProfileScreen.route)
    TutorialSegmentId.EDIT_PROFILE_INTRO -> listOf(Screen.EditProfileScreen.route)
    TutorialSegmentId.LEADERBOARD_INTRO -> listOf(Screen.LeaderboardScreen.route)
}

private val homeIntroSteps = listOf(
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
        description = "Ketuk level untuk memulai. Akan muncul dua pilihan: \"Belajar Dulu\" (pelajari materi) atau \"Langsung Main\" (langsung kuis). Kami sarankan \"Belajar Dulu\" biar kamu paham materinya.",
        targetKey = "level_button_first",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.FINISH,
        title = "Siap Bermain!",
        description = "Tutorial pembuka selesai! Nanti kamu bakal dapet panduan tambahan saat masuk ke ruang belajar, kuis, dan halaman lain. Ayo mulai!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.chap1
    )
)

private val studyMaterialIntroSteps = listOf(
    TutorialStep(
        id = TutorialStepId.STUDY_WELCOME,
        title = "Ruang Belajar",
        description = "Selamat datang di ruang belajar! Di sini kamu bisa pelajari materi sebelum kuis dimulai.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.group_275
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_AUDIO,
        title = "Tombol Audio",
        description = "Ketuk tombol ini buat dengerin materinya dengan suara. Ketuk lagi buat matiin audio.",
        targetKey = "study_audio_btn",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_NEXT,
        title = "Lanjut Materi",
        description = "Tekan tombol ini buat lanjut ke materi berikutnya. Saat materi terakhir, tombolnya berubah jadi \"Mulai Kuis\".",
        targetKey = "study_next_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_PREV,
        title = "Kembali ke Materi Sebelumnya",
        description = "Kalau mau baca ulang materi sebelumnya, tekan \"Sebelumnya\". Tombol ini muncul mulai dari materi ke-2.",
        targetKey = "study_prev_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_EXIT,
        title = "Keluar Belajar",
        description = "Mau keluar? Tekan tombol kembali di HP kamu — nanti muncul konfirmasi biar nggak keluar nggak sengaja.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.mascot_wrong
    ),
    TutorialStep(
        id = TutorialStepId.STUDY_CLOSING,
        title = "Siap Menghadapi Kuis!",
        description = "Setelah semua materi selesai, kamu bakal menghadapi beberapa kuis. Jawab sebisa kamu ya!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.chap1
    )
)

private val quizIntroSteps = listOf(
    TutorialStep(
        id = TutorialStepId.QUIZ_WELCOME,
        title = "Waktunya Kuis!",
        description = "Saatnya uji pemahamanmu dari materi tadi. Jawab setiap soal dengan teliti!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.group_275
    ),
    TutorialStep(
        id = TutorialStepId.QUIZ_OPTIONS,
        title = "Pilih Jawaban",
        description = "Ketuk salah satu pilihan jawaban. Kamu masih bisa ganti pilihan sebelum menekan tombol konfirmasi.",
        targetKey = "quiz_option_first",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.QUIZ_VERIFY,
        title = "Konfirmasi Jawaban",
        description = "Kalau sudah yakin, tekan tombol ini. Jawabanmu langsung dicek — pilihan salah jadi merah, benar jadi hijau.",
        targetKey = "quiz_verify_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.QUIZ_FEEDBACK,
        title = "Kartu Umpan Balik",
        description = "Setelah konfirmasi, muncul kartu umpan balik dengan tombol \"Lanjut\" (atau \"Selesai\" di soal terakhir) buat ke soal berikutnya.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.QUIZ_EXIT,
        title = "Keluar Kuis",
        description = "Mau keluar? Tekan tombol kembali di HP. Progres kuis belum tersimpan, jadi kamu harus mulai dari awal.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.mascot_wrong
    )
)

private val remedialIntroSteps = listOf(
    TutorialStep(
        id = TutorialStepId.REMEDIAL_WELCOME,
        title = "Belajar Lagi!",
        description = "Ada jawaban yang belum tepat — nggak apa-apa, kita belajar bareng lagi!",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.mascot_wrong
    ),
    TutorialStep(
        id = TutorialStepId.REMEDIAL_CTA,
        title = "Ayo Pelajari!",
        description = "Tekan tombol ini buat baca ulang materi soal yang kamu jawab salah. Setelah itu kamu dapet satu kesempatan buat jawab lagi.",
        targetKey = "remedial_cta_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        mascotResId = R.drawable.group_276
    )
)

private val profileIntroSteps = listOf(
    TutorialStep(
        id = TutorialStepId.PROFILE_HEADER_INFO,
        title = "Halaman Profil",
        description = "Ini halaman profilmu. Foto, nama, dan info sekolah ada di sini. Di dalam header ada ikon gear buat atur suara, dan ikon pensil buat edit profil.",
        targetKey = "profile_header",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_275
    ),
    TutorialStep(
        id = TutorialStepId.PROFILE_STATS,
        title = "Statistik Main",
        description = "Total skor, jumlah level selesai, dan jumlah bab selesai kamu tampil di sini.",
        targetKey = "profile_stats",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.PROFILE_ACHIEVEMENTS,
        title = "Pencapaian",
        description = "Daftar pencapaian kamu. Selesain kuis terus buat ngumpulin yang lain!",
        targetKey = "profile_achievements",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.PROFILE_LOGOUT,
        title = "Tombol Keluar",
        description = "Tekan tombol \"Keluar\" kalau kamu mau logout dari akun.",
        targetKey = "profile_logout_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        mascotResId = R.drawable.mascot_wrong
    )
)

private val editProfileIntroSteps = listOf(
    TutorialStep(
        id = TutorialStepId.EDIT_AVATAR,
        title = "Ganti Avatar",
        description = "Ketuk avatar buat ganti foto profilmu dari pilihan avatar yang tersedia.",
        targetKey = "edit_avatar_picker",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_275
    ),
    TutorialStep(
        id = TutorialStepId.EDIT_FULLNAME,
        title = "Nama Lengkap",
        description = "Edit nama lengkapmu di sini. Minimal 3 huruf.",
        targetKey = "edit_fullname",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.EDIT_BIO,
        title = "Bio Singkat",
        description = "Tulis bio singkat tentang kamu. Maksimal 500 karakter.",
        targetKey = "edit_bio",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.EDIT_SAVE,
        title = "Simpan Perubahan",
        description = "Tekan \"Simpan\" kalau sudah selesai. Perubahan kamu langsung tersimpan.",
        targetKey = "edit_save_btn",
        tooltipPosition = TooltipPosition.ABOVE_TARGET,
        mascotResId = R.drawable.chap1
    )
)

private val leaderboardIntroSteps = listOf(
    TutorialStep(
        id = TutorialStepId.LEADERBOARD_TABS,
        title = "Dua Tab",
        description = "Ada dua tab di sini: \"Aktivitas Harian\" buat lihat riwayat kuis yang pernah kamu kerjain, dan \"Papan Peringkat\" buat lihat ranking pemain.",
        targetKey = "leaderboard_tab_toggle",
        tooltipPosition = TooltipPosition.BELOW_TARGET,
        mascotResId = R.drawable.group_275
    ),
    TutorialStep(
        id = TutorialStepId.LEADERBOARD_PODIUM,
        title = "Podium & Peringkatmu",
        description = "Di tab \"Papan Peringkat\", tiga teratas tampil di podium. Peringkat kamu sendiri muncul di kotak tersendiri di bawah.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.group_276
    ),
    TutorialStep(
        id = TutorialStepId.LEADERBOARD_ACTIVITY,
        title = "Aktivitas Harian",
        description = "Geser ke tab \"Aktivitas Harian\" buat lihat daftar kuis yang pernah kamu kerjain, dikelompokkan per tanggal.",
        targetKey = null,
        tooltipPosition = TooltipPosition.CENTER_SCREEN,
        mascotResId = R.drawable.chap1
    )
)

val tutorialSegments: Map<TutorialSegmentId, List<TutorialStep>> = mapOf(
    TutorialSegmentId.HOME_INTRO to homeIntroSteps,
    TutorialSegmentId.STUDY_MATERIAL_INTRO to studyMaterialIntroSteps,
    TutorialSegmentId.QUIZ_INTRO to quizIntroSteps,
    TutorialSegmentId.REMEDIAL_INTRO to remedialIntroSteps,
    TutorialSegmentId.PROFILE_INTRO to profileIntroSteps,
    TutorialSegmentId.EDIT_PROFILE_INTRO to editProfileIntroSteps,
    TutorialSegmentId.LEADERBOARD_INTRO to leaderboardIntroSteps
)
