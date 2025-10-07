package com.example.archiveandroid.feature.home.record.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.archiveandroid.core.repository.BaseRepository
import com.example.archiveandroid.feature.home.record.data.paging.RecordPagingSource
import com.example.archiveandroid.feature.home.record.data.remote.ActivityApi
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityListResponse
import com.example.archiveandroid.feature.home.record.data.remote.dto.CreateActivityRequest
import com.example.archiveandroid.feature.home.record.data.remote.dto.UpdateActivityRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepositoryImpl @Inject constructor(
    private val activityApi: ActivityApi
) : BaseRepository(), ActivityRepository {
    
    override fun getActivitiesFlow(): Flow<Result<List<ActivityDto>>> = flow {
        emit(getActivities())
    }
    
    override suspend fun getActivities(): Result<List<ActivityDto>> {
        return handleApiCall {
            activityApi.getActivities()
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
