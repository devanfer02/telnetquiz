package com.example.litecartesnative.features.pretest.domain.model

data class Pretest(
    val question: String,
    val options: List<String>,
    val answer: String = "",
    val imageId: Int? = null,
)