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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
) : java.io.Serializable {
    /**
     * 필터가 적용되어 있는지 확인
     */
    fun hasActiveFilters(): Boolean {
        return selectedCategory.isNotEmpty() ||
                startYear.isNotEmpty() ||
                startMonth.isNotEmpty() ||
                startDay.isNotEmpty() ||
                endYear.isNotEmpty() ||
                endMonth.isNotEmpty() ||
                endDay.isNotEmpty() ||
                city.isNotEmpty() ||
                district.isNotEmpty()
    }
}

// TextField 공통 색상
private object FilterTextFieldColors {
    val focusedIndicator = Color(0xFF2196F3)
    val unfocusedIndicator = Color(0xFFE0E0E0)
    val container = Color.White
    val cursor = Color(0xFF2196F3)
    val placeholder = Color(0xFF999999)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendFilterScreen(
    viewModel: RecommendFilterViewModel,
    onDismiss: () -> Unit,
    onFiltersApplied: (RecommendFilterData) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 카테고리 목록
    val allCategories = CategoryMapper.allCategories
    
    // 카테고리 그리드 - 검색어에 맞는 카테고리들만 표시
    val filteredCategories = allCategories.filter { 
        it.contains(uiState.searchText, ignoreCase = true)
    }
    
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
                                .clickable { viewModel.resetFilters() }
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
                FilterSectionTitle(text = "카테고리")
                
                // 카테고리 검색
                FilterTextField(
                    value = uiState.searchText,
                    onValueChange = { viewModel.updateSearchText(it) },
                    placeholder = "카테고리 검색",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색",
                            tint = FilterTextFieldColors.placeholder,
                            modifier = Modifier.size(20.dp)
                        )
                    }
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
                
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(80.dp),
                    modifier = Modifier.height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp
                ) {
                    items(filteredCategories) { category ->
                        CategoryGridItem(
                            category = category,
                            isSelected = uiState.selectedCategory == category,
                            onClick = { viewModel.toggleCategory(category) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 일자 섹션
                FilterSectionTitle(text = "일자")
                
                // 시작 날짜
                DateInputRow(
                    year = uiState.startYear,
                    month = uiState.startMonth,
                    day = uiState.startDay,
                    onYearChange = { viewModel.updateStartYear(it) },
                    onMonthChange = { viewModel.updateStartMonth(it) },
                    onDayChange = { viewModel.updateStartDay(it) },
                    suffix = "부터"
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 종료 날짜
                DateInputRow(
                    year = uiState.endYear,
                    month = uiState.endMonth,
                    day = uiState.endDay,
                    onYearChange = { viewModel.updateEndYear(it) },
                    onMonthChange = { viewModel.updateEndMonth(it) },
                    onDayChange = { viewModel.updateEndDay(it) },
                    suffix = "까지"
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 위치 섹션
                FilterSectionTitle(text = "위치")
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterTextField(
                        value = uiState.city,
                        onValueChange = { viewModel.updateCity(it) },
                        placeholder = "시",
                        modifier = Modifier.weight(1f)
                    )
                    FilterTextField(
                        value = uiState.district,
                        onValueChange = { viewModel.updateDistrict(it) },
                        placeholder = "구",
                        modifier = Modifier.weight(1f)
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
            FilterButton(
                text = "취소",
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            )
            
            // 적용 버튼
            FilterButton(
                text = "적용",
                onClick = { onFiltersApplied(uiState.toFilterData()) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FilterSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
        ),
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun FilterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { 
            Text(
                text = placeholder,
                color = FilterTextFieldColors.placeholder,
                style = MaterialTheme.typography.bodySmall
            ) 
        },
        leadingIcon = leadingIcon,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = FilterTextFieldColors.focusedIndicator,
            unfocusedIndicatorColor = FilterTextFieldColors.unfocusedIndicator,
            focusedContainerColor = FilterTextFieldColors.container,
            unfocusedContainerColor = FilterTextFieldColors.container,
            cursorColor = FilterTextFieldColors.cursor
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}

@Composable
private fun DateInputRow(
    year: String,
    month: String,
    day: String,
    onYearChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onDayChange: (String) -> Unit,
    suffix: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterTextField(
            value = year,
            onValueChange = onYearChange,
            placeholder = "년",
            modifier = Modifier.weight(1f)
        )
        FilterTextField(
            value = month,
            onValueChange = onMonthChange,
            placeholder = "월",
            modifier = Modifier.weight(1f)
        )
        FilterTextField(
            value = day,
            onValueChange = onDayChange,
            placeholder = "일",
            modifier = Modifier.weight(1f)
        )
        Text(
            text = suffix,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun FilterButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
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
