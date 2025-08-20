package com.example.archiveandroid

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.kakaoNativeStringAppKey)
        Log.e("APP", "Start")
    }
}