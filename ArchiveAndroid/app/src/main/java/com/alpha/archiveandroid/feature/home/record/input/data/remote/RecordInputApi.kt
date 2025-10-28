package com.alpha.archiveandroid.feature.home.record.input.data.remote

import com.alpha.archiveandroid.core.network.dto.ApiResponse
import com.alpha.archiveandroid.feature.home.record.input.data.remote.dto.ImageUploadData
import com.alpha.archiveandroid.feature.home.record.input.data.remote.dto.RecordInputRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import okhttp3.MultipartBody

interface RecordInputApi {
    
    @POST("api/activities")
    suspend fun createRecord(@Body request: RecordInputRequest): Response<ApiResponse<Unit>>
    
    @Multipart
    @POST("api/images/upload")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<ApiResponse<ImageUploadData>>
    
    @GET("api/images/temp")
    suspend fun getTempImages(): Response<ApiResponse<List<ImageUploadData>>>
    
    @DELETE("api/images/{id}")
    suspend fun deleteImage(@Path("id") id: String): Response<ApiResponse<Unit>>
}
