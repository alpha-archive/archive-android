package com.example.archiveandroid.feature.home.recommend.detail.view.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.core.ui.components.DetailScreenLayout
import com.example.archiveandroid.feature.home.recommend.detail.view.RecommendDetailViewModel

@Composable
fun RecommendDetailScreen(
    onBack: () -> Unit = {},
    viewModel: RecommendDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    DetailScreenLayout(
        state = uiState,
        title = "추천 활동 상세",
        showBack = true,
        showMenu = false,
        onBack = onBack,
        modifier = modifier
    )
}
