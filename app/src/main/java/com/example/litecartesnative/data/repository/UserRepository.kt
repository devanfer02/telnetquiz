package com.example.litecartesnative.data.repository

import android.util.Log
import com.example.litecartesnative.data.remote.api.TelNetQuizApi
import com.example.litecartesnative.data.remote.dto.UserProfileDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: TelNetQuizApi
) {
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
}
