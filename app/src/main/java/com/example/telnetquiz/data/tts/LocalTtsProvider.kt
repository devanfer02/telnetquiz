package com.example.telnetquiz.data.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class LocalTtsProvider(context: Context) : TtsProvider, TextToSpeech.OnInitListener {

    private var tts: TextToSpeech = TextToSpeech(context, this)
    private var isReady = false

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
    }

    override fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
