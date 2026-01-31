package com.example.litecartesnative.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QuizDto(
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
    @SerializedName("questions")
    val questions: List<QuizQuestionDto>
)

data class QuizQuestionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("chapterId")
    val chapterId: Int,
    @SerializedName("quizId")
    val quizId: Int,
    @SerializedName("materialId")
    val materialId: Int?,
    @SerializedName("imageLink")
    val imageLink: String?,
    @SerializedName("description")
    val description: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("options")
    val options: List<QuizOptionDto>
)

data class QuizOptionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("questionId")
    val questionId: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class SubmitQuizRequest(
    @SerializedName("quiz_submissions")
    val quizSubmissions: List<QuizAnswerDto>
)

data class QuizAnswerDto(
    @SerializedName("question_id")
    val questionId: Int,
    @SerializedName("answered_option_id")
    val answeredOptionId: Int
)

data class QuizResultDto(
    @SerializedName("passed")
    val passed: Boolean,
    @SerializedName("score")
    val score: Int,
    @SerializedName("correctCount")
    val correctCount: Int,
    @SerializedName("totalQuestions")
    val totalQuestions: Int
)
