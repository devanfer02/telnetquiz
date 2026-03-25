package com.example.telnetquiz.data.tts

import android.content.Context
import android.media.MediaPlayer
import android.util.Base64
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class GoogleCloudTtsProvider(
    private val context: Context,
    private val apiKey: String
) : TtsProvider {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private var mediaPlayer: MediaPlayer? = null

    override fun speak(text: String) {
        stop()
        scope.launch {
            try {
                val audioBytes = synthesize(text)
                if (audioBytes != null) {
                    playAudio(audioBytes)
                }
            } catch (_: Exception) {}
        }
    }

    override fun stop() {
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

    private fun synthesize(text: String): ByteArray? {
        val body = JSONObject().apply {
            put("input", JSONObject().put("text", text))
            put("voice", JSONObject().apply {
                put("languageCode", "id-ID")
                put("ssmlGender", "FEMALE")
            })
            put("audioConfig", JSONObject().put("audioEncoding", "MP3"))
        }

        val request = Request.Builder()
            .url("https://texttospeech.googleapis.com/v1/text:synthesize?key=$apiKey")
            .post(body.toString().toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            return null
        }

        val json = JSONObject(response.body?.string() ?: return null)
        val audioContent = json.getString("audioContent")
        return Base64.decode(audioContent, Base64.DEFAULT)
    }

    private fun playAudio(audioBytes: ByteArray) {
        val tempFile = File.createTempFile("tts_", ".mp3", context.cacheDir)
        tempFile.writeBytes(audioBytes)

        mediaPlayer = MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            prepare()
            setOnCompletionListener {
                it.release()
                tempFile.delete()
                mediaPlayer = null
            }
            start()
        }
    }
}
