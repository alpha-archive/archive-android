package com.example.archiveandroid.feature.record.data.repository

import com.example.archiveandroid.feature.record.data.remote.dto.ActivityCreateRequest

interface RecordRepository {
    suspend fun createActivity(req: ActivityCreateRequest): Result<Unit>
}
