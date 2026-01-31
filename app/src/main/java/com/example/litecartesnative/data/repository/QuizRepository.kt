package com.example.litecartesnative.data.repository

import com.example.litecartesnative.data.remote.api.TelNetQuizApi
import com.example.litecartesnative.data.remote.dto.QuizAnswerDto
import com.example.litecartesnative.data.remote.dto.QuizDto
import com.example.litecartesnative.data.remote.dto.QuizResultDto
import com.example.litecartesnative.data.remote.dto.SubmitQuizRequest
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
            val response = api.submitQuizAnswers(quizId, SubmitQuizRequest(quizSubmissions = answers))
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
}
