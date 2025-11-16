package com.alpha.archive.feature.home.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archive.core.network.toUserFriendlyMessage
import com.alpha.archive.feature.home.record.data.repository.ActivityRepository
import com.alpha.archive.feature.home.record.data.remote.dto.ActivityDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordUiState(
    val allActivities: List<ActivityDto> = emptyList(),  // 전체 데이터
    val activities: List<ActivityDto> = emptyList(),     // 필터링된 데이터
    val selectedFilters: Set<String> = emptySet(),

    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    init {
        loadActivities()
    }

    private fun applyFilters(
        all: List<ActivityDto>,
        selected: Set<String>
    ): List<ActivityDto> {
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

    private fun mergeActivities(currentActivities: List<ActivityDto>, newActivities: List<ActivityDto>): List<ActivityDto> {
        // 현재 리스트를 Map으로 변환 (id를 키로)
        val currentMap = currentActivities.associateBy { it.id }

        // 새 리스트에서 기존에 없던 아이템들만 필터링
        val newItems = newActivities.filter { newItem ->
            val currentItem = currentMap[newItem.id]
            // 아이템이 없거나 내용이 변경된 경우
            currentItem == null || hasItemChanged(currentItem, newItem)
        }

        // 기존 아이템들 중 새 리스트에 없는 것들은 유지하고, 새 아이템들을 추가
        val existingItems = currentActivities.filter { currentItem ->
            newActivities.any { newItem -> newItem.id == currentItem.id }
        }

        // 기존 아이템들을 새 데이터로 업데이트
        val updatedExistingItems = existingItems.map { currentItem ->
            newActivities.find { it.id == currentItem.id } ?: currentItem
        }

        // 새 아이템들과 업데이트된 기존 아이템들을 합치고 날짜순으로 정렬
        return (updatedExistingItems + newItems).sortedByDescending { it.activityDate }
    }

    private fun hasItemChanged(currentItem: ActivityDto, newItem: ActivityDto): Boolean {
        return currentItem.title != newItem.title ||
                currentItem.category != newItem.category ||
                (currentItem.location ?: "") != (newItem.location ?: "") ||
                currentItem.activityDate != newItem.activityDate ||
                currentItem.rating != newItem.rating
    }

    private fun loadActivitiesInternal(isLoading: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = if (isLoading) true else _uiState.value.isLoading,
                isRefreshing = if (!isLoading) true else _uiState.value.isRefreshing,
                errorMessage = null
            )

            activityRepository.getActivities()
                .onSuccess { newActivities ->
                    val filtered = applyFilters(newActivities, _uiState.value.selectedFilters)

                    _uiState.value = _uiState.value.copy(
                        allActivities = newActivities,
                        activities = filtered,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = exception.toUserFriendlyMessage()
                    )
                }
        }
    }

    fun loadActivities() {
        loadActivitiesInternal(true)
    }

    fun refreshActivities() {
        loadActivitiesInternal(false)
    }

    fun loadActivitiesWithMerge() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            activityRepository.getActivities()
                .onSuccess { newActivities ->
                    val merged = mergeActivities(_uiState.value.allActivities, newActivities)
                    val filtered = applyFilters(merged, _uiState.value.selectedFilters)

                    _uiState.value = _uiState.value.copy(
                        allActivities = merged,
                        activities = filtered,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.toUserFriendlyMessage()
                    )
                }
        }
    }

    fun refreshActivitiesWithMerge() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)

            activityRepository.getActivities()
                .onSuccess { newActivities ->
                    val merged = mergeActivities(_uiState.value.allActivities, newActivities)
                    val filtered = applyFilters(merged, _uiState.value.selectedFilters)

                    _uiState.value = _uiState.value.copy(
                        allActivities = merged,
                        activities = filtered,
                        isRefreshing = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        errorMessage = exception.toUserFriendlyMessage()
                    )
                }
        }
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
