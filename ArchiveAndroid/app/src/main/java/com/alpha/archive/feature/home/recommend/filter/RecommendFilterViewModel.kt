package com.alpha.archive.feature.home.recommend.filter

import androidx.lifecycle.ViewModel
import com.alpha.archive.core.util.CategoryMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * 필터 UI 상태
 */
data class RecommendFilterUiState(
    val selectedCategory: String = "",  // 한글 카테고리
    val startYear: String = "",
    val startMonth: String = "",
    val startDay: String = "",
    val endYear: String = "",
    val endMonth: String = "",
    val endDay: String = "",
    val city: String = "",
    val district: String = "",
    val searchText: String = ""
) {
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
    
    /**
     * API 전송용 RecommendFilterData로 변환
     */
    fun toFilterData(): RecommendFilterData {
        return RecommendFilterData(
            selectedCategory = CategoryMapper.toEnglish(selectedCategory),
            startYear = startYear,
            startMonth = startMonth,
            startDay = startDay,
            endYear = endYear,
            endMonth = endMonth,
            endDay = endDay,
            city = city,
            district = district
        )
    }
}

@HiltViewModel
class RecommendFilterViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(RecommendFilterUiState())
    val uiState: StateFlow<RecommendFilterUiState> = _uiState.asStateFlow()
    
    /**
     * 초기 필터 데이터로 상태 초기화
     */
    fun initWithFilters(filterData: RecommendFilterData) {
        _uiState.value = RecommendFilterUiState(
            selectedCategory = CategoryMapper.toKorean(filterData.selectedCategory),
            startYear = filterData.startYear,
            startMonth = filterData.startMonth,
            startDay = filterData.startDay,
            endYear = filterData.endYear,
            endMonth = filterData.endMonth,
            endDay = filterData.endDay,
            city = filterData.city,
            district = filterData.district
        )
    }
    
    fun updateSelectedCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }
    
    fun updateStartYear(year: String) {
        _uiState.update { it.copy(startYear = year) }
    }
    
    fun updateStartMonth(month: String) {
        _uiState.update { it.copy(startMonth = month) }
    }
    
    fun updateStartDay(day: String) {
        _uiState.update { it.copy(startDay = day) }
    }
    
    fun updateEndYear(year: String) {
        _uiState.update { it.copy(endYear = year) }
    }
    
    fun updateEndMonth(month: String) {
        _uiState.update { it.copy(endMonth = month) }
    }
    
    fun updateEndDay(day: String) {
        _uiState.update { it.copy(endDay = day) }
    }
    
    fun updateCity(city: String) {
        _uiState.update { it.copy(city = city) }
    }
    
    fun updateDistrict(district: String) {
        _uiState.update { it.copy(district = district) }
    }
    
    fun updateSearchText(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }
    
    /**
     * 모든 필터 초기화
     */
    fun resetFilters() {
        _uiState.value = RecommendFilterUiState()
    }
    
    /**
     * 카테고리 토글 (선택/해제)
     */
    fun toggleCategory(category: String) {
        _uiState.update { state ->
            state.copy(
                selectedCategory = if (state.selectedCategory == category) "" else category
            )
        }
    }
}
