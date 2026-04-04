package com.example.telnetquiz.data.remote.api

import com.example.telnetquiz.data.remote.dto.AchievementsResponse
import com.example.telnetquiz.data.remote.dto.ApiResponse
import com.example.telnetquiz.data.remote.dto.AuthResponse
import com.example.telnetquiz.data.remote.dto.AvatarResponse
import com.example.telnetquiz.data.remote.dto.ChapterDetailDto
import com.example.telnetquiz.data.remote.dto.ChaptersResponse
import com.example.telnetquiz.data.remote.dto.LeaderboardResponse
import com.example.telnetquiz.data.remote.dto.LoginRequest
import com.example.telnetquiz.data.remote.dto.PaginatedSchoolsResponse
import com.example.telnetquiz.data.remote.dto.PretestQuestionsResponse
import com.example.telnetquiz.data.remote.dto.PretestResultDto
import com.example.telnetquiz.data.remote.dto.PretestStatusResponse
import com.example.telnetquiz.data.remote.dto.QuizDto
import com.example.telnetquiz.data.remote.dto.BulkMaterialsRequest
import com.example.telnetquiz.data.remote.dto.BulkMaterialsResponse
import com.example.telnetquiz.data.remote.dto.TtsAudioResponse
import com.example.telnetquiz.data.remote.dto.QuizMaterialsRequest
import com.example.telnetquiz.data.remote.dto.QuizMaterialsResponse
import com.example.telnetquiz.data.remote.dto.QuizResultDto
import com.example.telnetquiz.data.remote.dto.RecentActivityResponse
import com.example.telnetquiz.data.remote.dto.RegisterRequest
import com.example.telnetquiz.data.remote.dto.SchoolsResponse
import com.example.telnetquiz.data.remote.dto.StudyMaterialDto
import com.example.telnetquiz.data.remote.dto.SubmitPretestRequest
import com.example.telnetquiz.data.remote.dto.SubmitQuizRequest
import com.example.telnetquiz.data.remote.dto.UpdateProfileRequest
import com.example.telnetquiz.data.remote.dto.UserProfileDto
import com.example.telnetquiz.data.remote.dto.VerifyAnswerRequest
import com.example.telnetquiz.data.remote.dto.VerifyAnswerResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @Headers("Content-Type: application/json")
    @POST("api/materials/bulk-get")
    suspend fun bulkGetMaterials(
        @Body request: BulkMaterialsRequest
    ): Response<ApiResponse<BulkMaterialsResponse>>

    // TTS
    @GET("api/tts/{type}/{id}")
    suspend fun getTtsAudio(
        @Path("type") type: String,
        @Path("id") id: Int
    ): Response<ApiResponse<TtsAudioResponse>>
}
