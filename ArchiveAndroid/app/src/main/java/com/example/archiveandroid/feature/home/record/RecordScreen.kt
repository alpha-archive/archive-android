package com.example.archiveandroid.feature.home.record
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.archiveandroid.feature.home.record.filter.RecordFilterSheet

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
    onAddClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var lastScroll by remember { mutableIntStateOf(0) }
    var fabVisible by remember { mutableStateOf(true) }
    var showFilter by remember { mutableStateOf(false) }
    val records = remember { sampleRecords() }

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
                            fontSize = 24.sp
                        ),
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = { showFilter = true; onFilterClick() }) {
                        Icon(
                            painter = rememberAssetIconPainter("icons/filter.png"),
                            contentDescription = "필터",
                            modifier = Modifier.size(28.dp),
                            tint = Color.Unspecified
                        )
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
                    onClick = onAddClick,
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
            if (records.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "아직 기록이 없어요", style = MaterialTheme.typography.titleLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(records, key = { it.id }) { item ->
                        RecordListItem(
                            item = item,
                            onClick = { /* TODO: 상세보기 또는 편집 */ },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(72.dp)) }
                }
            }
        }
    }

    if (showFilter) {
        RecordFilterSheet(onDismiss = { showFilter = false })
    }
}

@Composable
private fun rememberAssetIconPainter(assetPath: String): Painter {
    val context = LocalContext.current
    val bitmap = remember(assetPath) {
        context.assets.open(assetPath).use { input ->
            BitmapFactory.decodeStream(input)
        }
    }
    return remember(bitmap) { BitmapPainter(bitmap.asImageBitmap()) }
}


