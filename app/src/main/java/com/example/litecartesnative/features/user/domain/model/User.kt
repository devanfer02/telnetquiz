package com.example.litecartesnative.features.user.domain.model

data class User(
    val fullname: String = "",
    val handle: String = "",
    val school: String = "",
    val email: String = "",
    val password: String = "",
    val exp: Int = 0
)