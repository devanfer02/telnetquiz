package com.example.telnetquiz.data.local

data class AudioSettings(
    val isMuted: Boolean = false,
    val globalVolume: Float = 1f,
    val sfxVolume: Float = 1f,
    val bgMusicVolume: Float = 1f
) {
    val effectiveSfxVolume: Float get() = if (isMuted) 0f else globalVolume * sfxVolume
    val effectiveBgMusicVolume: Float get() = if (isMuted) 0f else globalVolume * bgMusicVolume
}
