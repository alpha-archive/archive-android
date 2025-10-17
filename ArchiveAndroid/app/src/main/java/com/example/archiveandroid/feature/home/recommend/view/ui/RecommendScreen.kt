package com.example.archiveandroid.feature.home.recommend.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.core.ui.components.ListItem
import com.example.archiveandroid.core.ui.components.ListItemCard
import com.example.archiveandroid.feature.home.recommend.view.RecommendViewModel
import com.example.archiveandroid.feature.home.record.filter.RecordFilterSheet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendScreen(
    onFilterClick: () -> Unit = {},
    onRecommendItemClick: (String) -> Unit = {},
    viewModel: RecommendViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var lastScroll by remember { mutableIntStateOf(0) }
    var showFilter by remember { mutableStateOf(false) }

    // 데이터 상태
    val recommendations by viewModel.recommendations.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val selectedFilters by viewModel.selectedFilters.collectAsStateWithLifecycle()

    LaunchedEffect(scrollState.value) {
        val delta = scrollState.value - lastScroll
        lastScroll = scrollState.value
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
                            fontSize = 24.sp
                        ),
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = { showFilter = true; onFilterClick() }) {
                        Box {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "필터",
                                modifier = Modifier.size(28.dp),
                                tint = if (selectedFilters.isNotEmpty()) Color(0xFF2196F3) else Color.Unspecified
                            )
                            // 필터가 적용되었을 때 작은 점 표시
                            if (selectedFilters.isNotEmpty()) {
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
                    Text("오류: $error", style = MaterialTheme.typography.bodyMedium)
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
                            text = if (selectedFilters.isNotEmpty()) "선택한 카테고리의 추천이 없습니다" else "추천 활동이 없습니다", 
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF898989)
                        )
                        if (selectedFilters.isNotEmpty()) {
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
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = innerPadding
                    ) {
                        items(
                            count = recommendations.size,
                            key = { index -> recommendations[index].id }
                        ) { index ->
                            val recommendation = recommendations[index]
                            val listItem = recommendation.toListItem()

                            ListItemCard(
                                item = listItem,
                                onClick = { onRecommendItemClick(recommendation.id) },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        item { Spacer(modifier = Modifier.height(72.dp)) }
                    }
                }
            }
        }
    }

    if (showFilter) {
        RecordFilterSheet(
            onDismiss = { showFilter = false },
            onFiltersApplied = { selectedIds ->
                viewModel.updateFilters(selectedIds)
                showFilter = false
            },
            selectedFilters = selectedFilters
        )
    }
}

/**
 * 카테고리에 따른 색상 반환
 */
private fun getCategoryColor(category: String): Pair<Color, Color> {
    return when (category) {
        "여행" -> Pair(Color(0xFFE8F0FF), Color(0xFF335C99))
        "독서" -> Pair(Color(0xFFFFEFE6), Color(0xFFB04A17))
        "스포츠 관람" -> Pair(Color(0xFFEAF5FF), Color(0xFF2C5A8A))
        "운동" -> Pair(Color(0xFFE7FFF2), Color(0xFF138A52))
        "전시" -> Pair(Color(0xFFFFF4E5), Color(0xFF9A6B1A))
        "봉사" -> Pair(Color(0xFFEFF7FF), Color(0xFF2D6AA3))
        "뮤지컬" -> Pair(Color(0xFFF4E9FF), Color(0xFF6B39A6))
        "악기 연주" -> Pair(Color(0xFFEFF9FF), Color(0xFF1F6E8C))
        else -> Pair(Color(0xFFF5F5F5), Color(0xFF666666))
    }
}

/**
 * 카테고리 enum을 한글로 변환
 */
private fun getCategoryDisplayName(category: String): String {
    return when (category) {
        "MUSICAL" -> "뮤지컬"
        "THEATER" -> "연극"
        "MOVIE" -> "영화"
        "EXHIBITION" -> "전시"
        "COOKING" -> "요리"
        "VOLUNTEER" -> "봉사"
        "READING" -> "독서"
        "CONCERT" -> "콘서트"
        "FESTIVAL" -> "축제"
        "WORKSHOP" -> "워크샵"
        "SPORTS" -> "스포츠"
        "TRAVEL" -> "여행"
        "OUTDOOR" -> "야외활동"
        "HOBBY" -> "취미"
        "STUDY" -> "스터디"
        "NETWORKING" -> "네트워킹"
        "OTHER" -> "기타"
        else -> category
    }
}

/**
 * 추천 활동을 ListItem으로 변환
 */
private fun com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendActivityDto.toListItem(): ListItem {
    val categoryDisplayName = getCategoryDisplayName(this.category)
    val (bgColor, fgColor) = getCategoryColor(categoryDisplayName)
    
    // 날짜 포맷팅
    val dateText = try {
        val startDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(this.startAt)
        val endDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(this.endAt)
        val startStr = SimpleDateFormat("yyyy/M/d", Locale.getDefault()).format(startDate ?: Date())
        val endStr = SimpleDateFormat("yyyy/M/d", Locale.getDefault()).format(endDate ?: Date())
        "$startStr ~ $endStr"
    } catch (e: Exception) {
        "$startAt ~ $endAt"
    }
    
    // 위치 정보 생성
    val locationText = "$placeName ($placeDistrict)"
    
    return ListItem(
        id = this.id,
        title = this.title,
        location = locationText,
        categoryLabel = categoryDisplayName,
        categoryBg = bgColor,
        categoryFg = fgColor,
        thumbnailImageUrl = null, // API에서 이미지 URL이 없으므로 null
        date = dateText,
        recommendationReason = null // API에서 추천 이유가 없으므로 null
    )
}
