package com.example.archiveandroid.feature.home.recommend.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.recommend.data.repository.RecommendRepository
import com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendActivityDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
) : ViewModel() {
    
    // 원본 데이터
    private val _allRecommendations = MutableStateFlow<List<RecommendActivityDto>>(emptyList())
    
    // 선택된 필터 카테고리들
    private val _selectedFilters = MutableStateFlow<Set<String>>(emptySet())
    
    // 필터링된 데이터 (combine으로 자동 업데이트)
    val recommendations: StateFlow<List<RecommendActivityDto>> = combine(
        _allRecommendations,
        _selectedFilters
    ) { allRecommendations, selectedFilters ->
        if (selectedFilters.isEmpty()) {
            allRecommendations
        } else {
            allRecommendations.filter { recommendation ->
                // 카테고리 enum 값으로 비교 (MUSICAL, THEATER 등)
                selectedFilters.contains(recommendation.category)
            }
        }
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
        loadRecommendations()
    }
    
    private fun loadRecommendations(loadingState: MutableStateFlow<Boolean>) {
        viewModelScope.launch {
            loadingState.value = true
            _error.value = null
            
            recommendRepository.getRecommendActivities()
                .onSuccess { recommendations ->
                    _allRecommendations.value = recommendations
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "추천 데이터를 불러올 수 없습니다"
                }
            
            loadingState.value = false
        }
    }
    
    fun loadRecommendations() {
        loadRecommendations(_isLoading)
    }
    
    fun refreshRecommendations() {
        loadRecommendations(_isRefreshing)
    }
    
    fun loadRecommendationsWithFilter(
        location: String? = null,
        title: String? = null,
        category: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            recommendRepository.getRecommendActivities(
                location = location,
                title = title,
                category = category
            )
                .onSuccess { recommendations ->
                    _allRecommendations.value = recommendations
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "추천 데이터를 불러올 수 없습니다"
                }
            
            _isLoading.value = false
        }
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
