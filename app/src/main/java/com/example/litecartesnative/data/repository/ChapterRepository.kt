package com.example.litecartesnative.data.repository

import com.example.litecartesnative.data.remote.api.TelNetQuizApi
import com.example.litecartesnative.data.remote.dto.ChapterDetailDto
import com.example.litecartesnative.data.remote.dto.ChapterDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChapterRepository @Inject constructor(
    private val api: TelNetQuizApi
) {
    suspend fun getChapters(): Result<List<ChapterDto>> {
        return try {
            val response = api.getChapters()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error("No chapters found")
                }
            } else {
                Result.Error(response.message() ?: "Failed to fetch chapters", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getChapterById(id: Int): Result<ChapterDetailDto> {
        return try {
            val response = api.getChapterById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error("Chapter not found")
                }
            } else {
                Result.Error(response.message() ?: "Failed to fetch chapter", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
