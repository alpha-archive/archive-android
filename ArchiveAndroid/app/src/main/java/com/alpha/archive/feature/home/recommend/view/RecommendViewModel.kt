package com.alpha.archive.feature.home.recommend.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archive.core.network.toUserFriendlyMessage
import com.alpha.archive.feature.home.recommend.data.repository.RecommendRepository
import com.alpha.archive.feature.home.recommend.data.remote.dto.RecommendActivityDto
import com.alpha.archive.feature.home.recommend.filter.RecommendFilterData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
) : ViewModel() {
    
    // 원본 데이터
    private val _allRecommendations = MutableStateFlow<List<RecommendActivityDto>>(emptyList())
    
    // 현재 적용된 필터
    private val _currentFilters = MutableStateFlow<RecommendFilterData>(RecommendFilterData())
    
    // 페이지네이션 상태
    private val _currentCursor = MutableStateFlow<String?>(null)
    private val _hasNextPage = MutableStateFlow(true)
    
    // 추천 데이터 (필터는 API 레벨에서 처리)
    val recommendations: StateFlow<List<RecommendActivityDto>> = _allRecommendations.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    val hasNextPage: StateFlow<Boolean> = _hasNextPage.asStateFlow()
    
    init {
        loadRecommendations()
    }
    
    private fun loadRecommendations(
        loadingState: MutableStateFlow<Boolean>,
        cursor: String? = null,
        append: Boolean = false
    ) {
        viewModelScope.launch {
            loadingState.value = true
            _error.value = null
            
            val filters = _currentFilters.value
            recommendRepository.getRecommendActivities(
                cursor = cursor,
                size = 20, // 페이지 크기
                category = if (filters.selectedCategory.isNotEmpty()) filters.selectedCategory else null,
                startDate = if (filters.startYear.isNotEmpty() && filters.startMonth.isNotEmpty() && filters.startDay.isNotEmpty()) {
                    "${filters.startYear}-${filters.startMonth.padStart(2, '0')}-${filters.startDay.padStart(2, '0')}"
                } else null,
                endDate = if (filters.endYear.isNotEmpty() && filters.endMonth.isNotEmpty() && filters.endDay.isNotEmpty()) {
                    "${filters.endYear}-${filters.endMonth.padStart(2, '0')}-${filters.endDay.padStart(2, '0')}"
                } else null,
                city = if (filters.city.isNotEmpty()) filters.city else null,
                district = if (filters.district.isNotEmpty()) filters.district else null
            )
                .onSuccess { recommendations ->
                    if (append) {
                        _allRecommendations.value = _allRecommendations.value + recommendations
                    } else {
                        _allRecommendations.value = recommendations
                    }
                    
                    // 마지막 항목의 ID를 다음 커서로 설정
                    if (recommendations.isNotEmpty()) {
                        _currentCursor.value = recommendations.last().id
                        _hasNextPage.value = recommendations.size >= 20 // 페이지 크기와 같으면 더 있을 가능성
                    } else {
                        _hasNextPage.value = false
                    }
                }
                .onFailure { exception ->
                    _error.value = exception.toUserFriendlyMessage()
                }
            
            loadingState.value = false
        }
    }
    
    fun loadRecommendations() {
        _currentCursor.value = null
        _hasNextPage.value = true
        loadRecommendations(_isLoading, cursor = null, append = false)
    }
    
    fun applyFilters(filters: RecommendFilterData) {
        _currentFilters.value = filters
        _currentCursor.value = null
        _hasNextPage.value = true
        loadRecommendations(_isLoading, cursor = null, append = false)
    }
    
    fun getCurrentFilters(): RecommendFilterData {
        return _currentFilters.value
    }
    
    fun loadMoreRecommendations() {
        if (!_hasNextPage.value || _isLoadingMore.value) return
        
        loadRecommendations(_isLoadingMore, cursor = _currentCursor.value, append = true)
    }
    
    fun refreshRecommendations() {
        _currentCursor.value = null
        _hasNextPage.value = true
        loadRecommendations(_isRefreshing, cursor = null, append = false)
    }
    
    fun loadRecommendationsWithFilter(
        location: String? = null,
        title: String? = null,
        category: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _currentCursor.value = null
            _hasNextPage.value = true
            
            recommendRepository.getRecommendActivities(
                cursor = null,
                size = 20,
                location = location,
                title = title,
                category = category
            )
                .onSuccess { recommendations ->
                    _allRecommendations.value = recommendations
                    
                    if (recommendations.isNotEmpty()) {
                        _currentCursor.value = recommendations.last().id
                        _hasNextPage.value = recommendations.size >= 20
                    } else {
                        _hasNextPage.value = false
                    }
                }
                .onFailure { exception ->
                    _error.value = exception.toUserFriendlyMessage()
                }
            
            _isLoading.value = false
        }
    }
    
}
