package com.example.archiveandroid.feature.home.recorddetail.data.remote

import com.example.archiveandroid.core.network.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecordDetailApi {
    @GET("/api/activities/{id}")
    suspend fun getActivityDetail(
        @Path("id") id: String
    ): Response<ApiResponse<Any>> // TODO: 실제 RecordDetail DTO로 교체
}
