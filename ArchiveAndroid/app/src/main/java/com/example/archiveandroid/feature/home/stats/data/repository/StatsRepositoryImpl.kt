package com.example.archiveandroid.feature.home.stats.data.repository

import com.example.archiveandroid.core.repository.BaseRepository
import com.example.archiveandroid.feature.home.stats.data.remote.StatsApi
import com.example.archiveandroid.feature.home.stats.data.remote.dto.OverallStatisticsResponse
import com.example.archiveandroid.feature.home.stats.data.remote.dto.WeeklyStatisticsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val statsApi: StatsApi
) : BaseRepository(), StatsRepository {
    
    override suspend fun getOverallStatistics(): Result<OverallStatisticsResponse> {
        return handleApiCall {
            statsApi.getOverallStatistics()
        }
    }
    
    override suspend fun getWeeklyStatistics(): Result<WeeklyStatisticsResponse> {
        return handleApiCall {
            statsApi.getWeeklyStatistics()
        }
    }
}

