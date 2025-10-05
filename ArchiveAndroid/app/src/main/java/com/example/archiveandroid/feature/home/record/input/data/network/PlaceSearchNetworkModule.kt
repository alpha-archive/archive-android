package com.example.archiveandroid.feature.home.record.input.data.network

import com.example.archiveandroid.feature.home.record.input.data.remote.PlaceSearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * PlaceSearch feature에서 사용하는 네트워크 관련 의존성을 제공하는 모듈
 * 
 * - PlaceSearchApi: 카카오 장소 검색 API
 */
@Module
@InstallIn(SingletonComponent::class)
object PlaceSearchNetworkModule {
    
    @Provides
    @Singleton
    @Named("KakaoOkHttp")
    fun providePlaceSearchOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        if (com.example.archiveandroid.BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @Named("KakaoRetrofit")
    fun providePlaceSearchRetrofit(
        @Named("KakaoOkHttp") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun providePlaceSearchApi(
        @Named("KakaoRetrofit") retrofit: Retrofit
    ): PlaceSearchApi =
        retrofit.create(PlaceSearchApi::class.java)
}
