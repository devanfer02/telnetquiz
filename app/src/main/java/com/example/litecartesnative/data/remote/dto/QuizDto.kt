package com.example.litecartesnative.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QuizDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("chapterId")
    val chapterId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("questions")
    val questions: List<QuizQuestionDto>
)

data class QuizQuestionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("quizId")
    val quizId: Int,
    @SerializedName("options")
    val options: List<QuizOptionDto>
)

data class QuizOptionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("questionId")
    val questionId: Int
)

data class SubmitQuizRequest(
    @SerializedName("answers")
    val answers: List<QuizAnswerDto>
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
