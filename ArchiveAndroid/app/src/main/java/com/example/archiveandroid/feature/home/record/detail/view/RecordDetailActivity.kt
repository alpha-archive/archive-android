package com.example.archiveandroid.feature.home.recorddetail.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

        val initialActivityId = intent.getStringExtra("activityId")
        val activityIdsList = intent.getStringArrayListExtra("activityIds") ?: arrayListOf()

        setContent {
            ArchiveAndroidTheme {
                val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                var currentActivityId by remember { mutableStateOf(initialActivityId) }
                
                val currentIndex = remember(currentActivityId) {
                    activityIdsList.indexOf(currentActivityId)
                }
                
                val hasPrevious = currentIndex > 0
                val hasNext = currentIndex >= 0 && currentIndex < activityIdsList.size - 1

                // activityId가 있으면 해당 기록 로드
                LaunchedEffect(currentActivityId) {
                    currentActivityId?.let { activityId ->
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
                        currentActivityId?.let { activityId ->
                            val intent = Intent(this@RecordDetailActivity, RecordInputActivity::class.java).apply {
                                putExtra("activityId", activityId)
                            }
                            startActivityForResult(intent, 1001)
                        }
                    },
                    onDelete = { 
                        currentActivityId?.let { activityId ->
                            viewModel.deleteActivity(activityId)
                        }
                    },
                    showNavigation = activityIdsList.isNotEmpty(),
                    hasPrevious = hasPrevious,
                    hasNext = hasNext,
                    onPreviousClick = {
                        if (hasPrevious) {
                            currentActivityId = activityIdsList[currentIndex - 1]
                        }
                    },
                    onNextClick = {
                        if (hasNext) {
                            currentActivityId = activityIdsList[currentIndex + 1]
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

