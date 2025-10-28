package com.example.archiveandroid.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * 상세화면 데이터 모델
 */
data class DetailScreenData(
    val title: String,
    val categoryDisplayName: String,
    val activityDate: String,
    val location: String,
    val memo: String,
    val images: List<String> = emptyList(),
    val recommendationReason: String? = null, // 추천 이유 (추천 상세화면에서만 사용)
    val categoryBg: Color = Color(0xFFA0A6FF), // 카테고리 배경색
    val categoryFg: Color = Color.White // 카테고리 텍스트색
)

/**
 * 상세화면 상태
 */
data class DetailScreenState(
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val error: String? = null,
    val data: DetailScreenData? = null
)

/**
 * 공통 상세화면 레이아웃
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenLayout(
    state: DetailScreenState,
    title: String,
    showBack: Boolean = true,
    showMenu: Boolean = false,
    menuItems: List<String> = emptyList(),
    onBack: () -> Unit = {},
    onMenuClick: (String) -> Unit = {},
    showNavigation: Boolean = false,
    hasPrevious: Boolean = false,
    hasNext: Boolean = false,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showDropdownMenu by remember { mutableStateOf(false) }
    var showImageViewer by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "뒤로가기"
                            )
                        }
                    }
                },
                actions = {
                    if (showMenu && menuItems.isNotEmpty()) {
                        Box {
                            IconButton(onClick = { showDropdownMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "메뉴"
                                )
                            }
                            DropdownMenu(
                                expanded = showDropdownMenu,
                                onDismissRequest = { showDropdownMenu = false }
                            ) {
                                menuItems.forEach { item ->
                                    DropdownMenuItem(
                                        text = { 
                                            Text(
                                                text = item,
                                                style = MaterialTheme.typography.bodyMedium
                                            ) 
                                        },
                                        onClick = {
                                            showDropdownMenu = false
                                            onMenuClick(item)
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.isDeleting -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "삭제 중...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = state.error ?: "",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(onClick = onRefresh) {
                            Text("다시 시도")
                        }
                    }
                }
            }
            state.data != null -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                    // 이미지 섹션
                    if (state.data.images.isNotEmpty()) {
                        item {
                            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                            val imageWidth = screenWidth - 32.dp // 좌우 패딩 16dp씩
                            val pagerState = rememberPagerState(pageCount = { state.data.images.size })
                            
                            Box {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) { page ->
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(state.data.images[page])
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(imageWidth)
                                            .aspectRatio(4f / 3f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable {
                                                selectedImageIndex = page
                                                showImageViewer = true
                                            },
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                
                                // 인디케이터
                                if (state.data.images.size > 1) {
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        repeat(state.data.images.size) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .background(
                                                        color = if (index == pagerState.currentPage) 
                                                            Color.White else Color.White.copy(alpha = 0.5f),
                                                        shape = CircleShape
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier
                                    .width(330.dp)
                                    .aspectRatio(4f / 3f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF5F5F5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Gray
                                )
                            }
                        }
                    }

                    // 정보 섹션
                    item {
                        DetailInfoGroup {
                            DetailRowInfo(label = "카테고리") {
                                CategoryButton(
                                    text = state.data.categoryDisplayName,
                                    backgroundColor = state.data.categoryBg,
                                    textColor = state.data.categoryFg
                                )
                            }
                            DetailDivider()

                            DetailRowInfo(label = "활동명", value = state.data.title)
                            DetailDivider()

                            DetailRowInfo(label = "날짜", value = state.data.activityDate)
                            DetailDivider()

                            DetailRowInfo(label = "위치", value = state.data.location)
                            DetailDivider()

                            // 추천 이유가 있는 경우 (추천 상세화면)
                            if (state.data.recommendationReason != null) {
                                DetailMemoSection(
                                    label = "추천 이유",
                                    memo = state.data.recommendationReason
                                )
                                DetailDivider()
                            }

                            DetailMemoSection(memo = state.data.memo)
                        }
                    }

                    // 하단 여백
                    item {
                        Spacer(modifier = Modifier.padding(bottom = if (showNavigation) 100.dp else 72.dp))
                    }
                }
                
                // 하단 네비게이션 버튼
                if (showNavigation && (hasPrevious || hasNext)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onPreviousClick,
                            enabled = hasPrevious,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (hasPrevious) Color(0xFF444444) else Color(0xFFE0E0E0),
                                contentColor = if (hasPrevious) Color.White else Color(0xFF9E9E9E)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("이전")
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Button(
                            onClick = onNextClick,
                            enabled = hasNext,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (hasNext) Color(0xFF444444) else Color(0xFFE0E0E0),
                                contentColor = if (hasNext) Color.White else Color(0xFF9E9E9E)
                            )
                        ) {
                            Text("다음")
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "데이터가 없습니다",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
    
    // 전체화면 이미지 뷰어
    if (showImageViewer && state.data?.images?.isNotEmpty() == true) {
        FullScreenImageViewer(
            images = state.data.images,
            initialIndex = selectedImageIndex,
            onDismiss = { showImageViewer = false }
        )
    }
}

/**
 * 전체화면 이미지 뷰어
 */
@Composable
private fun FullScreenImageViewer(
    images: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { images.size }
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
    ) {
        // 이미지 페이저
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(images[page])
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onDismiss() },
                    contentScale = ContentScale.Fit
                )
            }
        }
        
        // 인디케이터
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(images.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (index == pagerState.currentPage) 
                                    Color.White else Color.White.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
