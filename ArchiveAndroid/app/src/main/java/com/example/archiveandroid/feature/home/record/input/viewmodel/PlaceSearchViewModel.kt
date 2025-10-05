package com.example.archiveandroid.feature.home.record.input.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.record.input.data.repository.PlaceSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    private val placeSearchRepository: PlaceSearchRepository
) : ViewModel() {
    
    private val _searchResults = MutableStateFlow<List<com.example.archiveandroid.feature.home.record.input.data.remote.dto.PlaceDocument>>(emptyList())
    val searchResults: StateFlow<List<com.example.archiveandroid.feature.home.record.input.data.remote.dto.PlaceDocument>> = _searchResults.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun searchPlaces(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            placeSearchRepository.searchPlaces(query)
                .onSuccess { response ->
                    _searchResults.value = response.documents
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "검색 중 오류가 발생했습니다."
                    _searchResults.value = emptyList()
                }
            
            _isLoading.value = false
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
