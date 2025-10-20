package com.example.archiveandroid.feature.home.recorddetail.view.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = modifier
    )
}

/**
 * ActivityDetailDto를 DetailScreenData로 변환하는 확장 함수
 */
private fun com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDetailDto.toDetailScreenData(): com.example.archiveandroid.core.ui.components.DetailScreenData {
    return com.example.archiveandroid.core.ui.components.DetailScreenData(
        title = this.title,
        categoryDisplayName = this.categoryDisplayName,
        activityDate = DateFormatter.formatToDateString(this.activityDate),
        location = this.location,
        memo = this.memo,
        images = this.images.map { it.imageUrl }
    )
}