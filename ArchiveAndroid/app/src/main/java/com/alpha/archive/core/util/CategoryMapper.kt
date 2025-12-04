package com.alpha.archive.core.util

/**
 * 카테고리 영문 enum과 한글 표시명 간의 변환을 담당하는 유틸리티
 * 모든 카테고리 변환 로직을 이 파일에서 중앙 관리합니다.
 */
object CategoryMapper {
    
    /**
     * 지원하는 모든 카테고리 (한글 표시용)
     */
    val allCategories = listOf(
        "야외활동", "전시", "뮤지컬", "연극", "영화", "콘서트",
        "축제", "워크샵", "스포츠", "여행", "독서", "요리",
        "봉사", "취미", "스터디", "네트워킹", "기타"
    )
    
    private val koreanToEnglish = mapOf(
        "뮤지컬" to "MUSICAL",
        "연극" to "THEATER",
        "영화" to "MOVIE",
        "전시" to "EXHIBITION",
        "요리" to "COOKING",
        "봉사" to "VOLUNTEER",
        "독서" to "READING",
        "콘서트" to "CONCERT",
        "축제" to "FESTIVAL",
        "워크샵" to "WORKSHOP",
        "스포츠" to "SPORTS",
        "여행" to "TRAVEL",
        "야외활동" to "OUTDOOR",
        "취미" to "HOBBY",
        "스터디" to "STUDY",
        "네트워킹" to "NETWORKING",
        "기타" to "OTHER"
    )
    
    private val englishToKorean = mapOf(
        "MUSICAL" to "뮤지컬",
        "THEATER" to "연극",
        "MOVIE" to "영화",
        "EXHIBITION" to "전시",
        "COOKING" to "요리",
        "VOLUNTEER" to "봉사",
        "READING" to "독서",
        "CONCERT" to "콘서트",
        "FESTIVAL" to "축제",
        "WORKSHOP" to "워크샵",
        "SPORTS" to "스포츠",
        "TRAVEL" to "여행",
        "OUTDOOR" to "야외활동",
        "HOBBY" to "취미",
        "STUDY" to "스터디",
        "NETWORKING" to "네트워킹",
        "OTHER" to "기타"
    )
    
    /**
     * 영문 카테고리를 한글 표시명으로 변환
     * @param englishCategory API에서 받은 영문 카테고리 (예: "MUSICAL", "OUTDOOR")
     * @return 한글 표시명 (예: "뮤지컬", "체험"), 매핑되지 않으면 원본 반환
     */
    fun toKorean(englishCategory: String): String {
        return englishToKorean[englishCategory.uppercase()] ?: englishCategory
    }
    
    /**
     * 한글 카테고리를 영문 enum으로 변환
     * @param koreanCategory 한글 카테고리 (예: "뮤지컬", "체험")
     * @return 영문 enum (예: "MUSICAL", "OUTDOOR"), 매핑되지 않으면 빈 문자열 반환
     */
    fun toEnglish(koreanCategory: String): String {
        return koreanToEnglish[koreanCategory] ?: ""
    }
}

