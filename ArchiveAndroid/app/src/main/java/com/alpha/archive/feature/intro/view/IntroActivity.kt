package com.alpha.archive.feature.intro.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpha.archive.core.ui.components.UpdateDialog
import com.alpha.archive.core.ui.theme.ArchiveAndroidTheme
import com.alpha.archive.core.version.UpdateType
import com.alpha.archive.feature.home.HomeActivity
import com.alpha.archive.feature.intro.view.ui.IntroScreen
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

        setContent {
            ArchiveAndroidTheme {
                val ui = viewModel.uiState.collectAsStateWithLifecycle().value
                val versionCheckResult = viewModel.versionCheckResult.collectAsStateWithLifecycle().value
                var showUpdateDialog by remember { mutableStateOf(false) }
                
                // 버전 체크 완료되면 자동 로그인 시도
                LaunchedEffect(versionCheckResult) {
                    if (versionCheckResult != null) {
                        if (versionCheckResult.updateType != UpdateType.NONE) {
                            showUpdateDialog = true
                        } else {
                            viewModel.tryKakaoAutoLogin()
                        }
                    }
                }
                
                // 필수 업데이트가 있으면 로그인 플로우 진행 안함
                if (versionCheckResult?.updateType == UpdateType.REQUIRED) {
                    IntroScreen.SplashScreen()
                } else {
                    when (ui) {
                        IntroViewModel.UiState.Loading -> IntroScreen.SplashScreen()
                        IntroViewModel.UiState.NeedLogin -> IntroScreen.LoginScreen(
                            onClickKakao = { viewModel.kakaoLogin(this) }
                        )
                        IntroViewModel.UiState.LoggedIn ->
                            viewModel.onKakaoLoginSuccess(TokenManagerProvider.instance.manager.getToken()?.accessToken!!)
                        is IntroViewModel.UiState.Success -> NavigateToHomeAndFinish()
                        is IntroViewModel.UiState.Error -> IntroScreen.LoginScreen(
                            error = ui.msg, onClickKakao = { viewModel.kakaoLogin(this) }
                        )
                    }
                }
                
                // 업데이트 다이얼로그
                if (showUpdateDialog && versionCheckResult != null) {
                    UpdateDialog(
                        updateType = versionCheckResult.updateType,
                        playStoreUrl = versionCheckResult.playStoreUrl,
                        onDismiss = { showUpdateDialog = false }
                    )
                }
            }
        }
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