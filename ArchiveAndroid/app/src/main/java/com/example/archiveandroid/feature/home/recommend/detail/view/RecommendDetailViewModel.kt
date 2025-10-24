package com.example.archiveandroid.feature.home.recommend.detail.view

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.core.ui.components.DetailScreenState
import com.example.archiveandroid.core.util.DateFormatter
import com.example.archiveandroid.feature.home.recommend.data.repository.RecommendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendDetailViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
) : ViewModel() {
    
    private var activityId: String = ""
    
    private val _uiState = MutableStateFlow(
        DetailScreenState(isLoading = true)
    )
    val uiState: StateFlow<DetailScreenState> = _uiState.asStateFlow()
    
    fun loadActivityDetail(activityId: String) {
        this.activityId = activityId
        
        if (activityId.isEmpty()) {
            _uiState.value = DetailScreenState(
                isLoading = false,
                error = "활동 ID가 없습니다"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            recommendRepository.getRecommendActivityDetail(activityId)
                .onSuccess { detailDto ->
                    val detailData = detailDto.toDetailScreenData()
                    _uiState.value = DetailScreenState(
                        isLoading = false,
                        data = detailData
                    )
                }
                .onFailure { exception ->
                    // 통신 실패 시 더미 데이터 표시
                    val dummyData = createDummyData(activityId)
                    _uiState.value = DetailScreenState(
                        isLoading = false,
                        data = dummyData
                    )
                }
        }
    }
    
    fun refresh() {
        if (activityId.isNotEmpty()) {
            loadActivityDetail(activityId)
        }
    }
    
    /**
     * 더미 데이터 생성
     */
    private fun createDummyData(activityId: String): com.example.archiveandroid.core.ui.components.DetailScreenData {
        val dummyCategories = listOf("여행", "독서", "스포츠 관람", "운동", "전시", "봉사", "뮤지컬", "악기 연주")
        val dummyTitles = listOf(
            "서울 한강공원에서 즐기는 피크닉",
            "국립중앙박물관 특별전시 관람",
            "서울월드컵경기장에서 열리는 축구 경기",
            "한강에서 즐기는 조깅과 자전거 타기",
            "동대문디자인플라자(DDP) 전시 관람",
            "지역아동센터에서 진행하는 봉사활동",
            "세종문화회관 뮤지컬 공연 관람",
            "강남에서 즐기는 피아노 레슨"
        )
        val dummyDates = listOf(
            "12/25 ~ 12/31",
            "1/15 ~ 3/15",
            "2/10",
            "매주 토요일",
            "1/20 ~ 2/28",
            "매주 일요일",
            "2/14 ~ 2/16",
            "매주 화, 목"
        )
        val dummyLocations = listOf(
            "서울 한강공원 (여의도)",
            "국립중앙박물관 (용산구)",
            "서울월드컵경기장 (마포구)",
            "한강공원 (여의도)",
            "동대문디자인플라자 (중구)",
            "지역아동센터 (강동구)",
            "세종문화회관 (중구)",
            "강남 피아노 학원 (강남구)"
        )
        val dummyDescriptions = listOf(
            "한강의 아름다운 경관을 감상하며 가족, 친구들과 함께 즐기는 피크닉 활동입니다. 바람을 맞으며 휴식을 취하고, 다양한 야외 활동을 즐길 수 있습니다.\n\n• 참가비: 무료\n• 준비물: 돗자리, 간식, 음료수\n• 주차: 한강공원 주차장 이용 가능\n• 주의사항: 쓰레기는 반드시 가져가기",
            "국립중앙박물관에서 열리는 특별 전시를 관람하며 우리나라의 역사와 문화를 깊이 있게 이해할 수 있는 기회입니다.\n\n• 관람료: 성인 3,000원, 청소년 2,000원\n• 관람시간: 09:00-18:00 (화-일)\n• 예약: 온라인 사전 예약 필수\n• 주의사항: 촬영 금지 구역 준수",
            "서울월드컵경기장에서 열리는 프로축구 경기를 관람하며 스포츠의 열정과 감동을 느낄 수 있습니다.\n\n• 티켓가격: 15,000원~50,000원\n• 경기시간: 19:30-21:30\n• 교통: 지하철 6호선 월드컵경기장역\n• 주의사항: 금연 구역, 음주 금지",
            "한강을 따라 조깅하거나 자전거를 타며 건강한 운동을 즐기고, 자연 속에서 스트레스를 해소할 수 있습니다.\n\n• 참가비: 무료\n• 준비물: 운동복, 운동화, 물병\n• 대여: 자전거 대여소 운영 (시간당 3,000원)\n• 주의사항: 안전모 착용 권장",
            "동대문디자인플라자에서 열리는 다양한 디자인 전시를 관람하며 창의적 영감을 얻을 수 있습니다.\n\n• 관람료: 성인 8,000원, 청소년 6,000원\n• 관람시간: 10:00-20:00 (화-일)\n• 예약: 온라인 사전 예약 가능\n• 주의사항: 휴대폰 진동 모드",
            "지역아동센터에서 아이들과 함께하는 의미 있는 봉사활동으로 지역사회에 기여할 수 있습니다.\n\n• 참가비: 무료\n• 활동시간: 14:00-17:00\n• 준비물: 편한 복장, 긍정적인 마음\n• 주의사항: 사전 교육 필수",
            "세종문화회관에서 열리는 뮤지컬 공연을 관람하며 음악과 연극의 아름다움을 경험할 수 있습니다.\n\n• 티켓가격: 30,000원~100,000원\n• 공연시간: 19:30-22:00\n• 예약: 인터파크, 예스24 등\n• 주의사항: 공연 중 휴대폰 사용 금지",
            "강남의 전문 피아노 학원에서 개인 맞춤형 레슨을 받으며 음악 실력을 향상시킬 수 있습니다.\n\n• 레슨비: 시간당 50,000원\n• 레슨시간: 1시간 (개인별 조정 가능)\n• 준비물: 악보, 연필\n• 주의사항: 사전 연락 필수"
        )
        
        val categoryIndex = activityId.hashCode() % dummyCategories.size
        val category = dummyCategories[categoryIndex]
        val (bgColor, fgColor) = getCategoryColor(category)
        
        return com.example.archiveandroid.core.ui.components.DetailScreenData(
            title = dummyTitles[categoryIndex],
            categoryDisplayName = category,
            activityDate = dummyDates[categoryIndex],
            location = dummyLocations[categoryIndex],
            memo = dummyDescriptions[categoryIndex],
            images = listOf(
                "https://picsum.photos/400/300?random=1",
                "https://picsum.photos/400/300?random=2",
                "https://picsum.photos/400/300?random=3"
            ),
            recommendationReason = null, // 추천 이유 제거
            categoryBg = bgColor,
            categoryFg = fgColor
        )
    }
}

/**
 * RecommendActivityDetailDto를 DetailScreenData로 변환
 */
private fun com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendActivityDetailDto.toDetailScreenData(): com.example.archiveandroid.core.ui.components.DetailScreenData {
    // 날짜 포맷팅
    val dateText = DateFormatter.formatDateRange(this.startDate, this.endDate)
    
    // 이미지 URL 리스트 생성
    val imageUrls = this.images.map { it.imageUrl }
    
    // 카테고리 색상
    val (bgColor, fgColor) = getCategoryColor(this.categoryDisplayName)
    
    return com.example.archiveandroid.core.ui.components.DetailScreenData(
        title = this.title,
        categoryDisplayName = this.categoryDisplayName,
        activityDate = dateText,
        location = this.location,
        memo = this.description,
        images = imageUrls,
        recommendationReason = this.recommendationReason,
        categoryBg = bgColor,
        categoryFg = fgColor
    )
}

/**
 * 카테고리에 따른 색상 반환
 */
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
        else -> Pair(Color(0xFFE0E0E0), Color(0xFF666666))
    }
}
