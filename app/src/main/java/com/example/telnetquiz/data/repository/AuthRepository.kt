package com.example.telnetquiz.data.repository

import com.example.telnetquiz.data.local.TokenManager
import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import com.example.telnetquiz.data.remote.dto.LoginRequest
import com.example.telnetquiz.data.remote.dto.PaginatedSchoolsResponse
import com.example.telnetquiz.data.remote.dto.RegisterRequest
import com.example.telnetquiz.data.remote.dto.SchoolDto
import com.example.telnetquiz.data.remote.dto.UserProfileDto
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

    private val errorTranslations = mapOf(
        "user already exists" to "Pengguna dengan email ini sudah terdaftar",
        "invalid email address" to "Email yang diberikan tidak valid",
        "invalid email or password" to "Email atau kata sandi salah",
        "request body validation failed" to "Data yang diberikan tidak valid",
        "failed to register user" to "Gagal mendaftarkan pengguna",
        "failed to login user" to "Gagal masuk",
    )

    private fun translateError(message: String): String {
        val lower = message.lowercase()
        return errorTranslations.entries
            .firstOrNull { lower.contains(it.key) }?.value ?: message
    }

    private fun parseApiError(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) return "Terjadi kesalahan"

        return try {
            val jsonObject = gson.fromJson(errorBody, com.google.gson.JsonObject::class.java)
            val errors = jsonObject.get("errors")
            when {
                errors != null && errors.isJsonPrimitive ->
                    translateError(errors.asString)
                errors != null && errors.isJsonObject -> {
                    errors.asJsonObject.entrySet().flatMap { (_, messages) ->
                        when {
                            messages.isJsonArray -> messages.asJsonArray.map { translateError(it.asString) }
                            messages.isJsonPrimitive -> listOf(translateError(messages.asString))
                            else -> emptyList()
                        }
                    }.joinToString("\n") { "• $it" }
                }
                else -> translateError(
                    jsonObject.get("message")?.asString ?: "Terjadi kesalahan"
                )
            }
        } catch (_: Exception) {
            "Terjadi kesalahan"
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
                    body.refreshToken?.let { tokenManager.saveRefreshToken(it) }
                    tokenManager.saveUserInfo(email, fullname)
                    Result.Success(body.message)
                } else {
                    Result.Success(body?.message ?: "Registration successful")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseApiError(errorBody)
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Result.Error(e.toUserMessage())
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
                    body.refreshToken?.let { tokenManager.saveRefreshToken(it) }
                    tokenManager.saveUserInfo(email, "")
                    Result.Success(body.message)
                } else {
                    Result.Error("Invalid response from server")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseApiError(errorBody)
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Result.Error(e.toUserMessage())
        }
    }

    suspend fun logout() {
        try {
            api.logout()
        } catch (_: Exception) {
        }
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
                Result.Error("Failed to fetch schools")
            }
        } catch (e: Exception) {
            Result.Error(e.toUserMessage())
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
                Result.Error("Failed to search schools")
            }
        } catch (e: Exception) {
            Result.Error(e.toUserMessage())
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
                Result.Error("Session invalid")
            }
        } catch (e: Exception) {
            Result.Error(e.toUserMessage())
        }
    }
}
