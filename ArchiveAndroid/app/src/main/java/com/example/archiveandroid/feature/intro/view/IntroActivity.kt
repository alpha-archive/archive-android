package com.example.archiveandroid.feature.intro.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.feature.home.HomeActivity
import com.example.archiveandroid.feature.intro.view.ui.theme.ArchiveAndroidTheme
import com.kakao.sdk.auth.TokenManagerProvider
import dagger.hilt.android.AndroidEntryPoint

/**
 * IntroViewModel
 *
 * Intro 화면의 UI 상태를 관리하고 비즈니스 로직을 처리하는 ViewModel.
 * - Repository를 통해 서버 인증 로직을 호출
 * - UI에서 로그인 성공/실패 상태를 관찰할 수 있도록 State 관리
 *
 * 역할:
 * 1. 카카오 로그인 성공 후 서버에 토큰 전송
 * 2. 서버로부터 발급받은 앱 토큰(JWT 등)을 DataStore 등에 저장 요청
 * 3. Intro → Home 화면으로 전환할 시점 제어
 */

@AndroidEntryPoint
class IntroActivity : ComponentActivity() {

    private val viewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.tryKakaoAutoLogin()

        setContent {
            ArchiveAndroidTheme {
                val ui = viewModel.uiState.collectAsStateWithLifecycle().value
                when (ui) {
                    IntroViewModel.UiState.Loading -> SplashScreen()
                    IntroViewModel.UiState.NeedLogin -> LoginScreen(
                        onClickKakao = { viewModel.kakaoLogin(this) }
                    )
                    IntroViewModel.UiState.LoggedIn ->
                        viewModel.onKakaoLoginSuccess(TokenManagerProvider.instance.manager.getToken()?.accessToken!!)
                    is IntroViewModel.UiState.Success -> NavigateToHomeAndFinish()
                    is IntroViewModel.UiState.Error -> LoginScreen(
                        error = ui.msg, onClickKakao = { viewModel.kakaoLogin(this) }
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    // 로딩 인디케이터 등
}

@Composable
fun LoginScreen(error: String? = null, onClickKakao: () -> Unit) {
    Column(/* ... */) {
        if (error != null) Text(text = error)
        Button(onClick = onClickKakao) { Text("카카오 로그인") }
    }
}

@Composable
fun NavigateToHomeAndFinish() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, HomeActivity::class.java))
        if (context is Activity) {
            context.finish()
        }
    }
}