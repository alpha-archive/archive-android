package com.example.archiveandroid.feature.intro.data.repository

import com.example.archiveandroid.feature.intro.data.remote.dto.AppTokenResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface AuthRepository {
    suspend fun loginWithKakao(kakaoToken: String): Result<AppTokenResponse>
    suspend fun refreshToken(refreshToken: String): Result<AppTokenResponse>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataModule {
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}