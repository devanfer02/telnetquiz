package com.example.litecartesnative.data.remote.api

import com.example.litecartesnative.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface TelNetQuizApi {

    // Auth endpoints
    @Headers("Content-Type: application/json")
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @Headers("Content-Type: application/json")
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    // Pretest endpoints
    @GET("api/pretest")
    suspend fun getPretestQuestions(): Response<ApiResponse<PretestQuestionsResponse>>

    @Headers("Content-Type: application/json")
    @POST("api/pretest")
    suspend fun submitPretestAnswers(
        @Body request: SubmitPretestRequest
    ): Response<ApiResponse<Unit>>

    // Chapter endpoints
    @GET("api/chapters")
    suspend fun getChapters(): Response<ApiResponse<ChaptersResponse>>

    @GET("api/chapters/{id}")
    suspend fun getChapterById(
        @Path("id") id: Int
    ): Response<ApiResponse<ChapterDetailDto>>

    // Quiz endpoints
    @GET("api/quiz/{id}")
    suspend fun getQuizById(
        @Path("id") id: Int
    ): Response<ApiResponse<QuizDto>>

    @Headers("Content-Type: application/json")
    @POST("api/quiz/{id}")
    suspend fun submitQuizAnswers(
        @Path("id") id: Int,
        @Body request: SubmitQuizRequest
    ): Response<ApiResponse<QuizResultDto>>

    // User profile endpoint (also used for session validation)
    @GET("api/users/profile")
    suspend fun getUserProfile(): Response<ApiResponse<UserProfileDto>>
}
