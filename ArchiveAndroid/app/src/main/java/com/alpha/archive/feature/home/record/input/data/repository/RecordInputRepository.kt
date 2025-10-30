package com.alpha.archive.feature.home.record.input.data.repository

import com.alpha.archive.feature.home.record.data.remote.dto.ActivityDetailDto
import com.alpha.archive.feature.home.record.input.data.remote.dto.ImageUploadData
import com.alpha.archive.feature.home.record.input.data.remote.dto.RecordInputRequest
import java.io.File

interface RecordInputRepository {
    suspend fun createRecord(request: RecordInputRequest): Result<Unit>
    suspend fun updateRecord(activityId: String, request: RecordInputRequest, addImageIds: List<String>, removeImageIds: List<String>): Result<Unit>
    suspend fun getActivityForEdit(activityId: String): Result<ActivityDetailDto>
    suspend fun uploadImage(file: File): Result<ImageUploadData>
    suspend fun getTempImages(): Result<List<ImageUploadData>>
    suspend fun deleteImage(id: String): Result<Unit>
}
