package com.example.telnetquiz.di

import android.content.Context
import com.example.telnetquiz.BuildConfig
import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import com.example.telnetquiz.data.tts.EdgeTtsProvider
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
    fun provideTtsProvider(
        @ApplicationContext context: Context,
        api: TelNetQuizApi
    ): TtsProvider {
        return when (BuildConfig.TTS_PROVIDER) {
            "edge" -> EdgeTtsProvider(context, api)
            else -> LocalTtsProvider(context)
        }
    }
}
