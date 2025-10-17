package com.example.archiveandroid.feature.home.record.filter

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

class RecordFilterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        RecordFilterUiState(
            options = defaultCategoryOptions()
        )
    )
    val uiState: StateFlow<RecordFilterUiState> = _uiState.asStateFlow()

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
        // Pastel background with darker text color for readability
        return listOf(
            FilterOption("TRAVEL", "여행", Color(0xFFE8F0FF), Color(0xFF335C99), false),
            FilterOption("READING", "독서", Color(0xFFFFEFE6), Color(0xFFB04A17), false),
            FilterOption("SPORTS", "스포츠", Color(0xFFEAF5FF), Color(0xFF2C5A8A), false),
            FilterOption("EXHIBITION", "전시", Color(0xFFFFF4E5), Color(0xFF9A6B1A), false),
            FilterOption("VOLUNTEER", "봉사", Color(0xFFEFF7FF), Color(0xFF2D6AA3), false),
            FilterOption("MUSICAL", "뮤지컬", Color(0xFFF4E9FF), Color(0xFF6B39A6), false),
            FilterOption("THEATER", "연극", Color(0xFFEFF9FF), Color(0xFF1F6E8C), false),
            FilterOption("CONCERT", "콘서트", Color(0xFFFFE8E8), Color(0xFFB04A17), false),
            FilterOption("MOVIE", "영화", Color(0xFFE8F8E8), Color(0xFF138A52), false),
            FilterOption("FESTIVAL", "축제", Color(0xFFFFF8E8), Color(0xFF9A6B1A), false)
        )
    }
}


