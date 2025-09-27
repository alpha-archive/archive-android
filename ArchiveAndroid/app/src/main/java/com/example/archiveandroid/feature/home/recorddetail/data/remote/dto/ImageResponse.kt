package com.example.archiveandroid.feature.home.recorddetail.data.remote.dto

// 업로드 응답
data class ImageUploadResponse(
    val id: String,
    val imageKey: String,
    val imageUrl: String,
    val originalFilename: String,
    val fileSize: Long,
    val contentType: String,
    val status: String
)

// 임시 이미지 목록 조회
data class TempImageResponse(
    val success: Boolean,
    val message: String?,
    val data: List<ImageData>
)

// 이미지 데이터
data class ImageData(
    val id: String,
    val imageKey: String,
    val imageUrl: String,
    val originalFilename: String,
    val fileSize: Long,
    val contentType: String,
    val status: String
)

// 공통 응답
data class BaseResponse(
    val success: Boolean,
    val message: String?
)
