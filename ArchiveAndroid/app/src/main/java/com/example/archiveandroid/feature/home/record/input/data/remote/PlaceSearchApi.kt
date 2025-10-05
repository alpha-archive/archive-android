package com.example.archiveandroid.feature.home.record.input.data.remote

import com.example.archiveandroid.feature.home.record.input.data.remote.dto.PlaceSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PlaceSearchApi {
    @GET("v2/local/search/keyword")
    suspend fun searchPlaces(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15
    ): PlaceSearchResponse
}
