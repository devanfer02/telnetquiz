package com.example.litecartesnative.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChapterDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("isUnlocked")
    val isUnlocked: Boolean,
    @SerializedName("progress")
    val progress: Int
)

data class ChapterDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)
