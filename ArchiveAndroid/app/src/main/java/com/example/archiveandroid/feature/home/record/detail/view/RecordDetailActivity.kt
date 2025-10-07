package com.example.archiveandroid.feature.home.recorddetail.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.core.ui.theme.ArchiveAndroidTheme
import com.example.archiveandroid.feature.home.recorddetail.view.ui.RecordDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordDetailActivity : ComponentActivity() {

    private val viewModel: RecordDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityId = intent.getStringExtra("activityId")

        setContent {
            ArchiveAndroidTheme {
                val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

                // activityId가 있으면 해당 기록 로드 (한 번만 실행)
                LaunchedEffect(activityId) {
                    if (activityId != null) {
                        viewModel.loadRecordDetail(activityId)
                    }
                }

                RecordDetailScreen(
                    uiState = uiState,
                    onBack = { finish() }
                )
            }
        }
    }
}

