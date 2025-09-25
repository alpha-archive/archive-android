package com.example.archiveandroid.feature.record.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.record.data.remote.dto.ActivityCreateRequest
import com.example.archiveandroid.feature.record.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: RecordRepository
) : ViewModel() {

    sealed interface UiState {
        data class Editing(
            val submitting: Boolean = false,
            val errorMessage: String? = null
        ) : UiState
        data object Success : UiState
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Editing())
    val uiState: StateFlow<UiState> = _uiState

    fun onSaveClicked(req: ActivityCreateRequest) {
        val cur = _uiState.value
        if (cur is UiState.Editing && cur.submitting) return

        if (req.title.isBlank()) {
            _uiState.value = UiState.Editing(submitting = false, errorMessage = "활동명을 입력해주세요!")
            return
        }
        if (req.category.isBlank()) {
            _uiState.value = UiState.Editing(submitting = false, errorMessage = "카테고리를 선택해주세요!")
            return
        }

        _uiState.value = UiState.Editing(submitting = true, errorMessage = null)

        viewModelScope.launch {
            repository.createActivity(req)
                .onSuccess { _uiState.value = UiState.Success }
                .onFailure { exception ->
                    _uiState.value = UiState.Editing(
                        submitting = false,
                        errorMessage = exception.message ?: "저장 실패"
                    )
                }
        }
    }
}
