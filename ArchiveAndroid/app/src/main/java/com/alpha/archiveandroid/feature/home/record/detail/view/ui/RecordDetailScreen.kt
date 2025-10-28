package com.alpha.archiveandroid.feature.home.recorddetail.view.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alpha.archiveandroid.core.ui.components.DetailScreenLayout
import com.alpha.archiveandroid.core.ui.components.DetailScreenState
import com.alpha.archiveandroid.core.util.CategoryColorGenerator
import com.alpha.archiveandroid.core.util.DateFormatter
import com.alpha.archiveandroid.feature.home.recorddetail.view.RecordDetailUiState

@Composable
fun RecordDetailScreen(
    uiState: RecordDetailUiState,
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onRefresh: () -> Unit = {},
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
        onRefresh = onRefresh,
        modifier = modifier
    )
}

/**
 * ActivityDetailDto를 DetailScreenData로 변환하는 확장 함수
 */
private fun com.alpha.archiveandroid.feature.home.record.data.remote.dto.ActivityDetailDto.toDetailScreenData(): com.alpha.archiveandroid.core.ui.components.DetailScreenData {
    val (bgColor, fgColor) = CategoryColorGenerator.getCategoryColors(this.categoryDisplayName)
    
    return com.alpha.archiveandroid.core.ui.components.DetailScreenData(
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