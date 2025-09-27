package com.example.archiveandroid.feature.home.record.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import kotlinx.coroutines.delay

/**
 * 더미 데이터를 사용하는 RecordPagingSource
 * 서버 API 완성 전까지 사용
 */
class DummyRecordPagingSource : PagingSource<String, ActivityDto>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ActivityDto> {
        return try {
            // 로딩 시뮬레이션
            delay(500)
            
            val cursor = params.key ?: ""
            val page = if (cursor.isEmpty()) 1 else cursor.toIntOrNull() ?: 1
            val pageSize = params.loadSize
            
            val dummyData = generateDummyData(page, pageSize)
            val nextCursor = if (dummyData.size == pageSize) (page + 1).toString() else null
            
            LoadResult.Page(
                data = dummyData,
                prevKey = null, // 이전 페이지는 없음
                nextKey = nextCursor
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, ActivityDto>): String? {
        return null // 첫 페이지로 새로고침
    }
    
    private fun generateDummyData(page: Int, pageSize: Int): List<ActivityDto> {
        val startId = (page - 1) * pageSize + 1
        val endId = startId + pageSize - 1
        
        return (startId..endId).map { id ->
            ActivityDto(
                id = id.toString(),
                title = getDummyTitle(id),
                location = getDummyLocation(id),
                category = getDummyCategory(id),
                activityDate = getDummyDate(id),
                rating = getDummyRating(id),
                memo = getDummyMemo(id),
                imageUrls = getDummyImages(id),
                createdAt = getDummyCreatedAt(id),
                updatedAt = getDummyUpdatedAt(id)
            )
        }
    }
    
    private fun getDummyTitle(id: Int): String {
        val titles = listOf(
            "KOSS 여름 LT",
            "국립현대미술관 '미래의 기억'",
            "주말 러닝 5km",
            "뮤지컬 '레미제라블' 관람",
            "도서관에서 독서",
            "축구 경기 관람",
            "피아노 연주",
            "노인복지센터 봉사",
            "카페에서 작업",
            "영화 '인터스텔라' 관람"
        )
        return titles[id % titles.size]
    }
    
    private fun getDummyLocation(id: Int): String {
        val locations = listOf(
            "강원도 양양군 정암해변",
            "서울시 과천시",
            "한강공원",
            "샤롯데씨어터",
            "국립중앙도서관",
            "서울월드컵경기장",
            "집",
            "강남구 노인복지센터",
            "스타벅스 강남점",
            "CGV 용산아이파크몰"
        )
        return locations[id % locations.size]
    }
    
    private fun getDummyCategory(id: Int): String {
        val categories = listOf("여행", "전시", "운동", "뮤지컬", "독서", "스포츠", "음악", "봉사", "작업", "영화")
        return categories[id % categories.size]
    }
    
    private fun getDummyDate(id: Int): String {
        val baseDate = "2024-01-01"
        val daysToAdd = id * 3
        return baseDate // 실제로는 날짜 계산 로직 필요
    }
    
    private fun getDummyRating(id: Int): Int {
        return (1..5).random()
    }
    
    private fun getDummyMemo(id: Int): String {
        val memos = listOf(
            "정말 즐거웠던 시간이었다.",
            "다음에도 꼭 가고 싶다.",
            "친구들과 함께해서 더 좋았다.",
            "혼자만의 시간이었다.",
            "가족과 함께한 소중한 시간."
        )
        return memos[id % memos.size]
    }
    
    private fun getDummyImages(id: Int): List<String> {
        return if (id % 3 == 0) {
            listOf("https://picsum.photos/400/300?random=$id")
        } else {
            emptyList()
        }
    }
    
    private fun getDummyCreatedAt(id: Int): String {
        return "2024-01-01T00:00:00Z"
    }
    
    private fun getDummyUpdatedAt(id: Int): String {
        return "2024-01-01T00:00:00Z"
    }
}
