package com.example.archiveandroid.feature.home.stats.data.repository

import com.example.archiveandroid.feature.home.stats.data.remote.dto.OverallStatisticsResponse

interface StatsRepository {
    suspend fun getOverallStatistics(): Result<OverallStatisticsResponse>
}

