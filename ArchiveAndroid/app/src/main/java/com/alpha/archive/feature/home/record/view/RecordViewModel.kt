package com.alpha.archive.feature.home.record

import androidx.lifecycle.viewModelScope
import com.alpha.archive.core.ui.BaseViewModel
import com.alpha.archive.feature.home.record.data.repository.ActivityRepository
import com.alpha.archive.feature.home.record.data.remote.dto.ActivityListItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordUiState(
    val allActivities: List<ActivityListItemDto> = emptyList(),  // 전체 데이터
    val activities: List<ActivityListItemDto> = emptyList(),     // 필터링된 데이터
    val selectedFilters: Set<String> = emptySet(),

    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : BaseViewModel<RecordUiState>() {

    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    init {
        loadActivities()
    }

    private fun applyFilters(
        all: List<ActivityListItemDto>,
        selected: Set<String>
    ): List<ActivityListItemDto> {
        val filtered = if (selected.isEmpty()) {
            all
        } else {
            all.filter { activity ->
                // 한글 카테고리명으로 직접 비교
                selected.contains(activity.categoryDisplayName) ||
                        selected.contains(activity.category)
            }
        }
        // 날짜 기준 내림차순 정렬 (최신순)
        return filtered.sortedByDescending { it.activityDate }
    }

    fun loadActivities() {
        viewModelScope.launch {
            activityRepository.getActivities()
                .onSuccess { list ->
                    _uiState.value = _uiState.value.copy(
                        allActivities = list,
                        activities = applyFilters(list, _uiState.value.selectedFilters)
                    )
                }
                .onFailure { e ->
                    _uiState.updateError(e) { msg ->
                        copy(errorMessage = msg)
                    }
                }
        }
    }

    fun refreshActivities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRefreshing = true,
                errorMessage = null
            )

            activityRepository.getActivities()
                .onSuccess { newList ->
                    val filtered = applyFilters(newList, _uiState.value.selectedFilters)

                    _uiState.value = _uiState.value.copy(
                        allActivities = newList,
                        activities = filtered,
                        isRefreshing = false
                    )
                }
                .onFailure { e ->
                    _uiState.updateError(e) { msg ->
                        copy(
                            isRefreshing = false,
                            errorMessage = msg
                        )
                    }
                }
        }
    }

    // merge 기반 로드 대신 replace로 통일
    fun loadActivitiesWithMerge() {
        loadActivities()
    }

    // merge 기반 새로고침 대신 replace로 통일
    fun refreshActivitiesWithMerge() {
        refreshActivities()
    }

    fun updateFilters(selectedCategoryIds: Set<String>) {
        val filtered = applyFilters(_uiState.value.allActivities, selectedCategoryIds)

        _uiState.value = _uiState.value.copy(
            selectedFilters = selectedCategoryIds,
            activities = filtered
        )
    }

    fun clearFilters() {
        updateFilters(emptySet())
    }
}
