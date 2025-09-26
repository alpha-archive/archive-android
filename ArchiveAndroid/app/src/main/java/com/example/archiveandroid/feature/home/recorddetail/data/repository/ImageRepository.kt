package com.example.archiveandroid.feature.home.recorddetail.data.repository

import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ImageData
import java.io.File

interface ImageRepository {
    suspend fun uploadImage(file: File): Result<ImageData>
    suspend fun getTempImages(): Result<List<ImageData>>
    suspend fun deleteImage(id: String): Result<Unit>
}
