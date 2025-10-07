package com.example.archiveandroid.feature.home.record.data.remote.dto

import com.google.gson.annotations.SerializedName

// 목록용 DTO
data class ActivityListItemDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("categoryDisplayName")
    val categoryDisplayName: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("activityDate")
    val activityDate: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("thumbnailImageUrl")
    val thumbnailImageUrl: String?,
    @SerializedName("isPublicEvent")
    val isPublicEvent: Boolean,
    @SerializedName("imageCount")
    val imageCount: Int
)

// 상세용 DTO
data class ActivityDetailDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("categoryDisplayName")
    val categoryDisplayName: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("activityDate")
    val activityDate: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("memo")
    val memo: String,
    @SerializedName("isPublicEvent")
    val isPublicEvent: Boolean,
    @SerializedName("publicEventInfo")
    val publicEventInfo: PublicEventInfo?,
    @SerializedName("images")
    val images: List<ImageInfo>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

// 공개 이벤트 정보 DTO
data class PublicEventInfo(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("startAt")
    val startAt: String,
    @SerializedName("endAt")
    val endAt: String
)

// 이미지 정보 DTO
data class ImageInfo(
    @SerializedName("id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("contentType")
    val contentType: String
)

// 기존 호환성을 위한 타입 별칭
typealias ActivityDto = ActivityListItemDto

data class CreateActivityRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("imageUrl")
    val imageUrl: String? = null
)

data class UpdateActivityRequest(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null
)
