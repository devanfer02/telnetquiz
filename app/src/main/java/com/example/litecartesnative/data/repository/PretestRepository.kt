package com.example.litecartesnative.data.repository

import com.example.litecartesnative.data.remote.api.TelNetQuizApi
import com.example.litecartesnative.data.remote.dto.PretestQuestionDto
import com.example.litecartesnative.data.remote.dto.PretestSubmissionDto
import com.example.litecartesnative.data.remote.dto.SubmitPretestRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PretestRepository @Inject constructor(
    private val api: TelNetQuizApi
) {
    suspend fun getPretestQuestions(): Result<List<PretestQuestionDto>> {
        return try {
            val response = api.getPretestQuestions()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.data != null) {
                    Result.Success(body.data.questions)
                } else {
                    Result.Error("No pretest questions found")
                }
            } else {
                Result.Error(response.message() ?: "Failed to fetch pretest", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun submitPretestAnswers(answers: List<PretestSubmissionDto>): Result<String> {
        return try {
            val response = api.submitPretestAnswers(SubmitPretestRequest(answers))
            if (response.isSuccessful) {
                val body = response.body()
                Result.Success(body?.message ?: "Pretest submitted successfully")
            } else {
                Result.Error(response.message() ?: "Failed to submit pretest", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
