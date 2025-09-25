package com.example.archiveandroid.feature.record.data.remote

import com.example.archiveandroid.feature.record.data.remote.dto.BaseResponse
import com.example.archiveandroid.feature.record.data.remote.dto.ImageUploadResponse
import com.example.archiveandroid.feature.record.data.remote.dto.TempImageResponse
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ImageApi {
    @Multipart
    @POST("/api/images/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): ImageUploadResponse

    @GET("/api/images/temp")
    suspend fun getTempImages(): TempImageResponse

    @DELETE("/api/images/{imageId}")
    suspend fun deleteImage(
        @Path("imageId") imageId: String
    ): BaseResponse
}
