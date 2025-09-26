package com.example.archiveandroid.feature.home.recorddetail.data.repository

import com.example.archiveandroid.core.repository.BaseRepository
import com.example.archiveandroid.feature.home.recorddetail.data.remote.RecordDetailApi
import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ActivityCreateRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordDetailRepositoryImpl @Inject constructor(
    private val api: RecordDetailApi
) : BaseRepository(), RecordDetailRepository {

    override suspend fun createActivity(req: ActivityCreateRequest): Result<Unit> {
        return handleUnitApiCall {
            api.createActivity(req)
        }
    }
}
