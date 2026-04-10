package com.example.telnetquiz.data.repository

import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import com.example.telnetquiz.data.remote.dto.PretestQuestionDto
import com.example.telnetquiz.data.remote.dto.PretestResultDto
import com.example.telnetquiz.data.remote.dto.PretestSubmissionDto
import com.example.telnetquiz.data.remote.dto.SubmitPretestRequest
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
                if (body?.data != null) {
                    Result.Success(body.data.questions)
                } else {
                    Result.Error("No pretest questions found")
                }
            } else {
                Result.Error(response.message() ?: "Failed to fetch pretest")
            }
        } catch (e: Exception) {
            Result.Error(e.toUserMessage())
        }
    }

    suspend fun submitPretestAnswers(answers: List<PretestSubmissionDto>): Result<PretestResultDto> {
        return try {
            val response = api.submitPretestAnswers(SubmitPretestRequest(answers))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error("Invalid response from server")
                }
            } else {
                Result.Error(response.message() ?: "Failed to submit pretest")
            }
        } catch (e: Exception) {
            Result.Error(e.toUserMessage())
        }
    }
}
