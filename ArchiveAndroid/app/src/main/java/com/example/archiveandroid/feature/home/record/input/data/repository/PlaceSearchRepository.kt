package com.example.archiveandroid.feature.home.record.input.data.repository

import com.example.archiveandroid.feature.home.record.input.data.remote.dto.PlaceSearchResponse

interface PlaceSearchRepository {
    suspend fun searchPlaces(query: String): Result<PlaceSearchResponse>
}
