package com.example.archiveandroid.feature.home.stats.data.remote

import com.example.archiveandroid.core.network.dto.ApiResponse
import com.example.archiveandroid.feature.home.stats.data.remote.dto.OverallStatisticsResponse
import retrofit2.Response
import retrofit2.http.GET

interface StatsApi {
    @GET("api/activities/statistics/overall")
    suspend fun getOverallStatistics(): Response<ApiResponse<OverallStatisticsResponse>>
}

