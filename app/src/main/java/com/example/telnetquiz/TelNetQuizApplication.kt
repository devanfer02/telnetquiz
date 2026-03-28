package com.example.telnetquiz

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.telnetquiz.data.audio.AppLifecycleObserver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TelNetQuizApplication : Application() {

    @Inject lateinit var appLifecycleObserver: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }
}
