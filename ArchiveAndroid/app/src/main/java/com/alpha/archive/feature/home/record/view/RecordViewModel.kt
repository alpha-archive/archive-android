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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    
    // 원본 데이터
    private val _allActivities = MutableStateFlow<List<ActivityDto>>(emptyList())
    
    // 선택된 필터 카테고리들
    private val _selectedFilters = MutableStateFlow<Set<String>>(emptySet())
    
    // 필터링된 데이터 (combine으로 자동 업데이트)
    val activities: StateFlow<List<ActivityDto>> = combine(
        _allActivities,
        _selectedFilters
    ) { allActivities, selectedFilters ->
        val filtered = if (selectedFilters.isEmpty()) {
            allActivities
        } else {
            allActivities.filter { activity ->
                // 한글 카테고리명으로 직접 비교
                selectedFilters.contains(activity.categoryDisplayName) || 
                selectedFilters.contains(activity.category)
            }
        }
        // 날짜 기준 내림차순 정렬 (최신순)
        filtered.sortedByDescending { it.activityDate }
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadActivities()
    }
    
    private fun loadActivities(loadingState: MutableStateFlow<Boolean>) {
        viewModelScope.launch {
            loadingState.value = true
            _error.value = null
            
            activityRepository.getActivities()
                .onSuccess { newActivities ->
                    _allActivities.value = newActivities
                }
                .onFailure { exception ->
                    _error.value = exception.toUserFriendlyMessage()
                }
            
            loadingState.value = false
        }
    }
    
    private fun loadActivitiesWithMerge(loadingState: MutableStateFlow<Boolean>) {
        viewModelScope.launch {
            loadingState.value = true
            _error.value = null
            
            activityRepository.getActivities()
                .onSuccess { newActivities ->
                    val currentActivities = _allActivities.value
                    val mergedActivities = mergeActivities(currentActivities, newActivities)
                    _allActivities.value = mergedActivities
                }
                .onFailure { exception ->
                    _error.value = exception.toUserFriendlyMessage()
                }
            
            loadingState.value = false
        }
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
    
    fun loadActivities() {
        loadActivities(_isLoading)
    }
    
    fun refreshActivities() {
        loadActivities(_isRefreshing)
    }
    
    fun loadActivitiesWithMerge() {
        loadActivitiesWithMerge(_isLoading)
    }
    
    fun refreshActivitiesWithMerge() {
        loadActivitiesWithMerge(_isRefreshing)
    }
    
    // 필터 관련 함수들
    fun updateFilters(selectedCategoryIds: Set<String>) {
        _selectedFilters.value = selectedCategoryIds
    }
    
    fun clearFilters() {
        _selectedFilters.value = emptySet()
    }
    
    val selectedFilters: StateFlow<Set<String>> = _selectedFilters.asStateFlow()
}
