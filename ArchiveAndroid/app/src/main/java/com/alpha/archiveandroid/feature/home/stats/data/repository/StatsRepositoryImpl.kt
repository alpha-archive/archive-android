package com.alpha.archiveandroid.feature.home.stats.data.repository

import com.alpha.archiveandroid.core.repository.BaseRepository
import com.alpha.archiveandroid.feature.home.stats.data.remote.StatsApi
import com.alpha.archiveandroid.feature.home.stats.data.remote.dto.MonthlyStatisticsResponse
import com.alpha.archiveandroid.feature.home.stats.data.remote.dto.OverallStatisticsResponse
import com.alpha.archiveandroid.feature.home.stats.data.remote.dto.WeeklyStatisticsResponse
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
    
    override suspend fun getMonthlyStatistics(yearMonth: String): Result<MonthlyStatisticsResponse> {
        return handleApiCall {
            statsApi.getMonthlyStatistics(yearMonth)
        }
    }
}

