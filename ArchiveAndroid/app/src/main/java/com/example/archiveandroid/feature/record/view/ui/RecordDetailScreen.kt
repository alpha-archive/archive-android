package com.example.archiveandroid.feature.intro.view.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import com.example.archiveandroid.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.example.yourapp.ui.components.AppTopBar
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailScreen(
    onBack: () -> Unit = {},
    onMore: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            AppTopBar(
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
                    contentDescription = "샘플 이미지",
                    modifier = Modifier
                        .width(330.dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.TopCenter
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "카테고리",
                            style = TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "여행",
                            style = TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                            )
                        )
                    }
                    Divider(
                        color = Color(0xffD9D9D9),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .padding(top = 10.dp, bottom = 20.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "활동명",
                            style = TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "KOSS 여름 LT",
                            style = TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                            )
                        )
                    }
                    Divider(
                        color = Color(0xffD9D9D9),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .padding(top = 10.dp, bottom = 20.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "날짜 / 시간",
                            style = TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "2025/7/31 16:22",
                            style = TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                            )
                        )
                    }
                    Divider(
                        color = Color(0xffD9D9D9),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .padding(top = 10.dp, bottom = 20.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "위치",
                            style = TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "강원도 양양군 정암해변",
                            style = TextStyle(
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                            )
                        )
                    }
                    Divider(
                        color = Color(0xffD9D9D9),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .padding(top = 10.dp, bottom = 20.dp)
                    )
                }



            }
        }
    }
}

val Pretendard = FontFamily(
    Font(R.font.pretendard)
)