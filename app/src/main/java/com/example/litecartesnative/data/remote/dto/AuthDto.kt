package com.example.litecartesnative.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("school_id")
    val schoolId: Int,
    @SerializedName("gender")
    val gender: Boolean,
    @SerializedName("grade")
    val grade: String
)

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
