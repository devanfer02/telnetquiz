package com.example.telnetquiz.di

import android.content.Context
import com.example.telnetquiz.BuildConfig
import com.example.telnetquiz.data.tts.GoogleCloudTtsProvider
import com.example.telnetquiz.data.tts.LocalTtsProvider
import com.example.telnetquiz.data.tts.TtsProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TtsModule {

    @Provides
    @Singleton
    fun provideTtsProvider(@ApplicationContext context: Context): TtsProvider {
        return when (BuildConfig.TTS_PROVIDER) {
            "cloud" -> GoogleCloudTtsProvider(context, BuildConfig.GOOGLE_TTS_API_KEY)
            else -> LocalTtsProvider(context)
        }
    }
}
