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
        category: String?
    ): Result<List<RecommendActivityDto>> {
        return handleApiCall {
            recommendApi.getRecommendActivities(
                cursor = cursor,
                size = size,
                location = location,
                title = title,
                category = category
            )
        }.map { response ->
            response.content
        }
    }
}
