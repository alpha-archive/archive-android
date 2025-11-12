package com.alpha.archive.feature.home.record.input.data.repository

import com.alpha.archive.core.repository.BaseRepository
import com.alpha.archive.feature.home.record.data.remote.ActivityApi
import com.alpha.archive.feature.home.record.data.remote.dto.ActivityDetailDto
import com.alpha.archive.feature.home.record.data.remote.dto.UpdateActivityRequest
import com.alpha.archive.feature.home.record.input.data.remote.RecordInputApi
import com.alpha.archive.feature.home.record.input.data.remote.dto.ImageUploadData
import com.alpha.archive.feature.home.record.input.data.remote.dto.RecordInputRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordInputRepositoryImpl @Inject constructor(
    private val api: RecordInputApi,
    private val activityApi: ActivityApi
) : BaseRepository(), RecordInputRepository {

    override suspend fun createRecord(request: RecordInputRequest): Result<Unit> {
        return handleUnitApiCall {
            api.createRecord(request)
        }
    }

    override suspend fun updateRecord(activityId: String, request: RecordInputRequest, addImageIds: List<String>, removeImageIds: List<String>): Result<Unit> {
        val updateRequest = UpdateActivityRequest(
            title = request.title,
            category = request.category,
            location = request.location ?: "",
            activityDate = request.activityDate ?: "",
            rating = request.rating ?: 0,
            memo = request.memo,
            addImageIds = addImageIds,
            removeImageIds = removeImageIds
        )
        return handleApiCall { activityApi.updateActivity(activityId, updateRequest) }.map { }
    }

    override suspend fun getActivityForEdit(activityId: String): Result<ActivityDetailDto> {
        return handleApiCall { activityApi.getActivity(activityId) }
    }

    override suspend fun uploadImage(file: File): Result<ImageUploadData> {
        val mediaType = when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            else -> "image/jpeg"
        }

        val requestFile = RequestBody.create(mediaType.toMediaTypeOrNull(), file)
        val safeFileName = file.name.replace(" ", "_").replace("[^a-zA-Z0-9._-]".toRegex(), "_")
        val body = MultipartBody.Part.createFormData("file", safeFileName, requestFile)

        return handleApiCall {
            api.uploadImage(body)
        }
    }

    override suspend fun getTempImages(): Result<List<ImageUploadData>> {
        return handleApiCall {
            api.getTempImages()
        }
    }

    override suspend fun deleteImage(id: String): Result<Unit> {
        return handleUnitApiCall {
            api.deleteImage(id)
        }
    }
}
