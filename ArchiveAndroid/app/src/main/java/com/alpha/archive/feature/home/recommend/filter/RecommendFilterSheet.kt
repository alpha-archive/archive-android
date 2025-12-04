package com.alpha.archive.feature.home.recommend.filter

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
import com.alpha.archive.core.util.CategoryColorGenerator
import com.alpha.archive.core.util.CategoryMapper

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
        selectedCategory = CategoryMapper.toKorean(initialFilters.selectedCategory)
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
    val allCategories = CategoryMapper.allCategories
    
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
                                fontWeight = FontWeight.Bold
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
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // 카테고리 검색
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { 
                    Text(
                        text = "카테고리 검색",
                        color = Color(0xFF999999),
                        style = MaterialTheme.typography.bodyMedium
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
                            text = "년",
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.bodySmall
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
                            text = "월",
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.bodySmall
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
                            text = "일",
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.bodySmall
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
                            text = "년",
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.bodySmall
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
                            text = "월",
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.bodySmall
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
                            text = "일",
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.bodySmall
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
                            text = "시",
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.bodySmall
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
                            text = "구",
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.bodySmall
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
                Text(
                    text = "취소",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            // 적용 버튼
            Button(
                onClick = {
                    val filterData = RecommendFilterData(
                        selectedCategory = CategoryMapper.toEnglish(selectedCategory),
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
                Text(
                    text = "적용",
                    style = MaterialTheme.typography.bodyMedium
                )
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
    val (bgColor, fgColor) = CategoryColorGenerator.getCategoryColors(category)
    
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) bgColor else Color(0xFFF0F0F0),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category,
            color = if (isSelected) fgColor else Color(0xFF666666),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}


