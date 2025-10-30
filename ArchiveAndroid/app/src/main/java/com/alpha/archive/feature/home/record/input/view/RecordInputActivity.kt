package com.alpha.archive.feature.home.record.input

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpha.archive.core.ui.theme.ArchiveAndroidTheme
import com.alpha.archive.feature.home.record.input.data.remote.dto.RecordInputRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordInputActivity : ComponentActivity() {

    private val viewModel: RecordInputViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val activityId = intent.getStringExtra("activityId")
        
        setContent {
            ArchiveAndroidTheme {
                val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

                // 수정 모드일 때 기존 데이터 로드
                LaunchedEffect(activityId) {
                    activityId?.let { id ->
                        viewModel.loadActivityForEdit(id)
                    }
                }

                LaunchedEffect(uiState.isSuccess) {
                    if (uiState.isSuccess) {
                        setResult(android.app.Activity.RESULT_OK)
                        finish()
                    }
                }

                RecordInputScreen(
                    ui = uiState,
                    onBack = { finish() },
                    onSave = { request: RecordInputRequest -> viewModel.onSaveClicked(request) },
                    onImageUpload = { file -> viewModel.uploadImage(file) },
                    onImageRemove = { imageId -> viewModel.removeImage(imageId) }
                )
            }
        }
    }
}
