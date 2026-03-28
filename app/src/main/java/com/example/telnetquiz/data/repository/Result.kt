package com.example.telnetquiz.data.repository

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

fun Exception.toUserMessage(): String = when (this) {
    is SocketTimeoutException -> "Koneksi ke server terlalu lama. Periksa koneksi internet Anda dan coba lagi."
    is ConnectException -> "Tidak dapat terhubung ke server. Periksa koneksi internet Anda."
    is IOException -> "Terjadi gangguan jaringan. Periksa koneksi internet Anda."
    else -> message ?: "Terjadi kesalahan jaringan"
}

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Loading -> Loading
    }
}
