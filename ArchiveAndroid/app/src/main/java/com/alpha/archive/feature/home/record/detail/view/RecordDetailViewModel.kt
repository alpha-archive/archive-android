package com.alpha.archive.feature.home.recorddetail.view

import androidx.lifecycle.viewModelScope
import com.alpha.archive.core.network.toUserFriendlyMessage
import com.alpha.archive.core.ui.BaseViewModel
import com.alpha.archive.feature.home.record.data.repository.ActivityRepository
import com.alpha.archive.feature.home.record.data.remote.dto.ActivityDetailDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val recordData: ActivityDetailDto? = null,
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false
)

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    private val repository: ActivityRepository
) : BaseViewModel<RecordDetailUiState>() {

    private val _uiState = MutableStateFlow(RecordDetailUiState())
    val uiState: StateFlow<RecordDetailUiState> = _uiState.asStateFlow()

    fun loadRecordDetail(recordId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.getActivity(recordId)
                .onSuccess { record ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recordData = record
                    )
                }
                .onFailure { e ->
                    _uiState.updateError(e) { msg ->
                        copy(
                            isLoading = false,
                            error = msg
                        )
                    }
                }
        }
    }

    fun deleteActivity(activityId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                error = null
            )

            repository.deleteActivity(activityId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        isDeleted = true
                    )
                }
                .onFailure { e ->
                    _uiState.updateError(e) { msg ->
                        copy(
                            isDeleting = false,
                            error = msg
                        )
                    }
                }
        }
    }
}