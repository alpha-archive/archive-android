package com.example.archiveandroid.feature.home.recommend.data.remote

import com.example.archiveandroid.core.network.dto.ApiResponse
import com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendActivityDetailDto
import com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendActivityListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendApi {
    
    @GET("api/recommendations/activities")
    suspend fun getRecommendActivities(
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: String? = null,
        @Query("title") title: String? = null,
        @Query("category") category: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("city") city: String? = null,
        @Query("district") district: String? = null
    ): Response<ApiResponse<RecommendActivityListResponse>>
    
    @GET("api/recommendations/activities/{activityId}")
    suspend fun getRecommendActivityDetail(
        @retrofit2.http.Path("activityId") activityId: String
    ): Response<ApiResponse<RecommendActivityDetailDto>>
}
