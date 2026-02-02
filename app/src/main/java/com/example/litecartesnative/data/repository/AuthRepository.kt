package com.example.litecartesnative.data.repository

import android.util.Log
import com.example.litecartesnative.data.local.TokenManager
import com.example.litecartesnative.data.remote.api.TelNetQuizApi
import com.example.litecartesnative.data.remote.dto.LoginRequest
import com.example.litecartesnative.data.remote.dto.RegisterRequest
import com.example.litecartesnative.data.remote.dto.UserProfileDto
import com.example.litecartesnative.data.remote.dto.ValidationErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: TelNetQuizApi,
    private val tokenManager: TokenManager
) {
    private val gson = Gson()

    val authToken: Flow<String?> = tokenManager.authToken
    val userEmail: Flow<String?> = tokenManager.userEmail
    val userName: Flow<String?> = tokenManager.userName

    private fun parseValidationError(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) return "An error occurred"

        return try {
            val errorResponse = gson.fromJson(errorBody, ValidationErrorResponse::class.java)
            if (errorResponse.errors != null && errorResponse.errors.isNotEmpty()) {
                errorResponse.errors.entries.joinToString("\n") { (field, messages) ->
                    messages.joinToString("\n") { "• $it" }
                }
            } else {
                errorResponse.message
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to parse error: ${e.message}")
            errorBody
        }
    }

    suspend fun register(fullname: String, email: String, password: String): Result<String> {
        return try {
            val request = RegisterRequest(fullname, email, password)
            Log.d("REGISTER", "Request: fullname=$fullname, email=$email, password=${password.take(3)}***")
            val response = api.register(request)
            Log.d("REGISTER", "Response code: ${response.code()}")
            Log.d("REGISTER", "Response body: ${response.body()}")
            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.token != null) {
                    tokenManager.saveAuthToken(body.token)
                    tokenManager.saveUserInfo(email, fullname)
                    Result.Success(body.message)
                } else {
                    Result.Success(body?.message ?: "Registration successful")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("REGISTER", "Error body: $errorBody")
                val errorMessage = parseValidationError(errorBody)
                Result.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            Log.e("REGISTER", "Exception: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val request = LoginRequest(email, password)
            Log.d("LOGIN", "Request: email=$email, password=${password.take(3)}***")
            val response = api.login(request)
            Log.d("LOGIN", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.token != null) {
                    tokenManager.saveAuthToken(body.token)
                    tokenManager.saveUserInfo(email, "")
                    Result.Success(body.message)
                } else {
                    Result.Error("Invalid response from server")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("LOGIN", "Error body: $errorBody")
                val errorMessage = parseValidationError(errorBody)
                Result.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Exception: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun logout() {
        tokenManager.clearSession()
    }

    suspend fun validateSession(): Result<UserProfileDto> {
        return try {
            val response = api.getUserProfile()
            if (response.isSuccessful) {
                val profile = response.body()?.data
                if (profile != null) {
                    tokenManager.saveUserInfo(profile.email, profile.fullname)
                    Result.Success(profile)
                } else {
                    Result.Error("Invalid profile response")
                }
            } else {
                Result.Error("Session invalid", response.code())
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "validateSession error: ${e.message}", e)
            Result.Error(e.message ?: "Network error")
        }
    }
}
