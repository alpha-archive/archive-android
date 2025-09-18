package com.example.archiveandroid.core.network

import com.example.archiveandroid.feature.intro.data.remote.AuthApi
import com.example.archiveandroid.feature.home.record.data.remote.ActivityApi
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
    fun provideAuthApi(
        retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideActivityApi(
        retrofit: Retrofit
    ): ActivityApi {
        return retrofit.create(ActivityApi::class.java)
    }
}
