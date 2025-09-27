package com.example.archiveandroid.feature.home.recorddetail.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.core.ui.theme.ArchiveAndroidTheme
import com.example.archiveandroid.feature.home.recorddetail.view.ui.RecordDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordDetailActivity : ComponentActivity() {

    private val viewModel: RecordDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recordId = intent.getStringExtra("record_id")

        setContent {
            ArchiveAndroidTheme {
                val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

                // recordId가 있으면 해당 기록 로드
                if (recordId != null) {
                    viewModel.loadRecordDetail(recordId)
                }

                RecordDetailScreen(
                    onBack = { finish() }
                )
            }
        }
    }
}

