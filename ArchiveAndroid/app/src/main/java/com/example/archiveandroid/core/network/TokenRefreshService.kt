package com.example.archiveandroid.core.network

import com.example.archiveandroid.core.storage.TokenStore
import com.example.archiveandroid.BuildConfig
import com.example.archiveandroid.core.network.dto.ApiResponse
import com.example.archiveandroid.feature.intro.data.remote.dto.AppTokenResponse
import com.example.archiveandroid.feature.intro.data.remote.dto.RefreshTokenRequest
import com.google.gson.GsonBuilder
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 토큰 갱신을 담당하는 서비스
 * 
 * - 토큰 만료 시 자동으로 refresh token을 사용해 새로운 토큰 발급
 * - 동시 요청 시 중복 갱신 방지
 * - 갱신 실패 시 로그아웃 처리
 */
@Singleton
class TokenRefreshService @Inject constructor(
    private val tokenStore: TokenStore
) {
    private val mutex = Mutex()
    private var isRefreshing = false
    
    // 토큰 갱신을 위한 별도 네트워크 클라이언트 (순환 의존성 방지)
    private val refreshClient by lazy {
        val gson = GsonBuilder().create()
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                    addInterceptor(logging)
                }
            }
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.serverUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        
        retrofit.create(RefreshApi::class.java)
    }
    
    /**
     * 토큰을 갱신하고 새로운 토큰을 저장
     * @return 갱신 성공 여부
     */
    suspend fun refreshToken(): Boolean = mutex.withLock {
        if (isRefreshing) {
            return@withLock false // 이미 갱신 중이면 실패로 처리
        }
        
        try {
            isRefreshing = true
            
            val refreshToken = tokenStore.refresh()
            if (refreshToken.isNullOrBlank()) {
                return@withLock false
            }
            
            val req = RefreshTokenRequest(refreshToken)
            val res = refreshClient.refreshToken(req)
            
            if (res.isSuccessful) {
                val body = res.body()
                if (body?.success == true && body.data != null) {
                    tokenStore.save(body.data.accessToken, body.data.refreshToken)
                    return@withLock true
                } else {
                    tokenStore.clear()
                    return@withLock false
                }
            } else {
                tokenStore.clear()
                return@withLock false
            }
        } finally {
            isRefreshing = false
        }
    }
    
    /**
     * 현재 토큰이 유효한지 확인
     * @return 토큰 존재 여부
     */
    suspend fun hasValidToken(): Boolean {
        val accessToken = tokenStore.access()
        return !accessToken.isNullOrBlank()
    }
}

// 토큰 갱신 전용 API 인터페이스 (순환 의존성 방지)
interface RefreshApi {
    suspend fun refreshToken(body: RefreshTokenRequest): Response<ApiResponse<AppTokenResponse>>
}
