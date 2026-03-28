package com.example.telnetquiz.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.example.telnetquiz.R
import com.example.telnetquiz.data.local.AudioPreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioPreferenceManager: AudioPreferenceManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var mediaPlayer: MediaPlayer? = null
    private var bgShouldPlay = false
    private var isMuted = false

    val isMutedFlow: Flow<Boolean> = audioPreferenceManager.isMuted

    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(3)
        .setAudioAttributes(audioAttributes)
        .build()

    private val sfxMap = mutableMapOf<SfxType, Int>()

    private val bgPauseRoutes = setOf(
        "question_screen",
        "result_screen",
        "remedial_screen",
        "pretest_screen",
        "pretest_result_screen"
    )

    init {
        SfxType.entries.forEach { type ->
            sfxMap[type] = soundPool.load(context, type.resId, 1)
        }

        scope.launch {
            audioPreferenceManager.isMuted.collect { muted ->
                isMuted = muted
                if (muted) {
                    mediaPlayer?.pause()
                } else if (bgShouldPlay) {
                    ensureBgPlaying()
                }
            }
        }
    }

    fun onScreenChanged(route: String) {
        val baseRoute = route.split("/").first().split("?").first()
        if (baseRoute.startsWith("auth_")) return
        if (baseRoute in bgPauseRoutes) {
            pauseBgMusic()
        } else {
            resumeBgMusic()
        }
    }

    fun resumeBgMusic() {
        bgShouldPlay = true
        if (isMuted) return
        ensureBgPlaying()
    }

    fun pauseBgMusic() {
        bgShouldPlay = false
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    fun playSfx(type: SfxType) {
        if (isMuted) return
        val soundId = sfxMap[type] ?: return
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    fun toggleMute() {
        scope.launch {
            audioPreferenceManager.setMuted(!isMuted)
        }
    }

    fun onAppForeground() {
        if (bgShouldPlay && !isMuted) {
            ensureBgPlaying()
        }
    }

    fun onAppBackground() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    private fun ensureBgPlaying() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.bgsound)?.apply {
                isLooping = true
                start()
            }
        } else if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }
}
