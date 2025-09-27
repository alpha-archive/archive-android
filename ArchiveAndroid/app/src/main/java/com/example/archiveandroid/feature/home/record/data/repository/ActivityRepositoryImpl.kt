package com.example.archiveandroid.feature.home.record.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.archiveandroid.core.repository.BaseRepository
import com.example.archiveandroid.feature.home.record.data.paging.DummyRecordPagingSource
import com.example.archiveandroid.feature.home.record.data.remote.ActivityApi
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityListResponse
import com.example.archiveandroid.feature.home.record.data.remote.dto.CreateActivityRequest
import com.example.archiveandroid.feature.home.record.data.remote.dto.UpdateActivityRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepositoryImpl @Inject constructor(
    private val activityApi: ActivityApi
) : BaseRepository(), ActivityRepository {
    
    override fun getActivitiesPaging(): Flow<PagingData<ActivityDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // 더미 데이터이므로 작은 페이지 크기
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
            pagingSourceFactory = { DummyRecordPagingSource() }
        ).flow
    }
    
    override suspend fun getActivities(
        limit: Int,
        cursor: String?,
        category: String?
    ): Result<ActivityListResponse> {
        return handleApiCall {
            activityApi.getActivities(limit, cursor, category)
        }
    }
    
    override suspend fun getActivity(id: String): Result<ActivityDto> {
        return handleApiCall {
            activityApi.getActivity(id)
        }
    }
    
    override suspend fun createActivity(request: CreateActivityRequest): Result<ActivityDto> {
        return handleApiCall {
            activityApi.createActivity(request)
        }
    }
    
    override suspend fun updateActivity(id: String, request: UpdateActivityRequest): Result<ActivityDto> {
        return handleApiCall {
            activityApi.updateActivity(id, request)
        }
    }
    
    override suspend fun deleteActivity(id: String): Result<Unit> {
        return handleUnitApiCall {
            activityApi.deleteActivity(id)
        }
    }
}
