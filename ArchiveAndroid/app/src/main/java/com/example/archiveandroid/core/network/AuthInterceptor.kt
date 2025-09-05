package com.example.archiveandroid.core.network

import com.example.archiveandroid.core.storage.TokenStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 네트워크 요청에 자동으로 Authorization 헤더를 추가하는 Interceptor
 *
 * - Kakao 로그인 요청(/api/auth/kakao/login)은 토큰 필요 없음 → 그대로 진행
 * - 그 외 요청은 TokenStore에서 accessToken을 가져와 헤더에 Bearer 토큰 추가
 * - 토큰 만료 시 자동으로 갱신 후 재요청
 * - Retrofit + OkHttp 클라이언트에 주입하여 모든 API 호출에 공통 적용
 */
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
    private val tokenRefreshService: TokenRefreshService
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // 인증이 필요하지 않은 요청들
        if (request.url.encodedPath.startsWith("/api/auth/kakao/login") ||
            request.url.encodedPath.startsWith("/api/auth/refresh")) {
            return chain.proceed(request)
        }
        
        // 동기적으로 토큰을 가져오기 위해 runBlocking 사용
        val access = kotlinx.coroutines.runBlocking { tokenStore.access() }
        val newReq = if (!access.isNullOrBlank()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $access")
                .build()
        } else request
        
        val response = chain.proceed(newReq)
        
        // 401 Unauthorized 응답 시 토큰 갱신 시도
        if (response.code == 401) {
            response.close()
            
            val refreshSuccess = kotlinx.coroutines.runBlocking { 
                tokenRefreshService.refreshToken() 
            }
            
            if (refreshSuccess) {
                // 토큰 갱신 성공 시 새로운 토큰으로 재요청
                val newAccess = kotlinx.coroutines.runBlocking { tokenStore.access() }
                val retryReq = request.newBuilder()
                    .addHeader("Authorization", "Bearer $newAccess")
                    .build()
                return chain.proceed(retryReq)
            } else {
                // 토큰 갱신 실패 시 토큰 삭제
                kotlinx.coroutines.runBlocking { 
                    tokenStore.clear() 
                }
            }
        }
        
        return response
    }
}