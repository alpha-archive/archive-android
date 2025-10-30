package com.alpha.archive.feature.intro.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AppTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
