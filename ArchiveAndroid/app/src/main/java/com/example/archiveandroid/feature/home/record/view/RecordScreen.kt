package com.example.archiveandroid.feature.home.record
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.feature.home.record.filter.RecordFilterSheet
import com.example.archiveandroid.feature.home.record.input.RecordInputActivity
import com.example.archiveandroid.feature.home.record.ui.RecordItem
import com.example.archiveandroid.feature.home.record.ui.RecordItemMapper.toRecordItem
import com.example.archiveandroid.feature.home.record.ui.RecordListItem
import com.example.archiveandroid.feature.home.recorddetail.view.RecordDetailActivity

private fun sampleRecords(): List<RecordItem> = listOf(
    RecordItem(
        id = "1",
        title = "KOSS 여름 LT",
        location = "강원도 양양군 정암해변",
        categoryLabel = "여행",
        categoryBg = Color(0xFFE8F0FF),
        categoryFg = Color(0xFF335C99)
    ),
    RecordItem(
        id = "2",
        title = "국립현대미술관 '미래의 기억'",
        location = "서울시 과천시",
        categoryLabel = "전시",
        categoryBg = Color(0xFFFFF4E5),
        categoryFg = Color(0xFF9A6B1A)
    ),
    RecordItem(
        id = "3",
        title = "주말 러닝 5km",
        location = "한강공원",
        categoryLabel = "운동",
        categoryBg = Color(0xFFE7FFF2),
        categoryFg = Color(0xFF138A52)
    ),
    RecordItem(
        id = "4",
        title = "뮤지컬 '레미제라블' 관람",
        location = "샤롯데씨어터",
        categoryLabel = "뮤지컬",
        categoryBg = Color(0xFFF4E9FF),
        categoryFg = Color(0xFF6B39A6)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    onFilterClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    viewModel: RecordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var lastScroll by remember { mutableIntStateOf(0) }
    var fabVisible by remember { mutableStateOf(true) }
    var showFilter by remember { mutableStateOf(false) }

    // 데이터 상태
    val activities by viewModel.activities.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val selectedFilters by viewModel.selectedFilters.collectAsStateWithLifecycle()
    

    // Activity Result Launcher
    val recordInputLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 기록 추가 완료 시 리스트 갱신
            viewModel.refreshActivities()
        }
    }

    val recordDetailLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 기록 삭제 완료 시 리스트 갱신
            viewModel.refreshActivities()
        }
    }

    fun startRecordInputActivity() {
        val intent = Intent(context, RecordInputActivity::class.java)
        recordInputLauncher.launch(intent)
    }

    fun startRecordDetailActivity(activityId: String) {
        val activityIds = ArrayList(activities.map { it.id })
        val intent = Intent(context, RecordDetailActivity::class.java).apply {
            putExtra("activityId", activityId)
            putStringArrayListExtra("activityIds", activityIds)
        }
        recordDetailLauncher.launch(intent)
    }

    LaunchedEffect(scrollState.value) {
        val delta = scrollState.value - lastScroll
        if (delta > 0) fabVisible = false
        if (delta < 0) fabVisible = true
        lastScroll = scrollState.value
    }


    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0),
                title = {
                    Text(
                        text = "나의 활동 기록",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
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
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = fabVisible,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { startRecordInputActivity() },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    containerColor = Color(0x33444444),
                    contentColor = Color.White,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "추가")
                }
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("로딩 중...", style = MaterialTheme.typography.bodyMedium)
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
                activities.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (selectedFilters.isNotEmpty()) "선택한 카테고리의 기록이 없습니다" else "기록이 없습니다", 
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
                        ) {
                            items(
                                count = activities.size,
                                key = { index -> activities[index].id }
                            ) { index ->
                                val activity = activities[index]
                                // ActivityDto를 RecordItem으로 변환
                                val recordItem = activity.toRecordItem()

                                RecordListItem(
                                    item = recordItem,
                                    onClick = { startRecordDetailActivity(activity.id) },
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                            item { Spacer(modifier = Modifier.height(72.dp)) }
                        }
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
        "전시" -> Pair(Color(0xFFFFF4E5), Color(0xFF9A6B1A))
        "운동" -> Pair(Color(0xFFE7FFF2), Color(0xFF138A52))
        "뮤지컬" -> Pair(Color(0xFFF4E9FF), Color(0xFF6B39A6))
        "독서" -> Pair(Color(0xFFFFEFE6), Color(0xFFB04A17))
        "스포츠" -> Pair(Color(0xFFEAF5FF), Color(0xFF2C5A8A))
        "음악" -> Pair(Color(0xFFEFF9FF), Color(0xFF1F6E8C))
        "봉사" -> Pair(Color(0xFFEFF7FF), Color(0xFF2D6AA3))
        "작업" -> Pair(Color(0xFFF0F8FF), Color(0xFF4169E1))
        "영화" -> Pair(Color(0xFFFFF0F5), Color(0xFFDC143C))
        else -> Pair(Color(0xFFF5F5F5), Color(0xFF666666))
    }
}



