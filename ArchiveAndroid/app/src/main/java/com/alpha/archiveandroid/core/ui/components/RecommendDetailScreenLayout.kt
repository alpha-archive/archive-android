package com.alpha.archiveandroid.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
 * 추천 상세화면 데이터 모델
 */
data class RecommendDetailScreenData(
    val title: String,
    val categoryDisplayName: String,
    val activityDate: String,
    val location: String,
    val detailInfo: String,
    val images: List<String> = emptyList(),
    val categoryBg: Color = Color(0xFFA0A6FF), // 카테고리 배경색
    val categoryFg: Color = Color.White // 카테고리 텍스트색
)

/**
 * 추천 상세화면 상태
 */
data class RecommendDetailScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: RecommendDetailScreenData? = null
)

/**
 * 추천 상세화면 전용 레이아웃
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendDetailScreenLayout(
    state: RecommendDetailScreenState,
    title: String,
    showBack: Boolean = true,
    onBack: () -> Unit = {},
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showImageViewer by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.basicMarquee(),
                        maxLines = 1
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
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedImageIndex = pagerState.currentPage
                                            showImageViewer = true
                                        }
                                ) { page ->
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(state.data.images[page])
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.FillWidth
                                    )
                                }
                                
                                // 페이지 인디케이터
                                if (state.data.images.size > 1) {
                                    LazyRow(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(state.data.images.size) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .background(
                                                        if (index == pagerState.currentPage) Color.White else Color.White.copy(alpha = 0.5f),
                                                        CircleShape
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 정보 섹션
                    item {
                        RecommendDetailInfoGroup {
                            // 카테고리와 일자를 같은 줄에
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 50.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CategoryButton(
                                    text = state.data.categoryDisplayName,
                                    backgroundColor = state.data.categoryBg,
                                    textColor = state.data.categoryFg
                                )
                                
                                Text(
                                    text = state.data.activityDate,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }
                            RecommendDetailDivider()

                            // 장소 (label 없이 값만)
                            Text(
                                text = state.data.location,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 50.dp)
                            )
                            
                            // 상세 정보가 있을 때만 표시
                            if (state.data.detailInfo.isNotEmpty()) {
                                RecommendDetailDivider()
                                RecommendDetailInfoSection(
                                    label = "상세 정보",
                                    info = state.data.detailInfo
                                )
                            }
                        }
                    }

                    // 하단 여백
                    item {
                        Spacer(modifier = Modifier.padding(bottom = 72.dp))
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            HorizontalPager(
                state = rememberPagerState(initialPage = selectedImageIndex, pageCount = { state.data.images.size }),
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.data.images[page])
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            
            IconButton(
                onClick = { showImageViewer = false },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = Color.White
                )
            }
        }
    }
}

/**
 * 추천 상세화면 정보 행 컴포넌트
 */
@Composable
fun RecommendDetailRowInfo(
    label: String,
    value: String? = null,
    modifier: Modifier = Modifier,
    valueContent: (@Composable () -> Unit)? = null // 커스텀 값 컨텐츠 (예: CategoryButton)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )
        if (valueContent != null) {
            valueContent()
        } else {
            Text(
                text = value.orEmpty(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

/**
 * 추천 상세화면 정보 섹션 컴포넌트
 */
@Composable
fun RecommendDetailInfoSection(
    label: String,
    info: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )
        Text(
            text = info,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/**
 * 추천 상세화면 구분선 컴포넌트
 */
@Composable
fun RecommendDetailDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        color = Color(0xffD9D9D9),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
    )
}

/**
 * 추천 상세화면 정보 그룹 컴포넌트
 */
@Composable
fun RecommendDetailInfoGroup(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        content()
    }
}
