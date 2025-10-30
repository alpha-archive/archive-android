package com.alpha.archive.feature.home.recommend.detail.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.alpha.archive.core.ui.theme.ArchiveAndroidTheme
import com.alpha.archive.feature.home.recommend.detail.view.ui.RecommendDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendDetailActivity : ComponentActivity() {
    
    private val viewModel: RecommendDetailViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val activityId = intent.getStringExtra("activityId") ?: ""
        
        setContent {
            ArchiveAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // activityId가 있으면 해당 추천 활동 로드
                    LaunchedEffect(activityId) {
                        if (activityId.isNotEmpty()) {
                            viewModel.loadActivityDetail(activityId)
                        }
                    }
                    
                    RecommendDetailScreen(
                        onBack = { finish() },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
