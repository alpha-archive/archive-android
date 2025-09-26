package com.example.archiveandroid.feature.home.recorddetail.data.remote

import com.example.archiveandroid.core.network.dto.ApiResponse
import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ActivityCreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RecordDetailApi {
    @POST("/api/activities")
    suspend fun createActivity(
        @Body body: ActivityCreateRequest
    ): Response<ApiResponse<Unit>>
}
