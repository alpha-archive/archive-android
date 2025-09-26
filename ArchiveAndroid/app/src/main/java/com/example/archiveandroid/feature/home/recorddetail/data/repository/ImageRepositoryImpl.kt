package com.example.archiveandroid.feature.home.recorddetail.data.repository

import com.example.archiveandroid.core.repository.BaseRepository
import com.example.archiveandroid.feature.home.recorddetail.data.remote.ImageApi
import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ImageData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val api: ImageApi
) : BaseRepository(), ImageRepository {

    override suspend fun uploadImage(file: File): Result<ImageData> {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return handleApiCall {
            api.uploadImage(body)
        }
    }

    override suspend fun getTempImages(): Result<List<ImageData>> {
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
