package com.example.telnetquiz.data.tts

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface TtsProvider {
    val isLoading: StateFlow<Boolean> get() = MutableStateFlow(false)
    fun speak(text: String)
    fun speakContent(type: String, id: Int, gender: Boolean?) {}
    fun stop()
    fun shutdown()
}
