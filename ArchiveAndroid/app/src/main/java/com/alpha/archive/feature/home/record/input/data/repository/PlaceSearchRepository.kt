package com.alpha.archive.feature.home.record.input.data.repository

import com.alpha.archive.feature.home.record.input.data.remote.dto.Place
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface PlaceSearchRepository {
    suspend fun searchPlaces(
        query: String,
        longitude: String? = null,
        latitude: String? = null,
        radius: Int? = null,
        page: Int? = null,
        size: Int? = null
    ): Result<List<Place>>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaceSearchDataModule {
    @Binds
    abstract fun bindPlaceSearchRepository(
        impl: PlaceSearchRepositoryImpl
    ): PlaceSearchRepository
}