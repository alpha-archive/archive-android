package com.alpha.archive.feature.home.stats.data.repository

import com.alpha.archive.feature.home.stats.data.remote.dto.MonthlyStatisticsResponse
import com.alpha.archive.feature.home.stats.data.remote.dto.OverallStatisticsResponse
import com.alpha.archive.feature.home.stats.data.remote.dto.WeeklyStatisticsResponse

interface StatsRepository {
    suspend fun getOverallStatistics(): Result<OverallStatisticsResponse>
    suspend fun getWeeklyStatistics(): Result<WeeklyStatisticsResponse>
    suspend fun getMonthlyStatistics(yearMonth: String): Result<MonthlyStatisticsResponse>
}

