package com.example.archiveandroid.feature.home.recorddetail.view.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.archiveandroid.R
import com.example.yourapp.ui.components.AppBarMenuItem
import com.example.yourapp.ui.components.TopAppBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailScreen(
    uiState: com.example.archiveandroid.feature.home.recorddetail.view.RecordDetailUiState,
    onBack: () -> Unit = {},
    onMore: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "나의 활동 기록",
                showBack = true,
                onBackClick = onBack,
                menuItems = listOf(
                    AppBarMenuItem("more", "수정", onMore),
                    AppBarMenuItem("more", "삭제", onMore),
                ),
                scrollBehavior = scrollBehavior,
                actions = {}
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("로딩 중...")
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("오류: ${uiState.error}")
                }
            }
            uiState.recordData != null -> {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 이미지 섹션
                    if (uiState.recordData.images.isNotEmpty()) {
                        item {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uiState.recordData.images.first().imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(330.dp)
                                    .aspectRatio(4f / 3f)
                                    .padding(bottom = 30.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        // 이미지가 없을 때 기본 이미지 표시
                        item {
                            Image(
                                painter = painterResource(id = R.drawable.detail_dummy_image),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(330.dp)
                                    .aspectRatio(4f / 3f)
                                    .padding(bottom = 30.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // 정보 섹션
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            RowInfo(label = "카테고리") {
                                CategoryButton(text = uiState.recordData.categoryDisplayName)
                            }
                            Divider(color = Color(0xffD9D9D9), modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp, vertical = 10.dp))

                            RowInfo(label = "활동명", value = uiState.recordData.title)
                            Divider(color = Color(0xffD9D9D9), modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp, vertical = 10.dp))

                            RowInfo(label = "날짜", value = formatActivityDate(uiState.recordData.activityDate))
                            Divider(color = Color(0xffD9D9D9), modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp, vertical = 10.dp))

                            RowInfo(label = "위치", value = uiState.recordData.location)
                            Divider(color = Color(0xffD9D9D9), modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp, vertical = 10.dp))

                            MemoSection(memo = uiState.recordData.memo)
                        }
                    }
                }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("데이터가 없습니다")
                }
            }
        }
    }
}


/* 카테고리 버튼 */
@Composable
private fun CategoryButton(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFA0A6FF))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        )
    }
}

@Composable
private fun RowInfo(
    label: String,
    value: String? = null,
    modifier: Modifier = Modifier,
    valueContent: (@Composable () -> Unit)? = null // 컴포저블 슬롯(CategoryButton)
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
            style = TextStyle( fontWeight = FontWeight.Normal, fontSize = 18.sp)
        )
        if (valueContent != null) {
            valueContent()
        } else {
            Text(
                text = value.orEmpty(),
                style = TextStyle( fontWeight = FontWeight.Normal)
            )
        }
    }
}

@Composable
private fun MemoSection(
    label: String = "메모",
    memo: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Text(
            text = label,
            style = TextStyle( fontWeight = FontWeight.Normal, fontSize = 18.sp)
        )
        Text(
            text = memo,
            style = TextStyle( fontWeight = FontWeight.Normal),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun NextButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF646464),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation( // 그림자 제거
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next_arrow),
                    contentDescription = "다음",
                    tint = Color.White,
                    modifier = Modifier
                        .size(35.dp)
                )
                Text(
                    text = "다음",
                    color = Color.White,
                    fontSize = 9.sp,
                    style = TextStyle()
                )
            }
        }
    }
}

// 날짜 포맷팅 함수
private fun formatActivityDate(activityDate: String): String {
    return try {
        // T 이전의 날짜 부분만 추출
        val datePart = activityDate.substringBefore("T")
        // yyyy-MM-dd 형식을 yyyy/MM/dd로 변환
        datePart.replace("-", "/")
    } catch (e: Exception) {
        activityDate // 파싱 실패 시 원본 반환
    }
}