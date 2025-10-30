package com.alpha.archive.feature.home.record.input.data.repository

import com.alpha.archive.BuildConfig
import com.alpha.archive.feature.home.record.input.data.remote.KakaoPlaceApi
import com.alpha.archive.feature.home.record.input.data.remote.dto.Place
import com.alpha.archive.feature.home.record.input.data.remote.dto.toPlace
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceSearchRepositoryImpl @Inject constructor(
    private val kakaoPlaceApi: KakaoPlaceApi
) : PlaceSearchRepository {

    override suspend fun searchPlaces(
        query: String,
        longitude: String?,
        latitude: String?,
        radius: Int?,
        page: Int?,
        size: Int?
    ): Result<List<Place>> {
        return try {
            val response = kakaoPlaceApi.searchPlaces(
                authorization = "KakaoAK ${BuildConfig.kakaoAppKey}",
                query = query,
                longitude = longitude,
                latitude = latitude,
                radius = radius,
                page = page,
                size = size ?: 15
            )
            
            if (response.isSuccessful) {
                val places = response.body()?.documents?.map { it.toPlace() } ?: emptyList()
                Result.success(places)
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}