package com.example.archiveandroid.feature.home.recommend.detail.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.archiveandroid.core.ui.theme.ArchiveAndroidTheme
import com.example.archiveandroid.feature.home.recommend.detail.view.ui.RecommendDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendDetailActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            ArchiveAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecommendDetailScreen(
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}
