package com.alpha.archiveandroid.feature.home.record.input.data.remote

import com.alpha.archiveandroid.feature.home.record.input.data.remote.dto.KakaoPlaceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoPlaceApi {
    
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlaces(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("x") longitude: String? = null,
        @Query("y") latitude: String? = null,
        @Query("radius") radius: Int? = null,
        @Query("rect") rect: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("category_group_code") categoryGroupCode: String? = null
    ): Response<KakaoPlaceResponse>
}
