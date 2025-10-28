package com.example.archiveandroid.feature.home.recommend.data.remote.dto

import com.google.gson.annotations.SerializedName

// 추천 활동 목록용 DTO (실제 API 구조에 맞춤)
data class RecommendActivityDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("category")
    val category: String, // MUSICAL, THEATER, MOVIE, EXHIBITION 등
    @SerializedName("startAt")
    val startAt: String,
    @SerializedName("endAt")
    val endAt: String,
    @SerializedName("placeName")
    val placeName: String,
    @SerializedName("placeAddress")
    val placeAddress: String,
    @SerializedName("placeCity")
    val placeCity: String,
    @SerializedName("placeDistrict")
    val placeDistrict: String,
    @SerializedName("placeLatitude")
    val placeLatitude: Double,
    @SerializedName("placeLongitude")
    val placeLongitude: Double,
    @SerializedName("placePhone")
    val placePhone: String?,
    @SerializedName("placeHomepage")
    val placeHomepage: String?,
    @SerializedName("ingestedAt")
    val ingestedAt: String
)

// 추천 활동 상세용 DTO (API 명세서 기준)
data class RecommendActivityDetailDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("category")
    val category: String,
    @SerializedName("thumbnailImageUrl")
    val thumbnailImageUrl: String?,
    @SerializedName("startAt")
    val startAt: String,
    @SerializedName("endAt")
    val endAt: String,
    @SerializedName("placeName")
    val placeName: String,
    @SerializedName("placeAddress")
    val placeAddress: String,
    @SerializedName("placeCity")
    val placeCity: String,
    @SerializedName("placeDistrict")
    val placeDistrict: String,
    @SerializedName("placeLatitude")
    val placeLatitude: Double,
    @SerializedName("placeLongitude")
    val placeLongitude: Double,
    @SerializedName("placePhone")
    val placePhone: String?,
    @SerializedName("placeHomepage")
    val placeHomepage: String?,
    @SerializedName("ingestedAt")
    val ingestedAt: String
)

// 추천 이미지 정보 DTO
data class RecommendImageDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("contentType")
    val contentType: String
)

// 추천 요청 DTO (필터링용)
data class RecommendRequest(
    @SerializedName("categories")
    val categories: List<String>? = null,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("distance")
    val distance: Int? = null,
    @SerializedName("dateRange")
    val dateRange: DateRange? = null
)

data class DateRange(
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String
)

// 페이징 응답 구조 (실제 API 구조에 맞춤)
data class RecommendActivityListResponse(
    @SerializedName("lastCursor")
    val lastCursor: String,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("content")
    val content: List<RecommendActivityDto>
)
