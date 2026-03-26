package com.example.telnetquiz.data.repository

import android.content.Context
import android.net.Uri
import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import com.example.telnetquiz.data.remote.dto.AchievementsResponse
import com.example.telnetquiz.data.remote.dto.LeaderboardResponse
import com.example.telnetquiz.data.remote.dto.PretestStatusResponse
import com.example.telnetquiz.data.remote.dto.RecentActivityResponse
import com.example.telnetquiz.data.remote.dto.UpdateProfileRequest
import com.example.telnetquiz.data.remote.dto.UserProfileDto
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: TelNetQuizApi,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val MAX_FILE_SIZE = 2 * 1024 * 1024 // 2MB
    }

    suspend fun uploadAvatar(uri: Uri): Result<String> {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
                ?: return Result.Error("Cannot read file")
            val bytes = inputStream.readBytes()
            inputStream.close()

            if (bytes.size > MAX_FILE_SIZE) {
                return Result.Error("Ukuran file melebihi batas 2MB")
            }

            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("image", "avatar.jpg", requestBody)

            val response = api.uploadAvatar(part)
            if (response.isSuccessful) {
                val imageUrl = response.body()?.data?.imageUrl
                if (imageUrl != null) {
                    Result.Success(imageUrl)
                } else {
                    Result.Error("Invalid upload response")
                }
            } else {
                Result.Error("Gagal mengunggah foto")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getUserProfile(): Result<UserProfileDto> {
        return try {
            val response = api.getUserProfile()
            if (response.isSuccessful) {
                val profile = response.body()?.data
                if (profile != null) {
                    Result.Success(profile)
                } else {
                    Result.Error("Invalid profile response")
                }
            } else {
                Result.Error("Failed to fetch profile")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun updateUserProfile(request: UpdateProfileRequest): Result<UserProfileDto> {
        return try {
            val response = api.updateUserProfile(request)
            if (response.isSuccessful) {
                val profile = response.body()?.data
                if (profile != null) {
                    Result.Success(profile)
                } else {
                    Result.Error("Invalid profile response")
                }
            } else {
                Result.Error("Failed to update profile")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getAchievements(): Result<AchievementsResponse> {
        return try {
            val response = api.getAchievements()
            if (response.isSuccessful) {
                val achievements = response.body()?.data
                if (achievements != null) {
                    Result.Success(achievements)
                } else {
                    Result.Error("Invalid achievements response")
                }
            } else {
                Result.Error("Failed to fetch achievements")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getLeaderboard(limit: Int = 10, cursor: Int? = null): Result<LeaderboardResponse> {
        return try {
            val response = api.getLeaderboard(limit, cursor)
            if (response.isSuccessful) {
                val leaderboard = response.body()?.data
                if (leaderboard != null) {
                    Result.Success(leaderboard)
                } else {
                    Result.Error("Invalid leaderboard response")
                }
            } else {
                Result.Error("Failed to fetch leaderboard")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getRecentActivity(): Result<RecentActivityResponse> {
        return try {
            val response = api.getRecentActivity()
            if (response.isSuccessful) {
                val activity = response.body()?.data
                if (activity != null) {
                    Result.Success(activity)
                } else {
                    Result.Error("Invalid recent activity response")
                }
            } else {
                Result.Error("Failed to fetch recent activity")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getPretestStatus(): Result<PretestStatusResponse> {
        return try {
            val response = api.getPretestStatus()
            if (response.isSuccessful) {
                val status = response.body()?.data
                if (status != null) {
                    Result.Success(status)
                } else {
                    Result.Error("Invalid pretest status response")
                }
            } else {
                Result.Error("Failed to fetch pretest status")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
