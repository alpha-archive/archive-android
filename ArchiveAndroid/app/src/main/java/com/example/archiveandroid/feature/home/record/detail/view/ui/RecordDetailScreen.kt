package com.example.archiveandroid.feature.home.recorddetail.view.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.archiveandroid.core.ui.components.DetailScreenLayout
import com.example.archiveandroid.core.ui.components.DetailScreenState
import com.example.archiveandroid.core.util.DateFormatter
import com.example.archiveandroid.feature.home.recorddetail.view.RecordDetailUiState

@Composable
fun RecordDetailScreen(
    uiState: RecordDetailUiState,
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    showNavigation: Boolean = false,
    hasPrevious: Boolean = false,
    hasNext: Boolean = false,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val detailState = DetailScreenState(
        isLoading = uiState.isLoading,
        isDeleting = uiState.isDeleting,
        error = uiState.error,
        data = uiState.recordData?.toDetailScreenData()
    )
    
    DetailScreenLayout(
        state = detailState,
        title = "나의 활동 기록",
        showBack = true,
        showMenu = true,
        menuItems = listOf("수정", "삭제"),
        onBack = onBack,
        onMenuClick = { menuItem ->
            when (menuItem) {
                "수정" -> onEdit()
                "삭제" -> onDelete()
            }
        },
        showNavigation = showNavigation,
        hasPrevious = hasPrevious,
        hasNext = hasNext,
        onPreviousClick = onPreviousClick,
        onNextClick = onNextClick,
        modifier = modifier
    )
}

/**
 * ActivityDetailDto를 DetailScreenData로 변환하는 확장 함수
 */
private fun com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDetailDto.toDetailScreenData(): com.example.archiveandroid.core.ui.components.DetailScreenData {
    val (bgColor, fgColor) = getCategoryColor(this.categoryDisplayName)
    
    return com.example.archiveandroid.core.ui.components.DetailScreenData(
        title = this.title,
        categoryDisplayName = this.categoryDisplayName,
        activityDate = DateFormatter.formatToDateString(this.activityDate),
        location = this.location,
        memo = this.memo ?: "",
        images = this.images.map { it.imageUrl },
        categoryBg = bgColor,
        categoryFg = fgColor
    )
}

/**
 * 카테고리에 따른 색상 반환
 */
private fun getCategoryColor(category: String): Pair<Color, Color> {
    return when (category) {
        "여행" -> Pair(Color(0xFFE8F0FF), Color(0xFF335C99))
        "독서" -> Pair(Color(0xFFFFEFE6), Color(0xFFB04A17))
        "스포츠 관람" -> Pair(Color(0xFFEAF5FF), Color(0xFF2C5A8A))
        "운동" -> Pair(Color(0xFFE7FFF2), Color(0xFF138A52))
        "전시" -> Pair(Color(0xFFFFF4E5), Color(0xFF9A6B1A))
        "봉사" -> Pair(Color(0xFFEFF7FF), Color(0xFF2D6AA3))
        "뮤지컬" -> Pair(Color(0xFFF4E9FF), Color(0xFF6B39A6))
        "악기 연주" -> Pair(Color(0xFFEFF9FF), Color(0xFF1F6E8C))
        else -> Pair(Color(0xFFE0E0E0), Color(0xFF666666))
    }
}