package com.example.litecartesnative.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChaptersResponse(
    @SerializedName("has_taken_pretest")
    val hasTakenPretest: Boolean,
    @SerializedName("chapters")
    val chapters: List<ChapterDto>
)

data class ChapterDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("mascot_id")
    val mascotId: Int,
    @SerializedName("user_performance")
    val userPerformance: UserPerformanceDto?,
    @SerializedName("quiz_count")
    val quizCount: Int,
    @SerializedName("completed_quizzes")
    val completedQuizzes: Int
)

data class ChapterDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("mascotId")
    val mascotId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("quizzes")
    val quizzes: List<QuizSummaryDto>
)

data class QuizSummaryDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("chapterId")
    val chapterId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("numberOfQuestions")
    val numberOfQuestions: Int
)

data class UserPerformanceDto(
    @SerializedName("wrong_answers")
    val wrongAnswers: Int,
    @SerializedName("total_pretest_questions")
    val totalPretestQuestions: Int,
    @SerializedName("accuracy_percentage")
    val accuracyPercentage: Int,
)
