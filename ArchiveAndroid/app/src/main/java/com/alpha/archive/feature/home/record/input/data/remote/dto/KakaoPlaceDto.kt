package com.alpha.archive.feature.home.record.input.data.remote.dto

import com.google.gson.annotations.SerializedName

data class KakaoPlaceResponse(
    @SerializedName("meta")
    val meta: KakaoPlaceMeta,
    @SerializedName("documents")
    val documents: List<KakaoPlaceDocument>
)

data class KakaoPlaceMeta(
    @SerializedName("same_name")
    val sameName: KakaoPlaceSameName,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean
)

data class KakaoPlaceSameName(
    @SerializedName("region")
    val region: List<String>,
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("selected_region")
    val selectedRegion: String
)

data class KakaoPlaceDocument(
    @SerializedName("id")
    val id: String,
    @SerializedName("place_name")
    val placeName: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_group_code")
    val categoryGroupCode: String,
    @SerializedName("category_group_name")
    val categoryGroupName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address_name")
    val addressName: String,
    @SerializedName("road_address_name")
    val roadAddressName: String,
    @SerializedName("x")
    val longitude: String,
    @SerializedName("y")
    val latitude: String,
    @SerializedName("place_url")
    val placeUrl: String,
    @SerializedName("distance")
    val distance: String
)

data class Place(
    val id: String,
    val name: String,
    val category: String,
    val phone: String,
    val address: String,
    val roadAddress: String,
    val longitude: String,
    val latitude: String,
    val distance: String
)

fun KakaoPlaceDocument.toPlace(): Place {
    return Place(
        id = this.id,
        name = this.placeName,
        category = this.categoryName,
        phone = this.phone,
        address = this.addressName,
        roadAddress = this.roadAddressName,
        longitude = this.longitude,
        latitude = this.latitude,
        distance = this.distance
    )
}
