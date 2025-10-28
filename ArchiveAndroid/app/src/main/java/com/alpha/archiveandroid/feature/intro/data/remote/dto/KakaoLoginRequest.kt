package com.alpha.archiveandroid.feature.intro.data.remote.dto

import com.google.gson.annotations.SerializedName

data class KakaoLoginRequest(
    @SerializedName("accessToken")
    val accessToken: String // KakaoAccessToken
)
