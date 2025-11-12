package com.alpha.archive.feature.home.recorddetail.data.repository

import com.alpha.archive.feature.home.record.data.remote.dto.ActivityDto

interface RecordDetailRepository {
    suspend fun getActivityDetail(id: String): Result<ActivityDto>
}
