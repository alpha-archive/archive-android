package com.alpha.archive.feature.home.recorddetail.data.repository

import com.alpha.archive.core.repository.BaseRepository
import com.alpha.archive.feature.home.record.data.remote.dto.ActivityDto
import com.alpha.archive.feature.home.recorddetail.data.remote.RecordDetailApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordDetailRepositoryImpl @Inject constructor(
    private val api: RecordDetailApi
) : BaseRepository(), RecordDetailRepository {

    override suspend fun getActivityDetail(id: String): Result<ActivityDto> {
        return handleApiCall {
            api.getActivityDetail(id)
        }
    }
}
