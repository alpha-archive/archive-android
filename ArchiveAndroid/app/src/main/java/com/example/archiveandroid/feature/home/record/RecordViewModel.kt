package com.example.archiveandroid.feature.home.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.record.data.repository.ActivityRepository
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityListResponse
import com.example.archiveandroid.feature.home.record.ui.RecordItem
import com.example.archiveandroid.feature.home.record.ui.RecordItemMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color
import javax.inject.Inject

data class RecordUiState(
    val records: List<RecordItem> = emptyList(),
    val nextCursor: String? = null,
    val hasMore: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RecordViewModel @Inject constructor(
    val activityRepository: ActivityRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()
    
    init {
        loadActivities()
    }
    
    fun loadActivities(category: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            // TODO: 서버 API 연결 시 실제 API 호출로 변경
            // activityRepository.getActivities(category = category)
            
            // 임시 더미 데이터
            delay(1000) // 로딩 시뮬레이션
            val dummyRecords = getDummyRecords(category)
            _uiState.value = _uiState.value.copy(
                records = dummyRecords,
                nextCursor = "dummy_cursor_2",
                hasMore = true,
                isLoading = false
            )
        }
    }
    
    fun loadMoreActivities() {
        val currentState = _uiState.value
        if (currentState.isLoadingMore || !currentState.hasMore || currentState.nextCursor == null) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoadingMore = true)
            
            // TODO: 서버 API 연결 시 실제 API 호출로 변경
            // activityRepository.getActivities(cursor = currentState.nextCursor)
            
            // 임시 더미 데이터
            delay(800) // 로딩 시뮬레이션
            val moreDummyRecords = getMoreDummyRecords(currentState.records.size)
            _uiState.value = _uiState.value.copy(
                records = currentState.records + moreDummyRecords,
                nextCursor = if (moreDummyRecords.size < 3) null else "dummy_cursor_${currentState.records.size + moreDummyRecords.size}",
                hasMore = moreDummyRecords.size >= 3,
                isLoadingMore = false
            )
        }
    }
    
    fun refresh() {
        loadActivities()
    }
    
    private fun getDummyRecords(category: String? = null): List<RecordItem> {
        val allRecords = listOf(
            RecordItem(
                id = "1",
                title = "KOSS 여름 LT",
                location = "강원도 양양군 정암해변",
                categoryLabel = "여행",
                categoryBg = Color(0xFFE8F0FF),
                categoryFg = Color(0xFF335C99)
            ),
            RecordItem(
                id = "2",
                title = "국립현대미술관 '미래의 기억'",
                location = "서울시 과천시",
                categoryLabel = "전시",
                categoryBg = Color(0xFFFFF4E5),
                categoryFg = Color(0xFF9A6B1A)
            ),
            RecordItem(
                id = "3",
                title = "주말 러닝 5km",
                location = "한강공원",
                categoryLabel = "운동",
                categoryBg = Color(0xFFE7FFF2),
                categoryFg = Color(0xFF138A52)
            ),
            RecordItem(
                id = "4",
                title = "뮤지컬 '레미제라블' 관람",
                location = "샤롯데씨어터",
                categoryLabel = "뮤지컬",
                categoryBg = Color(0xFFF4E9FF),
                categoryFg = Color(0xFF6B39A6)
            ),
            RecordItem(
                id = "5",
                title = "도서관에서 독서",
                location = "국립중앙도서관",
                categoryLabel = "독서",
                categoryBg = Color(0xFFFFEFE6),
                categoryFg = Color(0xFFB04A17)
            )
        )
        
        return if (category != null) {
            allRecords.filter { it.categoryLabel == category }
        } else {
            allRecords
        }
    }
    
    private fun getMoreDummyRecords(currentCount: Int): List<RecordItem> {
        val moreRecords = listOf(
            RecordItem(
                id = "${currentCount + 1}",
                title = "축구 경기 관람",
                location = "서울월드컵경기장",
                categoryLabel = "스포츠 관람",
                categoryBg = Color(0xFFEAF5FF),
                categoryFg = Color(0xFF2C5A8A)
            ),
            RecordItem(
                id = "${currentCount + 2}",
                title = "피아노 연주",
                location = "집",
                categoryLabel = "악기 연주",
                categoryBg = Color(0xFFEFF9FF),
                categoryFg = Color(0xFF1F6E8C)
            ),
            RecordItem(
                id = "${currentCount + 3}",
                title = "노인복지센터 봉사",
                location = "강남구 노인복지센터",
                categoryLabel = "봉사",
                categoryBg = Color(0xFFEFF7FF),
                categoryFg = Color(0xFF2D6AA3)
            )
        )
        
        // 무한 스크롤 시뮬레이션을 위해 3개씩만 반환
        return moreRecords.take(3)
    }
}
