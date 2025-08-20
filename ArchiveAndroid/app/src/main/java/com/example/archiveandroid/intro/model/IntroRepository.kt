package com.example.archiveandroid.intro.model

import android.content.Context
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * IntroRepository
 *
 * 앱 시작 시 필요한 데이터 처리와 서버 연동을 담당하는 Repository.
 * - Kakao 로그인 토큰을 서버로 전달하여 앱 전용 인증 토큰(JWT 등)을 발급받음
 * - Intro 화면에서 필요한 원격/로컬 데이터 소스를 관리
 *
 * 역할:
 * 1. Kakao SDK를 통해 저장된 accessToken 꺼내오기
 * 2. Retrofit API(AuthApi)를 호출해 서버에 토큰 전달
 * 3. 서버 응답(AuthResponse)을 ViewModel에 반환
 */

class IntroRepository @Inject constructor() {
    suspend fun autoKakaoLogin(): Boolean = suspendCancellableCoroutine { cont ->
        if (!AuthApiClient.instance.hasToken()) {
            cont.resume(false, null); return@suspendCancellableCoroutine
        }
        UserApiClient.instance.accessTokenInfo { _, error ->
            cont.resume(error == null, null)
        }
    }

    suspend fun kakaoLogin(context: Context) : Result<Unit> = suspendCancellableCoroutine { cont ->
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                token != null -> cont.resume(Result.success(Unit))
                else -> cont.resume(Result.failure(error ?: Exception("Kakao login failed")))
            }
        }

        val isKakaoTalkAvailable = UserApiClient.instance.isKakaoTalkLoginAvailable(context)
        if (isKakaoTalkAvailable) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
}