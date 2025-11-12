package com.alpha.archive.feature.home.recorddetail.data.remote

import com.alpha.archive.core.network.dto.ApiResponse
import com.alpha.archive.feature.home.record.data.remote.dto.ActivityDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecordDetailApi {
    @GET("/api/activities/{id}")
    suspend fun getActivityDetail(
        @Path("id") id: String
    ): Response<ApiResponse<ActivityDto>>
}
