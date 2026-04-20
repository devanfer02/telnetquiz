package com.example.telnetquiz.data.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class LocalTtsProvider(context: Context) : TtsProvider, TextToSpeech.OnInitListener {

    private var tts: TextToSpeech = TextToSpeech(context, this)
    private var isReady = false

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val indonesian = Locale("id", "ID")
            val result = tts.setLanguage(indonesian)
            isReady = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
            if (!isReady) {
                tts.setLanguage(Locale.getDefault())
                isReady = true
            }
            tts.voices?.firstOrNull {
                it.locale == indonesian && !it.isNetworkConnectionRequired
                        && it.name.lowercase().let { n -> n.contains("male") || n.contains("ardi") }
            }?.let { tts.voice = it }

            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isPlaying.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isPlaying.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isPlaying.value = false
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    _isPlaying.value = false
                }

                override fun onStop(utteranceId: String?, interrupted: Boolean) {
                    _isPlaying.value = false
                }
            })
        }
    }

    override fun speak(text: String) {
        if (isReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_utterance")
        }
    }

    override fun stop() {
        if (isReady) {
            tts.stop()
        }
        _isPlaying.value = false
    }

    override fun shutdown() {
        tts.stop()
        tts.shutdown()
        _isPlaying.value = false
    }
}
