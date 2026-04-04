package com.example.telnetquiz.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.example.telnetquiz.R
import com.example.telnetquiz.data.local.AudioPreferenceManager
import com.example.telnetquiz.data.local.AudioSettings
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
    @Volatile
    private var audioSettings = AudioSettings()

    val isMutedFlow: Flow<Boolean> = audioPreferenceManager.isMuted
    val audioSettingsFlow: Flow<AudioSettings> = audioPreferenceManager.audioSettings

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
        "pretest_result_screen",
        "feedback_screen"
    )

    init {
        SfxType.entries.forEach { type ->
            sfxMap[type] = soundPool.load(context, type.resId, 1)
        }

        scope.launch {
            audioPreferenceManager.audioSettings.collect { settings ->
                val wasMuted = audioSettings.isMuted
                audioSettings = settings
                applyBgVolume()
                if (settings.isMuted && !wasMuted) {
                    mediaPlayer?.pause()
                } else if (!settings.isMuted && bgShouldPlay) {
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
        if (audioSettings.isMuted) return
        ensureBgPlaying()
    }

    fun pauseBgMusic() {
        bgShouldPlay = false
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    fun playSfx(type: SfxType) {
        val vol = audioSettings.effectiveSfxVolume
        if (vol <= 0f) return
        val soundId = sfxMap[type] ?: return
        soundPool.play(soundId, vol, vol, 1, 0, 1f)
    }

    fun toggleMute() {
        scope.launch {
            audioPreferenceManager.setMuted(!audioSettings.isMuted)
        }
    }

    fun setGlobalVolume(volume: Float) {
        scope.launch { audioPreferenceManager.setGlobalVolume(volume) }
    }

    fun setSfxVolume(volume: Float) {
        scope.launch { audioPreferenceManager.setSfxVolume(volume) }
    }

    fun setBgMusicVolume(volume: Float) {
        scope.launch { audioPreferenceManager.setBgMusicVolume(volume) }
    }

    fun onAppForeground() {
        if (bgShouldPlay && !audioSettings.isMuted) {
            ensureBgPlaying()
        }
    }

    fun onAppBackground() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    private fun applyBgVolume() {
        val vol = audioSettings.effectiveBgMusicVolume
        mediaPlayer?.setVolume(vol, vol)
    }

    private fun ensureBgPlaying() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.bgsound)?.apply {
                val vol = audioSettings.effectiveBgMusicVolume
                setVolume(vol, vol)
                isLooping = true
                start()
            }
        } else if (mediaPlayer?.isPlaying == false) {
            applyBgVolume()
            mediaPlayer?.start()
        }
    }
}
