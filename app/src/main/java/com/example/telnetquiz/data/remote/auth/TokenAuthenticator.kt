package com.example.telnetquiz.data.remote.auth

import com.example.telnetquiz.data.local.TokenManager
import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import com.example.telnetquiz.data.remote.dto.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    @Named("refreshApi") private val refreshApi: dagger.Lazy<TelNetQuizApi>
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header("X-Refresh-Attempted") != null) {
            runBlocking { tokenManager.onSessionExpired() }
            return null
        }

        return runBlocking {
            mutex.withLock {
                val currentToken = tokenManager.getAuthTokenSync()
                val requestToken = response.request.header("Authorization")
                    ?.removePrefix("Bearer ")

                if (currentToken != null && currentToken != requestToken) {
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .header("X-Refresh-Attempted", "true")
                        .build()
                }

                val refreshToken = tokenManager.getRefreshTokenSync()
                if (refreshToken == null) {
                    tokenManager.onSessionExpired()
                    return@runBlocking null
                }

                try {
                    val refreshResponse = refreshApi.get().refreshToken(
                        RefreshTokenRequest(refreshToken)
                    )
                    if (refreshResponse.isSuccessful) {
                        val body = refreshResponse.body()!!
                        tokenManager.saveAuthToken(body.token)
                        tokenManager.saveRefreshToken(body.refreshToken)

                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${body.token}")
                            .header("X-Refresh-Attempted", "true")
                            .build()
                    } else {
                        tokenManager.onSessionExpired()
                        null
                    }
                } catch (_: Exception) {
                    tokenManager.onSessionExpired()
                    null
                }
            }
        }
    }
}
