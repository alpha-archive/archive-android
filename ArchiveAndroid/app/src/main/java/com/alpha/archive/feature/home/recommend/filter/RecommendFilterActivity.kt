package com.alpha.archive.feature.home.recommend.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.IntentCompat
import com.alpha.archive.core.ui.theme.ArchiveAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendFilterActivity : ComponentActivity() {
    
    private val viewModel: RecommendFilterViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Intent에서 현재 필터 상태 가져오기
        val currentFilters = IntentCompat.getParcelableExtra(
            intent, 
            "current_filters", 
            RecommendFilterData::class.java
        ) ?: RecommendFilterData()
        
        setContent {
            // ViewModel 초기화
            LaunchedEffect(Unit) {
                viewModel.initWithFilters(currentFilters)
            }
            
            ArchiveAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecommendFilterScreen(
                        viewModel = viewModel,
                        onDismiss = { finish() },
                        onFiltersApplied = { filterData ->
                            // 결과를 Intent로 전달
                            val resultIntent = Intent().apply {
                                putExtra("filter_data", filterData)
                            }
                            setResult(RESULT_OK, resultIntent)
                            finish()
                        }
                    )
                }
            }
        }
    }
    
    companion object {
        const val REQUEST_CODE = 1001
        
        fun createIntent(context: Context, filters: RecommendFilterData? = null): Intent {
            return Intent(context, RecommendFilterActivity::class.java).apply {
                if (filters != null) {
                    putExtra("current_filters", filters)
                }
            }
        }
    }
}
