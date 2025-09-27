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
        // 파일 확장자에 따른 MediaType 설정
        val mediaType = when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            else -> "image/jpeg" // 기본값
        }
        
        val requestFile = RequestBody.create(mediaType.toMediaTypeOrNull(), file)
        // 파일명을 안전하게 처리 (한글 파일명 대응)
        val safeFileName = file.name.replace(" ", "_").replace("[^a-zA-Z0-9._-]".toRegex(), "_")
        val body = MultipartBody.Part.createFormData("file", safeFileName, requestFile)
        
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
