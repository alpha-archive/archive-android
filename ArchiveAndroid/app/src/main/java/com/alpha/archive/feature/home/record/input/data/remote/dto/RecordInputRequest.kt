package com.alpha.archive.feature.home.record.input.data.remote.dto

data class RecordInputRequest(
    val title: String,
    val category: String,
    val location: String?,
    val activityDate: String?,
    val rating: Int?,
    val memo: String?,
    val imageIds: List<String> = emptyList(),
    // val publicEventId: String? = null
)
