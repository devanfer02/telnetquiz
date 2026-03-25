package com.example.telnetquiz.data.repository

import com.example.telnetquiz.data.local.TokenManager
import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import com.example.telnetquiz.data.remote.dto.LoginRequest
import com.example.telnetquiz.data.remote.dto.RegisterRequest
import com.example.telnetquiz.data.remote.dto.PaginatedSchoolsResponse
import com.example.telnetquiz.data.remote.dto.SchoolDto
import com.example.telnetquiz.data.remote.dto.UserProfileDto
import com.example.telnetquiz.data.remote.dto.ValidationErrorResponse
import com.example.telnetquiz.data.remote.dto.LoginErrorResponse
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
            errorBody
        }
    }

    private fun parseLoginError(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) return "An error occurred"

        return try {
            val errorResponse = gson.fromJson(errorBody, LoginErrorResponse::class.java)
            errorResponse.errors ?: errorResponse.message
        } catch (e: Exception) {
            errorBody
        }
    }

    suspend fun register(
        fullname: String,
        email: String,
        password: String,
        schoolId: Int,
        gender: Boolean,
        grade: String
    ): Result<String> {
        return try {
            val request = RegisterRequest(fullname, email, password, schoolId, gender, grade)
            val response = api.register(request)
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
                val errorMessage = parseValidationError(errorBody)
                Result.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val request = LoginRequest(email, password)
            val response = api.login(request)
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
                val errorMessage = parseLoginError(errorBody)
                Result.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun logout() {
        tokenManager.clearSession()
    }

    suspend fun getSchools(): Result<List<SchoolDto>> {
        return try {
            val response = api.getSchools()
            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.schools)
                } else {
                    Result.Error("Invalid schools response")
                }
            } else {
                Result.Error("Failed to fetch schools", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun searchSchools(
        search: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ): Result<PaginatedSchoolsResponse> {
        return try {
            val response = api.searchSchools(search, limit, offset)
            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data)
                } else {
                    Result.Error("Invalid schools response")
                }
            } else {
                Result.Error("Failed to search schools", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
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
            Result.Error(e.message ?: "Network error")
        }
    }
}
