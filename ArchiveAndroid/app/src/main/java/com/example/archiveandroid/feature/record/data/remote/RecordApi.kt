package com.example.archiveandroid.feature.record.data.remote

import com.example.archiveandroid.feature.intro.data.remote.dto.ApiResponse
import com.example.archiveandroid.feature.record.data.remote.dto.ActivityCreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RecordApi {
    @POST("/api/activities")
    suspend fun createActivity(
        @Body body: ActivityCreateRequest
    ): Response<ApiResponse<Unit>>
}
