package com.example.archiveandroid.core.network

import com.example.archiveandroid.feature.home.record.data.remote.ActivityApi
import com.example.archiveandroid.feature.home.recorddetail.data.remote.RecordDetailApi
import com.example.archiveandroid.feature.home.record.input.data.remote.RecordInputApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideActivityApi(
        retrofit: Retrofit
    ): ActivityApi {
        return retrofit.create(ActivityApi::class.java)
    }
    
    
    @Provides
    @Singleton
    fun provideRecordDetailApi(
        retrofit: Retrofit
    ): RecordDetailApi {
        return retrofit.create(RecordDetailApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideRecordInputApi(
        retrofit: Retrofit
    ): RecordInputApi {
        return retrofit.create(RecordInputApi::class.java)
    }
}
