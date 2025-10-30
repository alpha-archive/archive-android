package com.alpha.archive.feature.intro.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archive.core.network.toUserFriendlyMessage
import com.alpha.archive.core.storage.TokenStore
import com.alpha.archive.core.version.VersionCheckResult
import com.alpha.archive.core.version.VersionManager
import com.alpha.archive.feature.intro.data.repository.AuthRepository
import com.alpha.archive.feature.intro.data.repository.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * IntroActivity
 *
 * 앱 실행 시 가장 먼저 호출되는 Intro 화면 Activity.
 * - Kakao SDK 로그인 플로우를 시작
 * - 로그인 결과를 ViewModel에 전달
 * - 서버 인증이 완료되면 HomeActivity(또는 MainScreen)으로 이동
 *
 * 역할:
 * 1. 카카오톡/카카오계정 로그인 버튼 UI 제공
 * 2. Kakao SDK로부터 OAuthToken을 받아 ViewModel에 전달
 * 3. ViewModel의 상태(LiveData/StateFlow)를 관찰해 화면 이동 처리
 */

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val introRepository: IntroRepository,
    private val authRepository: AuthRepository,
    private val tokenStore: TokenStore,
    private val versionManager: VersionManager
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data object NeedLogin : UiState
        data object LoggedIn : UiState
        data class Success(val temp: String) : UiState
        data class Error(val msg: String) : UiState
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.NeedLogin)
    val uiState: StateFlow<UiState> = _uiState
    
    private val _versionCheckResult = MutableStateFlow<VersionCheckResult?>(null)
    val versionCheckResult: StateFlow<VersionCheckResult?> = _versionCheckResult
    
    init {
        checkVersion()
    }
    
    private fun checkVersion() {
        viewModelScope.launch {
            try {
                val result = versionManager.checkVersion()
                if (result != null) {
                    _versionCheckResult.value = result
                } else {
                    // 네트워크 오류로 버전 체크 실패 시 자동 로그인 진행
                    tryKakaoAutoLogin()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 예외 발생 시에도 자동 로그인 진행
                tryKakaoAutoLogin()
            }
        }
    }

    fun tryKakaoAutoLogin() {
        viewModelScope.launch {
            try {
                val ok = introRepository.autoKakaoLogin()
                _uiState.value = if (ok) UiState.LoggedIn else UiState.NeedLogin
            } catch (e: Exception) {
                _uiState.value = UiState.NeedLogin
            }
        }
    }

    fun kakaoLogin(context: Context) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val r = introRepository.kakaoLogin(context)
                _uiState.value = if (r.isSuccess) UiState.LoggedIn
                else UiState.Error(r.exceptionOrNull()?.toUserFriendlyMessage() ?: "로그인에 실패했습니다")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: e.toString())
            }
        }
    }

    fun onKakaoLoginSuccess(kakaoAccessToken: String) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                authRepository.loginWithKakao(kakaoAccessToken)
                    .onSuccess { response ->
                        tokenStore.save(response.accessToken, response.refreshToken)
                        _uiState.value = UiState.Success(response.accessToken)
                    }
                    .onFailure { exception ->
                        _uiState.value = UiState.Error(exception.toUserFriendlyMessage())
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: e.toString())
            }
        }
    }
}