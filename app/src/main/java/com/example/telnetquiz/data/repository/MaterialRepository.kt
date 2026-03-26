package com.example.telnetquiz.data.repository

import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import com.example.telnetquiz.data.remote.dto.BulkMaterialsRequest
import com.example.telnetquiz.data.remote.dto.StudyMaterialDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialRepository @Inject constructor(
    private val api: TelNetQuizApi
) {
    suspend fun getStudyMaterial(id: Int): Result<StudyMaterialDto> {
        return try {
            val response = api.getStudyMaterial(id)
            if (response.isSuccessful) {
                val material = response.body()?.data
                if (material != null) {
                    Result.Success(material)
                } else {
                    Result.Error("Invalid study material response")
                }
            } else {
                Result.Error("Failed to fetch study material")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun bulkGetMaterials(materialIds: List<Int>): Result<List<StudyMaterialDto>> {
        return try {
            val response = api.bulkGetMaterials(BulkMaterialsRequest(materialIds = materialIds))
            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.materials)
                } else {
                    Result.Error("Invalid bulk materials response")
                }
            } else {
                Result.Error("Failed to fetch materials")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
