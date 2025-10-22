package com.example.archiveandroid.feature.home.recommend.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 필터링을 위한 데이터 클래스
data class RecommendFilterData(
    val selectedCategory: String = "",
    val startYear: String = "",
    val startMonth: String = "",
    val startDay: String = "",
    val endYear: String = "",
    val endMonth: String = "",
    val endDay: String = "",
    val city: String = "",
    val district: String = ""
) : java.io.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendFilterScreen(
    onDismiss: () -> Unit,
    onFiltersApplied: (RecommendFilterData) -> Unit,
    initialFilters: RecommendFilterData = RecommendFilterData()
) {
    val sheetState = rememberModalBottomSheetState()
    var selectedCategory by remember { mutableStateOf("") }
    var startYear by remember { mutableStateOf("") }
    var startMonth by remember { mutableStateOf("") }
    var startDay by remember { mutableStateOf("") }
    var endYear by remember { mutableStateOf("") }
    var endMonth by remember { mutableStateOf("") }
    var endDay by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }
    
    // initialFilters가 변경될 때 상태 초기화
    LaunchedEffect(initialFilters) {
        selectedCategory = getCategoryKoreanValue(initialFilters.selectedCategory)
        startYear = initialFilters.startYear
        startMonth = initialFilters.startMonth
        startDay = initialFilters.startDay
        endYear = initialFilters.endYear
        endMonth = initialFilters.endMonth
        endDay = initialFilters.endDay
        city = initialFilters.city
        district = initialFilters.district
    }
    
    // 카테고리 목록
    val allCategories = listOf(
        "체험", "전시", "뮤지컬", "연극", "영화", "콘서트", 
        "축제", "워크샵", "스포츠", "여행", "독서", "요리",
        "봉사", "취미", "스터디", "네트워킹", "기타"
    )
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.White,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "필터",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    },
                    actions = {
                        Text(
                            text = "초기화",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .clickable {
                                    selectedCategory = ""
                                    startYear = ""
                                    startMonth = ""
                                    startDay = ""
                                    endYear = ""
                                    endMonth = ""
                                    endDay = ""
                                    city = ""
                                    district = ""
                                    searchText = ""
                                }
                                .padding(end = 8.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
        // 컨텐츠 영역
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 카테고리 섹션
            Text(
                text = "카테고리",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // 카테고리 검색
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { 
                    Text(
                        "카테고리 검색",
                        color = Color(0xFF999999),
                        fontSize = 14.sp
                    ) 
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "검색",
                        tint = Color(0xFF999999),
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF2196F3),
                    unfocusedIndicatorColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            
            // 카테고리 선택
            Text(
                text = "카테고리 선택",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // 카테고리 그리드 - 검색어에 맞는 카테고리들만 표시
            val filteredCategories = allCategories.filter { 
                it.contains(searchText, ignoreCase = true)
            }
            
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(80.dp),
                modifier = Modifier.height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                items(filteredCategories) { category ->
                    CategoryGridItem(
                        category = category,
                        isSelected = selectedCategory == category,
                        onClick = { 
                            selectedCategory = if (selectedCategory == category) "" else category
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 일자 섹션
            Text(
                text = "일자",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // 시작 날짜
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = startYear,
                    onValueChange = { startYear = it },
                    placeholder = { 
                        Text(
                            "년",
                            color = Color(0xFF999999),
                            fontSize = 14.sp
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2196F3),
                        unfocusedIndicatorColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = startMonth,
                    onValueChange = { startMonth = it },
                    placeholder = { 
                        Text(
                            "월",
                            color = Color(0xFF999999),
                            fontSize = 14.sp
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2196F3),
                        unfocusedIndicatorColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = startDay,
                    onValueChange = { startDay = it },
                    placeholder = { 
                        Text(
                            "일",
                            color = Color(0xFF999999),
                            fontSize = 14.sp
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2196F3),
                        unfocusedIndicatorColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                Text(
                    text = "부터",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 종료 날짜
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = endYear,
                    onValueChange = { endYear = it },
                    placeholder = { 
                        Text(
                            "년",
                            color = Color(0xFF999999),
                            fontSize = 14.sp
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2196F3),
                        unfocusedIndicatorColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = endMonth,
                    onValueChange = { endMonth = it },
                    placeholder = { 
                        Text(
                            "월",
                            color = Color(0xFF999999),
                            fontSize = 14.sp
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2196F3),
                        unfocusedIndicatorColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = endDay,
                    onValueChange = { endDay = it },
                    placeholder = { 
                        Text(
                            "일",
                            color = Color(0xFF999999),
                            fontSize = 14.sp
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2196F3),
                        unfocusedIndicatorColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                Text(
                    text = "까지",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 위치 섹션
            Text(
                text = "위치",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    placeholder = { 
                        Text(
                            "시",
                            color = Color(0xFF999999),
                            fontSize = 14.sp
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2196F3),
                        unfocusedIndicatorColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    placeholder = { 
                        Text(
                            "구",
                            color = Color(0xFF999999),
                            fontSize = 14.sp
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF2196F3),
                        unfocusedIndicatorColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        }
        
        // 하단 버튼 오버레이
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 취소 버튼
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("취소")
            }
            
            // 적용 버튼
            Button(
                onClick = {
                    val filterData = RecommendFilterData(
                        selectedCategory = getCategoryEnglishValue(selectedCategory),
                        startYear = startYear,
                        startMonth = startMonth,
                        startDay = startDay,
                        endYear = endYear,
                        endMonth = endMonth,
                        endDay = endDay,
                        city = city,
                        district = district
                    )
                    onFiltersApplied(filterData)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("적용")
            }
        }
    }
}


@Composable
private fun CategoryGridItem(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) getCategoryColor(category) else Color(0xFFF0F0F0),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category,
            color = if (isSelected) Color.White else Color(0xFF666666),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}

/**
 * 한글 카테고리를 영어 enum으로 변환
 */
private fun getCategoryEnglishValue(koreanCategory: String): String {
    return when (koreanCategory) {
        "뮤지컬" -> "MUSICAL"
        "연극" -> "THEATER"
        "영화" -> "MOVIE"
        "전시" -> "EXHIBITION"
        "요리" -> "COOKING"
        "봉사" -> "VOLUNTEER"
        "독서" -> "READING"
        "콘서트" -> "CONCERT"
        "축제" -> "FESTIVAL"
        "워크샵" -> "WORKSHOP"
        "스포츠" -> "SPORTS"
        "여행" -> "TRAVEL"
        "체험" -> "OUTDOOR"
        "취미" -> "HOBBY"
        "스터디" -> "STUDY"
        "네트워킹" -> "NETWORKING"
        "기타" -> "OTHER"
        else -> ""
    }
}

/**
 * 영어 enum을 한글 카테고리로 변환
 */
private fun getCategoryKoreanValue(englishCategory: String): String {
    return when (englishCategory) {
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
        "OUTDOOR" -> "체험"
        "HOBBY" -> "취미"
        "STUDY" -> "스터디"
        "NETWORKING" -> "네트워킹"
        "OTHER" -> "기타"
        else -> ""
    }
}

/**
 * 카테고리에 따른 색상 반환
 */
private fun getCategoryColor(category: String): Color {
    return when (category) {
        "음악" -> Color(0xFFFF5722)
        "예술" -> Color(0xFF9C27B0)
        "체험" -> Color(0xFF3F51B5)
        "전시" -> Color(0xFF2196F3)
        "뮤지컬" -> Color(0xFF00BCD4)
        "연극" -> Color(0xFF009688)
        "영화" -> Color(0xFF4CAF50)
        "콘서트" -> Color(0xFF8BC34A)
        "축제" -> Color(0xFFCDDC39)
        "워크샵" -> Color(0xFFFFEB3B)
        "스포츠" -> Color(0xFFFFC107)
        "여행" -> Color(0xFFFF9800)
        "독서" -> Color(0xFFFF5722)
        "요리" -> Color(0xFF795548)
        "봉사" -> Color(0xFF607D8B)
        "취미" -> Color(0xFFE91E63)
        "스터디" -> Color(0xFF673AB7)
        "네트워킹" -> Color(0xFF00BCD4)
        "기타" -> Color(0xFF9E9E9E)
        else -> Color(0xFF9E9E9E)
    }
}
