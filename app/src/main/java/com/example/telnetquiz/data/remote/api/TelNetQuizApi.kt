package com.example.telnetquiz.data.remote.api

import com.example.telnetquiz.data.remote.dto.*
import okhttp3.MultipartBody
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

    // Schools
    @GET("api/schools")
    suspend fun getSchools(): Response<ApiResponse<SchoolsResponse>>

    @GET("api/schools")
    suspend fun searchSchools(
        @Query("search") search: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<ApiResponse<PaginatedSchoolsResponse>>

    // Pretest endpoints
    @GET("api/pretest")
    suspend fun getPretestQuestions(): Response<ApiResponse<PretestQuestionsResponse>>

    @Headers("Content-Type: application/json")
    @POST("api/pretest")
    suspend fun submitPretestAnswers(
        @Body request: SubmitPretestRequest
    ): Response<ApiResponse<PretestResultDto>>

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

    // User profile endpoints (also used for session validation)
    @GET("api/users/profile")
    suspend fun getUserProfile(): Response<ApiResponse<UserProfileDto>>

    @Headers("Content-Type: application/json")
    @PATCH("api/users/profile")
    suspend fun updateUserProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<UserProfileDto>>

    @Multipart
    @POST("api/users/avatar")
    suspend fun uploadAvatar(
        @Part image: MultipartBody.Part
    ): Response<ApiResponse<AvatarResponse>>

    // Study material
    @GET("api/materials/{id}")
    suspend fun getStudyMaterial(
        @Path("id") id: Int
    ): Response<ApiResponse<StudyMaterialDto>>

    // Achievements
    @GET("api/achievements")
    suspend fun getAchievements(): Response<ApiResponse<AchievementsResponse>>

    // Leaderboard
    @GET("api/leaderboard")
    suspend fun getLeaderboard(
        @Query("limit") limit: Int = 10,
        @Query("cursor") cursor: Int? = null
    ): Response<ApiResponse<LeaderboardResponse>>

    // Pretest status
    @GET("api/pretest/status")
    suspend fun getPretestStatus(): Response<ApiResponse<PretestStatusResponse>>

    // Recent activity
    @GET("api/activity/recent")
    suspend fun getRecentActivity(): Response<ApiResponse<RecentActivityResponse>>

    // Verify quiz answer
    @Headers("Content-Type: application/json")
    @POST("api/quiz/verify")
    suspend fun verifyAnswer(
        @Body request: VerifyAnswerRequest
    ): Response<ApiResponse<VerifyAnswerResponse>>

    // Quiz study materials
    @Headers("Content-Type: application/json")
    @POST("api/quiz/materials")
    suspend fun getQuizMaterials(
        @Body request: QuizMaterialsRequest
    ): Response<ApiResponse<QuizMaterialsResponse>>
}
