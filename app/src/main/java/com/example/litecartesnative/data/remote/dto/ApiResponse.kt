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
