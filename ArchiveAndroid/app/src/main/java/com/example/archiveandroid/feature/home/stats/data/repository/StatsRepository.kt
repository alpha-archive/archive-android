package com.example.archiveandroid.feature.home.stats.data.repository

import com.example.archiveandroid.feature.home.stats.data.remote.dto.MonthlyStatisticsResponse
import com.example.archiveandroid.feature.home.stats.data.remote.dto.OverallStatisticsResponse
import com.example.archiveandroid.feature.home.stats.data.remote.dto.WeeklyStatisticsResponse

interface StatsRepository {
    suspend fun getOverallStatistics(): Result<OverallStatisticsResponse>
    suspend fun getWeeklyStatistics(): Result<WeeklyStatisticsResponse>
    suspend fun getMonthlyStatistics(yearMonth: String): Result<MonthlyStatisticsResponse>
}

