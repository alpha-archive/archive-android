package com.example.archiveandroid.feature.home.recommend.data.repository

import com.example.archiveandroid.core.repository.BaseRepository
import com.example.archiveandroid.feature.home.recommend.data.remote.RecommendApi
import com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendActivityDetailDto
import com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendActivityDto
import com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendRepositoryImpl @Inject constructor(
    private val recommendApi: RecommendApi
) : BaseRepository(), RecommendRepository {

    override suspend fun getRecommendActivities(
        cursor: String?,
        size: Int?,
        location: String?,
        title: String?,
        category: String?,
        startDate: String?,
        endDate: String?,
        city: String?,
        district: String?
    ): Result<List<RecommendActivityDto>> {
        return handleApiCall {
            recommendApi.getRecommendActivities(
                cursor = cursor,
                size = size,
                location = location,
                title = title,
                category = category,
                startDate = startDate,
                endDate = endDate,
                city = city,
                district = district
            )
        }.map { response ->
            response.content
        }
    }
    
    override suspend fun getRecommendActivityDetail(id: String): Result<RecommendActivityDetailDto> {
        return handleApiCall {
            recommendApi.getRecommendActivityDetail(id)
        }
    }
}
