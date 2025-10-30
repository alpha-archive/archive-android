package com.alpha.archive.feature.home.user.view.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpha.archive.feature.home.user.view.UserViewModel
import com.alpha.archive.feature.home.user.view.ui.components.LogoutButton
import com.alpha.archive.feature.home.user.view.ui.components.VersionInfoCard
import com.alpha.archive.feature.intro.view.IntroActivity

@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    // 로그아웃 성공 시 IntroActivity로 이동
    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is UserViewModel.UiState.LoggedOut -> {
                val intent = Intent(context, IntroActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
                if (context is Activity) {
                    context.finish()
                }
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 40.dp)
    ) {
        VersionInfoCard(version = viewModel.appVersion)

        Spacer(modifier = Modifier.weight(1f))

        LogoutButton(
            onClick = { viewModel.logout() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}


