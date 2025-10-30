package com.alpha.archive.feature.home.recommend.data.repository

import com.alpha.archive.feature.home.recommend.data.remote.dto.RecommendActivityDetailDto
import com.alpha.archive.feature.home.recommend.data.remote.dto.RecommendActivityDto
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface RecommendRepository {
    suspend fun getRecommendActivities(
        cursor: String? = null,
        size: Int? = null,
        location: String? = null,
        title: String? = null,
        category: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        city: String? = null,
        district: String? = null
    ): Result<List<RecommendActivityDto>>
    
    suspend fun getRecommendActivityDetail(id: String): Result<RecommendActivityDetailDto>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RecommendDataModule {
    @Binds
    abstract fun bindRecommendRepository(
        impl: RecommendRepositoryImpl
    ): RecommendRepository
}
