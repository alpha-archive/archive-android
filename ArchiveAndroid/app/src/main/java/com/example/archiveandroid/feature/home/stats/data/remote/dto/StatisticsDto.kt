package com.example.archiveandroid.feature.home.stats.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OverallStatisticsResponse(
    @SerializedName("totalActivities")
    val totalActivities: Int,
    @SerializedName("categoryStats")
    val categoryStats: List<CategoryStatDto>
)

data class CategoryStatDto(
    @SerializedName("category")
    val category: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("percentage")
    val percentage: Double
)

