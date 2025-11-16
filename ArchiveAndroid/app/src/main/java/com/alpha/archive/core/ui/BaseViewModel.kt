package com.alpha.archive.core.ui

import androidx.lifecycle.ViewModel
import com.alpha.archive.core.network.toUserFriendlyMessage
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<UiState> : ViewModel() {

    protected fun MutableStateFlow<UiState>.updateError(
        throwable: Throwable,
        transform: UiState.(String) -> UiState
    ) {
        val message = throwable.toUserFriendlyMessage()
        this.value = this.value.transform(message)
    }
}
