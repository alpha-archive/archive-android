package com.example.archiveandroid.di

import com.example.archiveandroid.feature.record.data.remote.ImageApi
import com.example.archiveandroid.feature.record.data.remote.RecordApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * API 통신에 필요한 네트워크 객체 제공
 */


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-url/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideImageApi(retrofit: Retrofit): ImageApi {
        return retrofit.create(ImageApi::class.java)
    }

    @Provides
    fun provideRecordApi(retrofit: Retrofit): RecordApi {
        return retrofit.create(RecordApi::class.java)
    }
}
