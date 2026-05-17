package com.example.telnetquiz.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T? = null
)

data class AuthResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("refreshToken")
    val refreshToken: String? = null
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class RefreshTokenResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class ValidationErrorResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("errors")
    val errors: Map<String, List<String>>? = null
)

data class LoginErrorResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("errors")
    val errors: String? = null
)

data class UserProfileDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("bio")
    val bio: String? = null,
    @SerializedName("gender")
    val gender: Boolean? = null,
    @SerializedName("grade")
    val grade: String? = null,
    @SerializedName("school")
    val school: SchoolDto? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("has_taken_pretest")
    val hasTakenPretest: Boolean = false,
    @SerializedName("stats")
    val stats: UserStatsDto? = null
)

data class UserStatsDto(
    @SerializedName("total_score")
    val totalScore: Int,
    @SerializedName("levels_completed")
    val levelsCompleted: Int,
    @SerializedName("chapters_completed")
    val chaptersCompleted: Int,
    @SerializedName("total_chapters")
    val totalChapters: Int = 0,
    @SerializedName("daily_streak")
    val dailyStreak: Int = 0
)

data class UpdateProfileRequest(
    @SerializedName("fullname")
    val fullname: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("bio")
    val bio: String? = null
)

data class SchoolDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class SchoolsResponse(
    @SerializedName("schools")
    val schools: List<SchoolDto>
)

data class PaginatedSchoolsResponse(
    @SerializedName("schools")
    val schools: List<SchoolDto>,
    @SerializedName("pagination")
    val pagination: SchoolPaginationDto
)

data class SchoolPaginationDto(
    @SerializedName("total")
    val total: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean
)

data class AvatarResponse(
    @SerializedName("image_url")
    val imageUrl: String
)

data class StudyMaterialDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("imageLink")
    val imageLink: String?,
    @SerializedName("audio_link")
    val audioLink: String? = null,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class AchievementDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("unlocked")
    val unlocked: Boolean,
    @SerializedName("unlockedAt")
    val unlockedAt: String? = null
)

data class AchievementsResponse(
    @SerializedName("achievements")
    val achievements: List<AchievementDto>
)

data class LeaderboardResponse(
    @SerializedName("period")
    val period: String? = null,
    @SerializedName("leaderboard")
    val leaderboard: List<LeaderboardEntryDto>? = null,
    @SerializedName("currentUser")
    val currentUser: LeaderboardUserDto? = null,
    @SerializedName("pagination")
    val pagination: PaginationDto? = null
)

data class LeaderboardEntryDto(
    @SerializedName("rank")
    val rank: Int = 0,
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("fullname")
    val fullname: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("gender")
    val gender: Boolean? = null,
    @SerializedName("totalScore")
    val totalScore: Int = 0,
    @SerializedName("rankDelta")
    val rankDelta: Int? = null
)

data class LeaderboardUserDto(
    @SerializedName("rank")
    val rank: Int = 0,
    @SerializedName("fullname")
    val fullname: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("gender")
    val gender: Boolean? = null,
    @SerializedName("totalScore")
    val totalScore: Int = 0,
    @SerializedName("rankDelta")
    val rankDelta: Int? = null
)

data class PaginationDto(
    @SerializedName("nextCursor")
    val nextCursor: Int?,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean
)

data class PretestStatusResponse(
    @SerializedName("has_taken_pretest")
    val hasTakenPretest: Boolean
)

data class RecentActivityResponse(
    @SerializedName("activities") val activities: List<DayActivityDto>
)

data class DayActivityDto(
    @SerializedName("date") val date: String,
    @SerializedName("level_count") val levelCount: Int = 0,
    @SerializedName("chapter_groups") val chapterGroups: List<ChapterActivityGroupDto>? = null
)

data class ChapterActivityGroupDto(
    @SerializedName("chapter_id") val chapterId: Int = 0,
    @SerializedName("chapter_title") val chapterTitle: String = "",
    @SerializedName("total_levels") val totalLevels: Int = 0,
    @SerializedName("levels_completed_today") val levelsCompletedToday: Int = 0,
    @SerializedName("average_score") val averageScore: Int = 0,
    @SerializedName("completion_percentage") val completionPercentage: Int = 0,
    @SerializedName("entries") val entries: List<ActivityEntryDto>? = null
)

data class ActivityEntryDto(
    @SerializedName("quiz_id") val quizId: Int,
    @SerializedName("quiz_level") val quizLevel: Int,
    @SerializedName("retry_count") val retryCount: Int,
    @SerializedName("latest_score") val latestScore: Int,
    @SerializedName("latest_time") val latestTime: String? = null
)

data class VerifyAnswerRequest(
    @SerializedName("quiz_id") val quizId: Int,
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("answered_option_id") val answeredOptionId: Int
)

data class VerifyAnswerResponse(
    @SerializedName("correct") val correct: Boolean,
    @SerializedName("correct_option_id") val correctOptionId: Int
)

data class QuizMaterialsRequest(
    @SerializedName("quiz_id") val quizId: Int
)

data class QuizMaterialsResponse(
    @SerializedName("materials") val materials: List<StudyMaterialDto>
)

data class BulkMaterialsRequest(
    @SerializedName("material_ids") val materialIds: List<Int>
)

data class BulkMaterialsResponse(
    @SerializedName("materials") val materials: List<StudyMaterialDto>
)

data class TtsAudioResponse(
    @SerializedName("audio_url") val audioUrl: String
)
