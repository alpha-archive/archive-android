package com.example.archiveandroid.feature.record.view.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import com.example.archiveandroid.R
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
import androidx.compose.ui.res.painterResource
import com.example.yourapp.ui.components.AppBarMenuItem
import com.example.yourapp.ui.components.TopAppBar
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.archiveandroid.core.ui.theme.Pretendard

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
                fontFamily = Pretendard,
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
            style = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 18.sp)
        )
        if (valueContent != null) {
            valueContent()
        } else {
            Text(
                text = value.orEmpty(),
                style = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal)
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
            style = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 18.sp)
        )
        Text(
            text = memo,
            style = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal),
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
                    style = TextStyle(fontFamily = Pretendard)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailScreen(
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
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

                    RowInfo(label = "카테고리") {
                        CategoryButton(text = "여행")
                    }
                    Divider(color = Color(0xffD9D9D9), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 10.dp))

                    RowInfo(label = "활동명", value = "KOSS 여름 LT")
                    Divider(color = Color(0xffD9D9D9), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 10.dp))

                    RowInfo(label = "날짜 / 시간", value = "2025/7/31 16:22")
                    Divider(color = Color(0xffD9D9D9), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 10.dp))

                    RowInfo(label = "위치", value = "강원도 양양군 정암해변")
                    Divider(color = Color(0xffD9D9D9), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 10.dp))

                    MemoSection(memo = "아 집가고 싶다")
                }
            }
        }

        NextButton { /* 다음 기록으로 넘어가기 */ }
    }
}