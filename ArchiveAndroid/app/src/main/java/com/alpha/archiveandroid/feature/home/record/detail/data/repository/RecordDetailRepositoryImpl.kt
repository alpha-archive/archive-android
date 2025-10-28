package com.alpha.archiveandroid.feature.home.recorddetail.data.repository

import com.alpha.archiveandroid.core.repository.BaseRepository
import com.alpha.archiveandroid.feature.home.recorddetail.data.remote.RecordDetailApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordDetailRepositoryImpl @Inject constructor(
    private val api: RecordDetailApi
) : BaseRepository(), RecordDetailRepository {

    override suspend fun getActivityDetail(id: String): Result<Any> {
        return handleApiCall {
            api.getActivityDetail(id)
        }
    }
}
