package com.example.archiveandroid.core.network.dto

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
