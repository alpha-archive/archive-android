package com.example.archiveandroid.feature.home.record.data.repository

import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityListResponse
import com.example.archiveandroid.feature.home.record.data.remote.dto.CreateActivityRequest
import com.example.archiveandroid.feature.home.record.data.remote.dto.UpdateActivityRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface ActivityRepository {
    suspend fun getActivities(
        limit: Int = 20,
        cursor: String? = null,
        category: String? = null
    ): Result<ActivityListResponse>
    
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
