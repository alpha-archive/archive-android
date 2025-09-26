package com.example.archiveandroid.feature.home.record.input

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import com.example.archiveandroid.core.ui.theme.ArchiveAndroidTheme
import com.example.archiveandroid.feature.home.record.input.RecordInputScreen
import com.example.archiveandroid.feature.home.record.input.RecordInputViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArchiveAndroidTheme {
                val viewModel: RecordInputViewModel = hiltViewModel()
                val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                
                LaunchedEffect(uiState.isSuccess) {
                    if (uiState.isSuccess) {
                        finish()
                    }
                }
                
                RecordInputScreen(
                    ui = uiState,
                    onBack = { finish() },
                    onSave = { request -> viewModel.onSaveClicked(request) },
                    isSubmitting = uiState.submitting,
                    error = uiState.errorMessage
                )
            }
        }
    }
}
