package com.alpha.archiveandroid.feature.home.record.data.repository

import com.alpha.archiveandroid.core.repository.BaseRepository
import com.alpha.archiveandroid.feature.home.record.data.remote.ActivityApi
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.ActivityDetailDto
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.CreateActivityRequest
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.UpdateActivityRequest
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
    
    override suspend fun getActivity(id: String): Result<ActivityDetailDto> {
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
