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

    private fun defaultCategoryOptions(): List<FilterOption> {
        // Pastel background with darker text color for readability
        return listOf(
            FilterOption("travel", "여행", Color(0xFFE8F0FF), Color(0xFF335C99), false),
            FilterOption("reading", "독서", Color(0xFFFFEFE6), Color(0xFFB04A17), false),
            FilterOption("sports", "스포츠 관람", Color(0xFFEAF5FF), Color(0xFF2C5A8A), false),
            FilterOption("workout", "운동", Color(0xFFE7FFF2), Color(0xFF138A52), false),
            FilterOption("exhibition", "전시", Color(0xFFFFF4E5), Color(0xFF9A6B1A), false),
            FilterOption("volunteer", "봉사", Color(0xFFEFF7FF), Color(0xFF2D6AA3), false),
            FilterOption("musical", "뮤지컬", Color(0xFFF4E9FF), Color(0xFF6B39A6), false),
            FilterOption("instrument", "악기 연주", Color(0xFFEFF9FF), Color(0xFF1F6E8C), false)
        )
    }
}


