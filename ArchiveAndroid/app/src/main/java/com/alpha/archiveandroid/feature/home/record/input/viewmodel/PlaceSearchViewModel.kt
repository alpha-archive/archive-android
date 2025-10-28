package com.alpha.archiveandroid.feature.home.record.input.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archiveandroid.core.network.toUserFriendlyMessage
import com.alpha.archiveandroid.feature.home.record.input.data.repository.PlaceSearchRepository
import com.alpha.archiveandroid.feature.home.record.input.data.remote.dto.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlaceSearchUiState(
    val searchResults: List<Place> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    private val placeSearchRepository: PlaceSearchRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PlaceSearchUiState())
    val uiState: StateFlow<PlaceSearchUiState> = _uiState.asStateFlow()
    
    fun searchPlaces(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                searchQuery = query
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                searchQuery = query
            )
            
            placeSearchRepository.searchPlaces(query)
                .onSuccess { places ->
                    _uiState.value = _uiState.value.copy(
                        searchResults = places,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.toUserFriendlyMessage(),
                        searchResults = emptyList(),
                        isLoading = false
                    )
                }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearSearchResults() {
        _uiState.value = _uiState.value.copy(
            searchResults = emptyList(),
            searchQuery = ""
        )
    }
}
