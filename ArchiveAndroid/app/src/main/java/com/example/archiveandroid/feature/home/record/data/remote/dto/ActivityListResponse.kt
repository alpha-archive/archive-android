package com.example.archiveandroid.feature.home.record.data.remote.dto

/**
 * Activity 목록 응답 DTO
 * Paging을 위한 cursor 정보 포함
 */
data class ActivityListResponse(
    val activities: List<ActivityDto>,
    val nextCursor: String?,
    val hasMore: Boolean
)
