package com.alpha.archive.feature.home.record.data.repository

import com.alpha.archive.feature.home.record.data.remote.dto.ActivityDetailDto
import com.alpha.archive.feature.home.record.data.remote.dto.ActivityListItemDto
import com.alpha.archive.feature.home.record.data.remote.dto.CreateActivityRequest
import com.alpha.archive.feature.home.record.data.remote.dto.UpdateActivityRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivitiesFlow(): Flow<Result<List<ActivityListItemDto>>>
    
    suspend fun getActivities(): Result<List<ActivityListItemDto>>
    
    suspend fun getActivity(id: String): Result<ActivityDetailDto>
    
    suspend fun createActivity(request: CreateActivityRequest): Result<ActivityListItemDto>
    
    suspend fun updateActivity(id: String, request: UpdateActivityRequest): Result<ActivityListItemDto>
    
    suspend fun deleteActivity(id: String): Result<Unit>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityDataModule {
    @Binds
    abstract fun bindActivityRepository(
        impl: ActivityRepositoryImpl
    ): ActivityRepository
}
