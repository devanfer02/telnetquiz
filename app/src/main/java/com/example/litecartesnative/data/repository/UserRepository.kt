package com.example.litecartesnative.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.litecartesnative.data.remote.api.TelNetQuizApi
import com.example.litecartesnative.data.remote.dto.*
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
                val errorBody = response.errorBody()?.string()
                Log.e("UserRepository", "Upload error: $errorBody")
                Result.Error("Gagal mengunggah foto", response.code())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "uploadAvatar error: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getUserProfile(): Result<UserProfileDto> {
        return try {
            Log.d("UserRepository", "Fetching user profile...")
            val response = api.getUserProfile()
            Log.d("UserRepository", "Response code: ${response.code()}")
            Log.d("UserRepository", "Response body: ${response.body()}")
            if (response.isSuccessful) {
                val profile = response.body()?.data
                if (profile != null) {
                    Log.d("UserRepository", "Profile loaded: ${profile.fullname}")
                    Result.Success(profile)
                } else {
                    Log.e("UserRepository", "Profile data is null")
                    Result.Error("Invalid profile response")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UserRepository", "Error response: $errorBody")
                Result.Error("Failed to fetch profile", response.code())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getUserProfile error: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun updateUserProfile(request: UpdateProfileRequest): Result<UserProfileDto> {
        return try {
            Log.d("UserRepository", "Updating user profile...")
            val response = api.updateUserProfile(request)
            Log.d("UserRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val profile = response.body()?.data
                if (profile != null) {
                    Log.d("UserRepository", "Profile updated: ${profile.fullname}")
                    Result.Success(profile)
                } else {
                    Log.e("UserRepository", "Updated profile data is null")
                    Result.Error("Invalid profile response")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UserRepository", "Error response: $errorBody")
                Result.Error("Failed to update profile", response.code())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "updateUserProfile error: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getAchievements(): Result<AchievementsResponse> {
        return try {
            Log.d("UserRepository", "Fetching achievements...")
            val response = api.getAchievements()
            Log.d("UserRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val achievements = response.body()?.data
                if (achievements != null) {
                    Log.d("UserRepository", "Achievements loaded: ${achievements.achievements.size}")
                    Result.Success(achievements)
                } else {
                    Log.e("UserRepository", "Achievements data is null")
                    Result.Error("Invalid achievements response")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UserRepository", "Error response: $errorBody")
                Result.Error("Failed to fetch achievements", response.code())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getAchievements error: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getLeaderboard(limit: Int = 10, cursor: Int? = null): Result<LeaderboardResponse> {
        return try {
            Log.d("UserRepository", "Fetching leaderboard (limit=$limit, cursor=$cursor)...")
            val response = api.getLeaderboard(limit, cursor)
            Log.d("UserRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val leaderboard = response.body()?.data
                if (leaderboard != null) {
                    Log.d("UserRepository", "Leaderboard loaded: ${leaderboard.leaderboard.size} entries")
                    Result.Success(leaderboard)
                } else {
                    Log.e("UserRepository", "Leaderboard data is null")
                    Result.Error("Invalid leaderboard response")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UserRepository", "Error response: $errorBody")
                Result.Error("Failed to fetch leaderboard", response.code())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getLeaderboard error: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getStudyMaterial(id: Int): Result<StudyMaterialDto> {
        return try {
            Log.d("UserRepository", "Fetching study material id=$id...")
            val response = api.getStudyMaterial(id)
            Log.d("UserRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val material = response.body()?.data
                if (material != null) {
                    Log.d("UserRepository", "Study material loaded: ${material.title}")
                    Result.Success(material)
                } else {
                    Log.e("UserRepository", "Study material data is null")
                    Result.Error("Invalid study material response")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UserRepository", "Error response: $errorBody")
                Result.Error("Failed to fetch study material", response.code())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getStudyMaterial error: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getRecentActivity(): Result<RecentActivityResponse> {
        return try {
            Log.d("UserRepository", "Fetching recent activity...")
            val response = api.getRecentActivity()
            Log.d("UserRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val activity = response.body()?.data
                if (activity != null) {
                    Log.d("UserRepository", "Recent activity loaded: ${activity.activities.size} days")
                    Result.Success(activity)
                } else {
                    Log.e("UserRepository", "Recent activity data is null")
                    Result.Error("Invalid recent activity response")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UserRepository", "Error response: $errorBody")
                Result.Error("Failed to fetch recent activity", response.code())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getRecentActivity error: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getPretestStatus(): Result<PretestStatusResponse> {
        return try {
            Log.d("UserRepository", "Fetching pretest status...")
            val response = api.getPretestStatus()
            Log.d("UserRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val status = response.body()?.data
                if (status != null) {
                    Log.d("UserRepository", "Pretest status: hasTaken=${status.hasTakenPretest}")
                    Result.Success(status)
                } else {
                    Log.e("UserRepository", "Pretest status data is null")
                    Result.Error("Invalid pretest status response")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UserRepository", "Error response: $errorBody")
                Result.Error("Failed to fetch pretest status", response.code())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getPretestStatus error: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }
}
