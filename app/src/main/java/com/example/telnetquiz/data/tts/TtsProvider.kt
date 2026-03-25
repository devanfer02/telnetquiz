package com.example.telnetquiz.data.tts

interface TtsProvider {
    fun speak(text: String)
    fun stop()
    fun shutdown()
}
