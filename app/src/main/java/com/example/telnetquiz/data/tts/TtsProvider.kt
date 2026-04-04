package com.example.telnetquiz.data.tts

interface TtsProvider {
    fun speak(text: String)
    fun speakContent(type: String, id: Int, gender: Boolean?) {}
    fun stop()
    fun shutdown()
}
