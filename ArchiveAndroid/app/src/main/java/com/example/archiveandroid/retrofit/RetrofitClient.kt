package com.example.archiveandroid.retrofit

import com.example.archiveandroid.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val gson = GsonBuilder().create()!!

    private val client : Retrofit = Retrofit
        .Builder()
        .client(
            OkHttpClient.Builder().build()
        )
        // BuildConfig.serverUrl는 Gradle의 buildConfigField에서 정의된 값이며, Retrofit의 모든 API 요청에 사용되는 기본 URL임.
        .baseUrl(BuildConfig.serverUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getInstance() : Retrofit {
        return client
    }
}