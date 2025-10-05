package com.example.archiveandroid.feature.home.record.input.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlaceSearchResponse(
    @SerializedName("meta") val meta: PlaceSearchMeta,
    @SerializedName("documents") val documents: List<PlaceDocument>
)

data class PlaceSearchMeta(
    @SerializedName("is_end") val isEnd: Boolean,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("total_count") val totalCount: Int
)

data class PlaceDocument(
    @SerializedName("id") val id: String,
    @SerializedName("place_name") val placeName: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("category_group_code") val categoryGroupCode: String,
    @SerializedName("category_group_name") val categoryGroupName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address_name") val addressName: String,
    @SerializedName("road_address_name") val roadAddressName: String,
    @SerializedName("x") val longitude: String,    // 경도
    @SerializedName("y") val latitude: String,     // 위도
    @SerializedName("place_url") val placeUrl: String,
    @SerializedName("distance") val distance: String
)
