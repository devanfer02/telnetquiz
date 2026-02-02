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

data class LoginErrorResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("errors")
    val errors: String? = null
)

data class UserProfileDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("email")
    val email: String
)
