package com.example.archiveandroid.feature.record.data.remote.dto

data class ActivityCreateRequest(
    val title: String,
    val category: String,
    val location: String?,
    val activityDate: String?,
    val rating: Int?,
    val memo: String?,
    val imageIds: List<String> = emptyList(),
    val publicEventId: String?
)
