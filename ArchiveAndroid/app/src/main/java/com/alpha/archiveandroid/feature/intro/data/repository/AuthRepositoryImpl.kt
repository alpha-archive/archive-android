package com.alpha.archiveandroid.feature.intro.data.repository

import com.alpha.archiveandroid.core.repository.BaseRepository
import com.alpha.archiveandroid.feature.intro.data.remote.AuthApi
import com.alpha.archiveandroid.feature.intro.data.remote.dto.AppTokenResponse
import com.alpha.archiveandroid.feature.intro.data.remote.dto.KakaoLoginRequest
import com.alpha.archiveandroid.feature.intro.data.remote.dto.RefreshTokenRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : BaseRepository(), AuthRepository {

    override suspend fun loginWithKakao(kakaoAccessToken: String): Result<AppTokenResponse> {
        return handleApiCall {
            api.loginWithKakao(KakaoLoginRequest(kakaoAccessToken))
        }
    }
    
    override suspend fun refreshToken(refreshToken: String): Result<AppTokenResponse> {
        return handleApiCall {
            api.refreshToken(RefreshTokenRequest(refreshToken))
        }
    }
}