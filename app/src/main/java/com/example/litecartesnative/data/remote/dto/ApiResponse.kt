package com.example.litecartesnative.data.remote.dto

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
    val token: String? = null
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
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("stats")
    val stats: UserStatsDto? = null
)

data class UserStatsDto(
    @SerializedName("total_score")
    val totalScore: Int,
    @SerializedName("levels_completed")
    val levelsCompleted: Int,
    @SerializedName("chapters_completed")
    val chaptersCompleted: Int
)

data class UpdateProfileRequest(
    @SerializedName("fullname")
    val fullname: String? = null,
    @SerializedName("image")
    val image: String? = null
)

data class StudyMaterialDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("imageLink")
    val imageLink: String?,
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
    @SerializedName("leaderboard")
    val leaderboard: List<LeaderboardEntryDto>,
    @SerializedName("currentUser")
    val currentUser: LeaderboardUserDto?,
    @SerializedName("pagination")
    val pagination: PaginationDto
)

data class LeaderboardEntryDto(
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("fullname")
    val fullname: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("totalScore")
    val totalScore: Int
)

data class LeaderboardUserDto(
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("fullname")
    val fullname: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("totalScore")
    val totalScore: Int
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
