package com.example.telnetquiz.data.audio

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLifecycleObserver @Inject constructor(
    private val audioManager: AudioManager
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        audioManager.onAppForeground()
    }

    override fun onStop(owner: LifecycleOwner) {
        audioManager.onAppBackground()
    }
}
