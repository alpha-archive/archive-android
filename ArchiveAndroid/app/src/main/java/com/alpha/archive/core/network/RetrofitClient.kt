package com.alpha.archive.core.network

import com.alpha.archive.BuildConfig
import com.alpha.archive.feature.home.record.input.data.remote.KakaoPlaceApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {
    private val gson = GsonBuilder()
        .create()

    @Provides
    @Singleton
    fun okHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }
        builder.addInterceptor(authInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    @Named("AppRetrofit")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val normalizedBaseUrl = if (BuildConfig.serverUrl.endsWith("/")) {
            BuildConfig.serverUrl
        } else {
            BuildConfig.serverUrl + "/"
        }
        return Retrofit.Builder()
            .baseUrl(normalizedBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("KakaoRetrofit")
    fun provideKakaoRetrofit(): Retrofit {
        val kakaoGson = GsonBuilder()
            .create()
            
        val kakaoOkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(logging)
                }
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .client(kakaoOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create(kakaoGson))
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoPlaceApi(@Named("KakaoRetrofit") kakaoRetrofit: Retrofit): KakaoPlaceApi {
        return kakaoRetrofit.create(KakaoPlaceApi::class.java)
    }
}