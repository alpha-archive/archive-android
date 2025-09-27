package com.example.archiveandroid.feature.home.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.archiveandroid.feature.home.record.data.repository.ActivityRepository
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    
    val activities: Flow<PagingData<ActivityDto>> = activityRepository
        .getActivitiesPaging()
        .cachedIn(viewModelScope)
}
