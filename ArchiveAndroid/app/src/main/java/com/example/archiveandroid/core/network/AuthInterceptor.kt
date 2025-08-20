package com.example.archiveandroid.core.network

import com.example.archiveandroid.core.storage.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 네트워크 요청에 자동으로 Authorization 헤더를 추가하는 Interceptor
 *
 * - Kakao 로그인 요청(/api/auth/kakao/login)은 토큰 필요 없음 → 그대로 진행
 * - 그 외 요청은 TokenStore에서 accessToken을 가져와 헤더에 Bearer 토큰 추가
 * - Retrofit + OkHttp 클라이언트에 주입하여 모든 API 호출에 공통 적용
 */
class AuthInterceptor @Inject constructor(private val tokenStore: TokenStore): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath.startsWith("/api/auth/kakao/login")) {
            return chain.proceed(request)
        }
        val access = runBlocking { tokenStore.access() }
        val newReq = if (!access.isNullOrBlank()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $access")
                .build()
        } else request
        return chain.proceed(newReq)
    }
}