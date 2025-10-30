package com.alpha.archive.core.network.dto

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
