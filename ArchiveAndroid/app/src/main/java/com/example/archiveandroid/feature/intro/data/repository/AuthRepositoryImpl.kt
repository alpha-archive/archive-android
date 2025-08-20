package com.example.archiveandroid.feature.intro.data.repository

import com.example.archiveandroid.feature.intro.data.remote.AuthApi
import com.example.archiveandroid.feature.intro.data.remote.dto.AppTokenResponse
import com.example.archiveandroid.feature.intro.data.remote.dto.KakaoLoginRequest
import jakarta.inject.Inject
import retrofit2.HttpException

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun loginWithKakao(kakaoAccessToken: String): Result<AppTokenResponse> = runCatching {
        val req = KakaoLoginRequest(kakaoAccessToken)

        val res = api.loginWithKakao(req)
        if (res.isSuccessful) {
            val body = res.body() ?: throw IllegalStateException("Empty body")
            if (body.success && body.data != null) body.data
            else throw IllegalStateException(body.message.ifBlank { "Request failed" })
        } else {
            throw HttpException(res)
        }
    }
}