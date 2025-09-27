package com.example.archiveandroid.feature.home.record.input.data.repository

import com.example.archiveandroid.feature.home.record.input.data.remote.dto.ImageUploadData
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.RecordInputRequest
import java.io.File

interface RecordInputRepository {
    suspend fun createRecord(request: RecordInputRequest): Result<Unit>
    suspend fun uploadImage(file: File): Result<ImageUploadData>
    suspend fun getTempImages(): Result<List<ImageUploadData>>
    suspend fun deleteImage(id: String): Result<Unit>
}
