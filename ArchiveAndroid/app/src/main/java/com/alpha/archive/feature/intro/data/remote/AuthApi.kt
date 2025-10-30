package com.alpha.archive.feature.intro.data.remote

import com.alpha.archive.core.network.dto.ApiResponse
import com.alpha.archive.feature.intro.data.remote.dto.AppTokenResponse
import com.alpha.archive.feature.intro.data.remote.dto.KakaoLoginRequest
import com.alpha.archive.feature.intro.data.remote.dto.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/kakao/login")
    suspend fun loginWithKakao(
        @Body body: KakaoLoginRequest
    ): Response<ApiResponse<AppTokenResponse>>
    
    @POST("/api/auth/refresh")
    suspend fun refreshToken(
        @Body body: RefreshTokenRequest
    ): Response<ApiResponse<AppTokenResponse>>
}