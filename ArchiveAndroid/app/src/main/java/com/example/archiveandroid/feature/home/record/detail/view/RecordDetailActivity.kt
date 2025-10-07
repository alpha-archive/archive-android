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
import com.example.archiveandroid.feature.home.record.input.RecordInputActivity
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

                // 삭제 성공 시 화면 닫기
                LaunchedEffect(uiState.isDeleted) {
                    if (uiState.isDeleted) {
                        setResult(android.app.Activity.RESULT_OK)
                        finish()
                    }
                }

                RecordDetailScreen(
                    uiState = uiState,
                    onBack = { finish() },
                    onEdit = { 
                        if (activityId != null) {
                            val intent = Intent(this@RecordDetailActivity, RecordInputActivity::class.java).apply {
                                putExtra("activityId", activityId)
                            }
                            startActivityForResult(intent, 1001)
                        }
                    },
                    onDelete = { 
                        if (activityId != null) {
                            viewModel.deleteActivity(activityId)
                        }
                    }
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            // 수정 완료 시 결과 반환
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}

