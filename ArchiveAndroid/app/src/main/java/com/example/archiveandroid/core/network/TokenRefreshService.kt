package com.example.archiveandroid.core.network

import com.example.archiveandroid.core.storage.TokenStore
import com.example.archiveandroid.feature.intro.data.repository.AuthRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    private val authRepository: AuthRepository,
    private val tokenStore: TokenStore
) {
    private val mutex = Mutex()
    private var isRefreshing = false
    
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
            
            val result = authRepository.refreshToken(refreshToken)
            result.onSuccess { response ->
                tokenStore.save(response.accessToken, response.refreshToken)
                return@withLock true
            }.onFailure {
                // 갱신 실패 시 토큰 삭제 (로그아웃)
                tokenStore.clear()
                return@withLock false
            }
            
            return@withLock false
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
