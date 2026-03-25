package com.example.litecartesnative.data.tts

interface TtsProvider {
    fun speak(text: String)
    fun stop()
    fun shutdown()
}
