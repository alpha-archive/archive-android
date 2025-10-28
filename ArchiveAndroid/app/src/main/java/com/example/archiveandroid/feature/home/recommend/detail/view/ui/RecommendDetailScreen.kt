package com.example.archiveandroid.feature.home.recommend.detail.view.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.core.ui.components.RecommendDetailScreenLayout
import com.example.archiveandroid.core.ui.components.RecommendDetailScreenState
import com.example.archiveandroid.feature.home.recommend.detail.view.RecommendDetailViewModel

@Composable
fun RecommendDetailScreen(
    onBack: () -> Unit = {},
    viewModel: RecommendDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // DetailScreenState를 RecommendDetailScreenState로 변환
    val recommendState = RecommendDetailScreenState(
        isLoading = uiState.isLoading,
        error = uiState.error,
        data = uiState.data?.let { data ->
            com.example.archiveandroid.core.ui.components.RecommendDetailScreenData(
                title = data.title,
                categoryDisplayName = data.categoryDisplayName,
                activityDate = data.activityDate,
                location = data.location,
                detailInfo = data.memo,
                images = data.images,
                categoryBg = data.categoryBg,
                categoryFg = data.categoryFg
            )
        }
    )
    
    RecommendDetailScreenLayout(
        state = recommendState,
        title = uiState.data?.title ?: "추천 활동 상세",
        showBack = true,
        onBack = onBack,
        onRefresh = { viewModel.refresh() },
        modifier = modifier
    )
}
