package com.example.archiveandroid.feature.home.recorddetail.data.repository

import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ActivityCreateRequest

interface RecordDetailRepository {
    suspend fun createActivity(req: ActivityCreateRequest): Result<Unit>
}
