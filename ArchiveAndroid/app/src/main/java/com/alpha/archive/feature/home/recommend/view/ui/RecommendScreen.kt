package com.alpha.archive.feature.home.recommend.view.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpha.archive.core.ui.components.ListItemCard
import com.alpha.archive.feature.home.recommend.data.mapper.RecommendMapper.toListItem
import com.alpha.archive.feature.home.recommend.detail.view.RecommendDetailActivity
import com.alpha.archive.feature.home.recommend.filter.RecommendFilterActivity
import com.alpha.archive.feature.home.recommend.filter.RecommendFilterData
import com.alpha.archive.feature.home.recommend.view.RecommendViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendScreen(
    onFilterClick: () -> Unit = {},
    onRecommendItemClick: (String) -> Unit = {},
    viewModel: RecommendViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    
    // ViewModel에서 필터 상태 collect (단일 소스)
    val currentFilters by viewModel.currentFilters.collectAsStateWithLifecycle()
    
    // 필터 Activity 런처
    val filterLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                IntentCompat.getParcelableExtra(
                    intent,
                    "filter_data",
                    RecommendFilterData::class.java
                )?.let { filterData ->
                    viewModel.applyFilters(filterData)
                }
            }
        }
    }
    
    // 추천 상세 Activity 런처
    val recommendDetailLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 추천 상세에서 돌아왔을 때 처리 (필요시)
    }

    // 데이터 상태
    val recommendations by viewModel.recommendations.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val hasNextPage by viewModel.hasNextPage.collectAsStateWithLifecycle()

    // 무한스크롤을 위한 상태 체크
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            val lastVisibleItem = visibleItemsInfo.lastOrNull()
            
            if (lastVisibleItem == null) return@derivedStateOf false
            
            // 마지막 아이템이 보이고, 더 로드할 데이터가 있고, 현재 로딩 중이 아닐 때
            lastVisibleItem.index >= recommendations.size - 3 && 
            hasNextPage && 
            !isLoadingMore
        }
    }

    // 무한스크롤 트리거
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMoreRecommendations()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0),
                title = {
                    Text(
                        text = "나를 위한 추천 활동",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = { 
                        val intent = RecommendFilterActivity.createIntent(context, currentFilters)
                        filterLauncher.launch(intent)
                        onFilterClick() 
                    }) {
                        Box {
                            val hasActiveFilters = currentFilters.hasActiveFilters()
                            
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "필터",
                                modifier = Modifier.size(28.dp),
                                tint = if (hasActiveFilters) Color(0xFF2196F3) else Color.Unspecified
                            )
                            // 필터가 적용되었을 때 작은 점 표시
                            if (hasActiveFilters) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            Color(0xFF2196F3),
                                            CircleShape
                                        )
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadRecommendations() }) {
                            Text("다시 시도")
                        }
                    }
                }
            }
            recommendations.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (currentFilters.selectedCategory.isNotEmpty()) "선택한 카테고리의 추천이 없습니다" else "추천 활동이 없습니다", 
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF898989)
                        )
                        if (currentFilters.selectedCategory.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "다른 카테고리를 선택해보세요",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF898989)
                            )
                        }
                    }
                }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = innerPadding
                    ) {
                        itemsIndexed(
                            items = recommendations,
                            key = { _, recommendation -> recommendation.id }
                        ) { index, recommendation ->
                            val listItem = recommendation.toListItem()

                            ListItemCard(
                                item = listItem,
                                onClick = { 
                                    val intent = Intent(
                                        context,
                                        RecommendDetailActivity::class.java
                                    ).apply {
                                        putExtra("activityId", recommendation.id)
                                    }
                                    recommendDetailLauncher.launch(intent)
                                },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            
                            // 더 로드 중일 때 로딩 인디케이터 표시
                            if (index == recommendations.size - 1 && isLoadingMore && hasNextPage) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                        
                        // 하단 여백
                        item { Spacer(modifier = Modifier.height(72.dp)) }
                    }
                }
            }
        }
    }

}
