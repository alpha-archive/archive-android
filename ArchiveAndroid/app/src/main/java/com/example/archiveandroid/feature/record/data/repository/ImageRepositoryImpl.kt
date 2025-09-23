package com.example.archiveandroid.feature.record.data.repository

import com.example.archiveandroid.feature.record.data.remote.ImageApi
import com.example.archiveandroid.feature.record.data.remote.dto.ImageUploadResponse
import com.example.archiveandroid.feature.record.data.remote.dto.TempImageResponse
import com.example.archiveandroid.feature.record.data.remote.dto.BaseResponse
import jakarta.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ImageRepositoryImpl @Inject constructor(
    private val api: ImageApi
) : ImageRepository {

    override suspend fun uploadImage(file: File): ImageUploadResponse {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return api.uploadImage(body)
    }

    override suspend fun getTempImages(): TempImageResponse {
        return api.getTempImages()
    }

    override suspend fun deleteImage(id: String): BaseResponse {
        return api.deleteImage(id)
    }
}
