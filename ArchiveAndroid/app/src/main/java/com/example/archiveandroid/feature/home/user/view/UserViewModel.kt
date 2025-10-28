package com.example.archiveandroid.feature.home.user.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.BuildConfig
import com.example.archiveandroid.core.storage.TokenStore
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class UserViewModel @Inject constructor(
    private val tokenStore: TokenStore
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    val appVersion: String = BuildConfig.VERSION_NAME

    fun logout() {
        viewModelScope.launch {
            try {
                // 1. 앱 토큰 삭제
                tokenStore.clear()
                
                // 2. 카카오 SDK 로그아웃
                logoutKakao()
                
                _uiState.value = UiState.LoggedOut
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "로그아웃 실패")
            }
        }
    }
    
    private suspend fun logoutKakao() = suspendCancellableCoroutine<Unit> { cont ->
        UserApiClient.instance.logout { error ->
            if (error != null) {
                // 로그아웃 실패해도 계속 진행 (토큰은 이미 삭제됨)
                error.printStackTrace()
            }
            if (cont.isActive) {
                cont.resume(Unit)
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object LoggedOut : UiState()
        data class Error(val message: String) : UiState()
    }
}

