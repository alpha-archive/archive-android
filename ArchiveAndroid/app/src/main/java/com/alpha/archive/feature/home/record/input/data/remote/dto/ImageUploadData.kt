package com.alpha.archive.feature.home.record.input.data.remote.dto

data class ImageUploadData(
    val id: String,
    val imageKey: String,
    val imageUrl: String,
    val originalFilename: String,
    val fileSize: Long,
    val contentType: String,
    val status: String
)
