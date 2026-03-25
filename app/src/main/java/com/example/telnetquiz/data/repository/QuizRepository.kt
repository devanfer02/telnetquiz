package com.example.telnetquiz.data.repository

import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import com.example.telnetquiz.data.remote.dto.QuizAnswerDto
import com.example.telnetquiz.data.remote.dto.QuizDto
import com.example.telnetquiz.data.remote.dto.QuizMaterialsRequest
import com.example.telnetquiz.data.remote.dto.QuizMaterialsResponse
import com.example.telnetquiz.data.remote.dto.QuizResultDto
import com.example.telnetquiz.data.remote.dto.SubmitQuizRequest
import com.example.telnetquiz.data.remote.dto.VerifyAnswerRequest
import com.example.telnetquiz.data.remote.dto.VerifyAnswerResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val api: TelNetQuizApi
) {
    suspend fun getQuizById(id: Int): Result<QuizDto> {
        return try {
            val response = api.getQuizById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error("Quiz not found")
                }
            } else {
                Result.Error(response.message() ?: "Failed to fetch quiz", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun submitQuizAnswers(quizId: Int, answers: List<QuizAnswerDto>): Result<QuizResultDto> {
        return try {
            val response = api.submitQuizAnswers(quizId, SubmitQuizRequest(answers = answers))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error("Invalid response from server")
                }
            } else {
                Result.Error(response.message() ?: "Failed to submit quiz", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun verifyAnswer(quizId: Int, questionId: Int, answeredOptionId: Int): Result<VerifyAnswerResponse> {
        return try {
            val response = api.verifyAnswer(
                VerifyAnswerRequest(
                    quizId = quizId,
                    questionId = questionId,
                    answeredOptionId = answeredOptionId
                )
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error("Invalid verify response")
                }
            } else {
                Result.Error(response.message() ?: "Failed to verify answer", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getQuizMaterials(quizId: Int): Result<QuizMaterialsResponse> {
        return try {
            val response = api.getQuizMaterials(QuizMaterialsRequest(quizId = quizId))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error("No materials found")
                }
            } else {
                Result.Error(response.message() ?: "Failed to fetch materials", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
