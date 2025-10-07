package com.example.archiveandroid.feature.home.record.data.repository

import androidx.paging.PagingData
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityListResponse
import com.example.archiveandroid.feature.home.record.data.remote.dto.CreateActivityRequest
import com.example.archiveandroid.feature.home.record.data.remote.dto.UpdateActivityRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivitiesFlow(): Flow<Result<List<ActivityDto>>>
    
    suspend fun getActivities(): Result<List<ActivityDto>>
    
    suspend fun getActivity(id: String): Result<ActivityDto>
    
    suspend fun createActivity(request: CreateActivityRequest): Result<ActivityDto>
    
    suspend fun updateActivity(id: String, request: UpdateActivityRequest): Result<ActivityDto>
    
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
