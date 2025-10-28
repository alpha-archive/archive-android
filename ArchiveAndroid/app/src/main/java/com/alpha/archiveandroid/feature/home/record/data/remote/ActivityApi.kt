package com.alpha.archiveandroid.feature.home.record.data.remote

import com.alpha.archiveandroid.core.network.dto.ApiResponse
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.ActivityDetailDto
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.CreateActivityRequest
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.UpdateActivityRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ActivityApi {
    @GET("api/activities")
    suspend fun getActivities(): Response<ApiResponse<List<ActivityDto>>>
    
    @GET("api/activities/{id}")
    suspend fun getActivity(
        @Path("id") id: String
    ): Response<ApiResponse<ActivityDetailDto>>
    
    @POST("/api/activities")
    suspend fun createActivity(
        @Body request: CreateActivityRequest
    ): Response<ApiResponse<ActivityDto>>
    
    @PUT("/api/activities/{id}")
    suspend fun updateActivity(
        @Path("id") id: String,
        @Body request: UpdateActivityRequest
    ): Response<ApiResponse<ActivityDto>>
    
    @DELETE("/api/activities/{id}")
    suspend fun deleteActivity(
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>
}
