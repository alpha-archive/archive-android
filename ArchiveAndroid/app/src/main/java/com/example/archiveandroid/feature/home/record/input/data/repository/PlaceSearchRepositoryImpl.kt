package com.example.archiveandroid.feature.home.record.input.data.repository

import com.example.archiveandroid.BuildConfig
import com.example.archiveandroid.feature.home.record.input.data.remote.PlaceSearchApi
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.PlaceSearchResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceSearchRepositoryImpl @Inject constructor(
    private val placeSearchApi: PlaceSearchApi
) : PlaceSearchRepository {
    
    override suspend fun searchPlaces(query: String): Result<PlaceSearchResponse> {
        return try {
            val apiKey = BuildConfig.kakaoAppKey.ifEmpty { BuildConfig.kakaoAppKey }
            android.util.Log.d("PlaceSearch", "Using API Key: $apiKey")
            
            val response = placeSearchApi.searchPlaces(
                authorization = "KakaoAK $apiKey",
                query = query,
                page = 1,
                size = 15
            )
            Result.success(response)
        } catch (e: Exception) {
            android.util.Log.e("PlaceSearch", "Error: ${e.message}", e)
            Result.failure(e)
        }
    }
}
