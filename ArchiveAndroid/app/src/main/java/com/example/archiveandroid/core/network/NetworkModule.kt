package com.example.archiveandroid.core.network

import com.example.archiveandroid.feature.home.chatbot.data.remote.ChatbotApi
import com.example.archiveandroid.feature.home.record.data.remote.ActivityApi
import com.example.archiveandroid.feature.home.record.input.data.remote.RecordInputApi
import com.example.archiveandroid.feature.home.recorddetail.data.remote.RecordDetailApi
import com.example.archiveandroid.feature.home.recommend.data.remote.RecommendApi
import com.example.archiveandroid.feature.home.stats.data.remote.StatsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideActivityApi(
        @Named("AppRetrofit") retrofit: Retrofit
    ): ActivityApi {
        return retrofit.create(ActivityApi::class.java)
    }
    
    
    @Provides
    @Singleton
    fun provideRecordDetailApi(
        @Named("AppRetrofit") retrofit: Retrofit
    ): RecordDetailApi {
        return retrofit.create(RecordDetailApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideRecordInputApi(
        @Named("AppRetrofit") retrofit: Retrofit
    ): RecordInputApi {
        return retrofit.create(RecordInputApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideRecommendApi(
        @Named("AppRetrofit") retrofit: Retrofit
    ): RecommendApi {
        return retrofit.create(RecommendApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideChatbotApi(
        @Named("AppRetrofit") retrofit: Retrofit
    ): ChatbotApi {
        return retrofit.create(ChatbotApi::class.java)
    }
       
    @Provides
    @Singleton
    fun provideStatsApi(
        @Named("AppRetrofit") retrofit: Retrofit
    ): StatsApi {
        return retrofit.create(StatsApi::class.java)
    }
}
