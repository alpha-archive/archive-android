package com.example.archiveandroid.feature.record.data.repository

import com.example.archiveandroid.feature.record.data.remote.dto.BaseResponse
import com.example.archiveandroid.feature.record.data.remote.dto.ImageUploadResponse
import com.example.archiveandroid.feature.record.data.remote.dto.TempImageResponse
import java.io.File

interface ImageRepository {
    suspend fun uploadImage(file: File): ImageUploadResponse
    suspend fun getTempImages(): TempImageResponse
    suspend fun deleteImage(id: String): BaseResponse
}
