package com.alpha.archiveandroid.feature.home.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archiveandroid.core.network.toUserFriendlyMessage
import com.alpha.archiveandroid.feature.home.record.data.repository.ActivityRepository
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
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
                .onSuccess { activities ->
                    _allActivities.value = activities
                }
                .onFailure { exception ->
                    _error.value = exception.toUserFriendlyMessage()
                }
            
            loadingState.value = false
        }
    }
    
    fun loadActivities() {
        loadActivities(_isLoading)
    }
    
    fun refreshActivities() {
        loadActivities(_isRefreshing)
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
