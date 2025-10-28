package com.alpha.archiveandroid.feature.intro.data.network

import com.alpha.archiveandroid.feature.intro.data.remote.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Intro feature에서 사용하는 네트워크 관련 의존성을 제공하는 모듈
 * 
 * - AuthApi: 카카오 로그인 API
 * - 기타 Intro 관련 API들
 */
@Module
@InstallIn(SingletonComponent::class)
object IntroNetworkModule {
    
    @Provides
    @Singleton
    fun provideAuthApi(@Named("AppRetrofit") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)
}
