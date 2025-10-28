package com.example.archiveandroid.feature.home.record.filter

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.core.util.CategoryColorGenerator
import com.example.archiveandroid.feature.home.record.data.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FilterOption(
    val id: String,
    val label: String,
    val bgColor: Color,
    val textColor: Color,
    val selected: Boolean
)

data class RecordFilterUiState(
    val options: List<FilterOption> = emptyList()
)

@HiltViewModel
class RecordFilterViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        RecordFilterUiState(
            options = emptyList()
        )
    )
    val uiState: StateFlow<RecordFilterUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    // 공개 함수로 변경하여 외부에서 호출 가능하도록
    fun loadCategories() {
        viewModelScope.launch {
            activityRepository.getActivities()
                .onSuccess { activities ->
                    // 현재 선택된 필터들을 저장
                    val currentSelectedIds = _uiState.value.options
                        .filter { it.selected }
                        .map { it.id }
                        .toSet()
                    
                    // 실제 데이터에서 사용된 카테고리들을 추출
                    val uniqueCategories = activities
                        .map { it.categoryDisplayName }
                        .distinct()
                        .sorted()
                    
                    // 카테고리를 FilterOption으로 변환 (기존 선택 상태 유지)
                    val options = uniqueCategories.map { category ->
                        val (bgColor, fgColor) = CategoryColorGenerator.getCategoryColors(category)
                        FilterOption(
                            id = category, // 한글 카테고리명을 ID로 사용
                            label = category,
                            bgColor = bgColor,
                            textColor = fgColor,
                            selected = currentSelectedIds.contains(category)
                        )
                    }
                    
                    _uiState.update { state ->
                        state.copy(options = options)
                    }
                }
                .onFailure {
                    // 실패 시 기본 카테고리 사용
                    _uiState.update { state ->
                        state.copy(options = defaultCategoryOptions())
                    }
                }
        }
    }

    fun toggle(id: String) {
        _uiState.update { state ->
            state.copy(
                options = state.options.map { opt ->
                    if (opt.id == id) opt.copy(selected = !opt.selected) else opt
                }
            )
        }
    }

    fun clearAll() {
        _uiState.update { state -> state.copy(options = state.options.map { it.copy(selected = false) }) }
    }

    fun initializeWithSelectedFilters(selectedIds: Set<String>) {
        _uiState.update { state ->
            state.copy(
                options = state.options.map { option ->
                    option.copy(selected = selectedIds.contains(option.id))
                }
            )
        }
    }

    private fun defaultCategoryOptions(): List<FilterOption> {
        val defaultCategories = listOf("여행", "독서", "스포츠", "전시", "봉사", "뮤지컬", "연극", "콘서트", "영화", "축제")
        return defaultCategories.map { category ->
            val (bgColor, fgColor) = CategoryColorGenerator.getCategoryColors(category)
            FilterOption(category, category, bgColor, fgColor, false)
        }
    }
}


