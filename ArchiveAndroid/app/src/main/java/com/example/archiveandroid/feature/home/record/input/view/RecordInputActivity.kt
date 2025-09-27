package com.example.archiveandroid.feature.home.record.input

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.core.ui.theme.ArchiveAndroidTheme
import com.example.archiveandroid.feature.home.record.input.RecordInputScreen
import com.example.archiveandroid.feature.home.record.input.RecordInputViewModel
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.RecordInputRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordInputActivity : ComponentActivity() {

    private val viewModel: RecordInputViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            ArchiveAndroidTheme {
                val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

                LaunchedEffect(uiState.isSuccess) {
                    if (uiState.isSuccess) {
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
