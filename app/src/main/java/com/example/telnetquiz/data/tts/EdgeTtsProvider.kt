package com.example.telnetquiz.data.tts

import android.content.Context
import android.media.MediaPlayer
import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

class EdgeTtsProvider(
    private val context: Context,
    private val api: TelNetQuizApi
) : TtsProvider {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private var mediaPlayer: MediaPlayer? = null
    private var currentJob: Job? = null
    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    override fun speakContent(type: String, id: Int, gender: Boolean?) {
        stop()
        currentJob = scope.launch {
            try {
                val cacheFile = getCacheFile(type, id)

                if (cacheFile.exists()) {
                    withContext(Dispatchers.Main) { playFile(cacheFile) }
                    return@launch
                }

                _isLoading.value = true

                val response = api.getTtsAudio(type, id)
                if (!response.isSuccessful) return@launch

                val audioUrl = response.body()?.data?.audioUrl ?: return@launch
                val audioBytes = downloadAudio(audioUrl) ?: return@launch

                cacheFile.parentFile?.mkdirs()
                cacheFile.writeBytes(audioBytes)
                withContext(Dispatchers.Main) { playFile(cacheFile) }
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun speak(text: String) {
        // No-op for edge provider — use speakContent instead
    }

    override fun stop() {
        currentJob?.cancel()
        currentJob = null
        _isLoading.value = false
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    override fun shutdown() {
        stop()
        scope.cancel()
    }

    private fun getCacheFile(type: String, id: Int): File {
        return File(context.cacheDir, "tts/$type-$id-audio.mp3")
    }

    private fun downloadAudio(url: String): ByteArray? {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) return null
        return response.body?.bytes()
    }

    private fun playFile(file: File) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            prepare()
            setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
            start()
        }
    }
}
