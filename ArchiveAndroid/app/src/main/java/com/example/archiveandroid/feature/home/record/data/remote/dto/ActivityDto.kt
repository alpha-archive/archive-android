package com.example.archiveandroid.feature.home.record.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ActivityDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class ActivityListResponse(
    @SerializedName("activities")
    val activities: List<ActivityDto>,
    @SerializedName("nextCursor")
    val nextCursor: String?,
    @SerializedName("hasMore")
    val hasMore: Boolean
)

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
