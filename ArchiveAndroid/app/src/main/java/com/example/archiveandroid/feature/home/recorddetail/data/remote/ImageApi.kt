package com.example.archiveandroid.feature.home.recorddetail.data.remote

import com.example.archiveandroid.core.network.dto.ApiResponse
import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ImageData
import okhttp3.MultipartBody
import retrofit2.Response
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
    ): Response<ApiResponse<ImageData>>

    @GET("/api/images/temp")
    suspend fun getTempImages(): Response<ApiResponse<List<ImageData>>>

    @DELETE("/api/images/{imageId}")
    suspend fun deleteImage(
        @Path("imageId") imageId: String
    ): Response<ApiResponse<Unit>>
}
