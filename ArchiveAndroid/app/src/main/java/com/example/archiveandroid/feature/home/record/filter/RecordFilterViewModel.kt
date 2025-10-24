package com.example.archiveandroid.feature.home.record.filter

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                        val (bgColor, fgColor) = getCategoryColor(category)
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

    private fun getCategoryColor(category: String): Pair<Color, Color> {
        return when (category) {
            "여행" -> Pair(Color(0xFFE8F0FF), Color(0xFF335C99))
            "독서" -> Pair(Color(0xFFFFEFE6), Color(0xFFB04A17))
            "스포츠 관람" -> Pair(Color(0xFFEAF5FF), Color(0xFF2C5A8A))
            "운동" -> Pair(Color(0xFFE7FFF2), Color(0xFF138A52))
            "전시" -> Pair(Color(0xFFFFF4E5), Color(0xFF9A6B1A))
            "봉사" -> Pair(Color(0xFFEFF7FF), Color(0xFF2D6AA3))
            "뮤지컬" -> Pair(Color(0xFFF4E9FF), Color(0xFF6B39A6))
            "악기 연주" -> Pair(Color(0xFFEFF9FF), Color(0xFF1F6E8C))
            "연극" -> Pair(Color(0xFFE8F5FF), Color(0xFF2C5A8A))
            "콘서트" -> Pair(Color(0xFFFFE8E8), Color(0xFFB04A17))
            "영화" -> Pair(Color(0xFFE8F8E8), Color(0xFF138A52))
            "축제" -> Pair(Color(0xFFFFF8E8), Color(0xFF9A6B1A))
            "워크샵" -> Pair(Color(0xFFEFF9FF), Color(0xFF1F6E8C))
            "스포츠" -> Pair(Color(0xFFEAF5FF), Color(0xFF2C5A8A))
            "요리" -> Pair(Color(0xFFFFF4E5), Color(0xFF9A6B1A))
            "취미" -> Pair(Color(0xFFF4E9FF), Color(0xFF6B39A6))
            "스터디" -> Pair(Color(0xFFE8F0FF), Color(0xFF335C99))
            "네트워킹" -> Pair(Color(0xFFEFF7FF), Color(0xFF2D6AA3))
            "체험" -> Pair(Color(0xFFFFEFE6), Color(0xFFB04A17))
            else -> Pair(Color(0xFFE0E0E0), Color(0xFF666666))
        }
    }

    private fun defaultCategoryOptions(): List<FilterOption> {
        // Fallback: Pastel background with darker text color for readability
        return listOf(
            FilterOption("여행", "여행", Color(0xFFE8F0FF), Color(0xFF335C99), false),
            FilterOption("독서", "독서", Color(0xFFFFEFE6), Color(0xFFB04A17), false),
            FilterOption("스포츠", "스포츠", Color(0xFFEAF5FF), Color(0xFF2C5A8A), false),
            FilterOption("전시", "전시", Color(0xFFFFF4E5), Color(0xFF9A6B1A), false),
            FilterOption("봉사", "봉사", Color(0xFFEFF7FF), Color(0xFF2D6AA3), false),
            FilterOption("뮤지컬", "뮤지컬", Color(0xFFF4E9FF), Color(0xFF6B39A6), false),
            FilterOption("연극", "연극", Color(0xFFEFF9FF), Color(0xFF1F6E8C), false),
            FilterOption("콘서트", "콘서트", Color(0xFFFFE8E8), Color(0xFFB04A17), false),
            FilterOption("영화", "영화", Color(0xFFE8F8E8), Color(0xFF138A52), false),
            FilterOption("축제", "축제", Color(0xFFFFF8E8), Color(0xFF9A6B1A), false)
        )
    }
}


