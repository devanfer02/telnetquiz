package com.example.litecartesnative.data.repository

import com.example.litecartesnative.data.local.TokenManager
import com.example.litecartesnative.data.remote.api.TelNetQuizApi
import com.example.litecartesnative.data.remote.dto.LoginRequest
import com.example.litecartesnative.data.remote.dto.RegisterRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: TelNetQuizApi,
    private val tokenManager: TokenManager
) {
    val authToken: Flow<String?> = tokenManager.authToken
    val userEmail: Flow<String?> = tokenManager.userEmail
    val userName: Flow<String?> = tokenManager.userName

    suspend fun register(fullname: String, email: String, password: String): Result<String> {
        return try {
            val response = api.register(RegisterRequest(fullname, email, password))
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
                Result.Error(response.message() ?: "Registration failed", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email, password))
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
                Result.Error(response.message() ?: "Login failed", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun logout() {
        tokenManager.clearSession()
    }
}
