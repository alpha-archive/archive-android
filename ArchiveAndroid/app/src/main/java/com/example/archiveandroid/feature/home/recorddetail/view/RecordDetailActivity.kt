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
import com.example.archiveandroid.feature.home.recorddetail.view.ui.RecordInputScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordDetailActivity : ComponentActivity() {

    private val viewModel: RecordDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArchiveAndroidTheme {
                val ui = viewModel.uiState.collectAsStateWithLifecycle().value

                LaunchedEffect(ui.isSuccess) {
                    if (ui.isSuccess) {
                        setResult(Activity.RESULT_OK, Intent())
                        finish()
                    }
                }

                RecordInputScreen(
                    ui = ui,
                    onBack = { finish() },
                    onSave = { req -> viewModel.onSaveClicked(req) },
                    isSubmitting = ui.submitting,
                    error = ui.errorMessage
                )
            }
        }
    }
}

