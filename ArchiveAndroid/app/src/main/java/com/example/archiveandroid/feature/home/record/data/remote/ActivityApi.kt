package com.example.archiveandroid.feature.home.record.data.remote

import com.example.archiveandroid.core.network.dto.ApiResponse
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityListResponse
import com.example.archiveandroid.feature.home.record.data.remote.dto.CreateActivityRequest
import com.example.archiveandroid.feature.home.record.data.remote.dto.UpdateActivityRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ActivityApi {
    @GET("/api/activities")
    suspend fun getActivities(
        @Query("limit") limit: Int = 20,
        @Query("cursor") cursor: String? = null,
        @Query("category") category: String? = null
    ): Response<ApiResponse<ActivityListResponse>>
    
    @GET("/api/activities/{id}")
    suspend fun getActivity(
        @Path("id") id: String
    ): Response<ApiResponse<ActivityDto>>
    
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
