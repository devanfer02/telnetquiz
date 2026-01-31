package com.example.litecartesnative.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PretestQuestionsResponse(
    @SerializedName("questions")
    val questions: List<PretestQuestionDto>
)

data class PretestQuestionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("chapter_id")
    val chapterId: Int,
    @SerializedName("image_link")
    val imageLink: String?,
    @SerializedName("description")
    val description: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("options")
    val options: List<PretestOptionDto>
)

data class PretestOptionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("text")
    val text: String
)

data class SubmitPretestRequest(
    @SerializedName("pretest_submissions")
    val pretestSubmissions: List<PretestSubmissionDto>
)

data class PretestSubmissionDto(
    @SerializedName("question_id")
    val questionId: Int,
    @SerializedName("answered_option_id")
    val answeredOptionId: Int
)
