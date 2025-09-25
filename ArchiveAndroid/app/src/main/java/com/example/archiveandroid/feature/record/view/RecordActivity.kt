package com.example.archiveandroid.feature.record.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.core.ui.theme.ArchiveAndroidTheme
import com.example.archiveandroid.feature.record.view.ui.RecordInputScreen
import com.example.archiveandroid.feature.record.data.remote.dto.ActivityCreateRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordActivity : ComponentActivity() {

    private val viewModel: RecordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArchiveAndroidTheme {
                val ui = viewModel.uiState.collectAsStateWithLifecycle().value

                when (ui) {
                    is RecordViewModel.UiState.Editing -> {
                        RecordInputScreen(
                            ui = ui,
                            onBack = { finish() },
                            onSave = { req -> viewModel.onSaveClicked(req) },
                            isSubmitting = ui.submitting,
                            error = ui.errorMessage
                        )
                    }

                    is RecordViewModel.UiState.Success -> {
                        FinishAndReturn()
                    }
                }
            }
        }
    }
}

@Composable
private fun FinishAndReturn() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        (context as? Activity)?.apply {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }
}
