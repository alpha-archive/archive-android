package com.alpha.archiveandroid.core.storage

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 인증 토큰(Access Token, Refresh Token)을 안전하게 저장하고 관리하기 위한 인터페이스.
 * 이 인터페이스를 통해 토큰 저장 방식(SharedPreferences)을 추상화합니다.
 */
interface TokenStore {
    suspend fun save(access: String, refresh: String)
    suspend fun access(): String?
    suspend fun refresh(): String?
    suspend fun clear()
}

private const val PREF_NAME = "token_store_prefs"
private const val KEY_ACCESS = "access_token"
private const val KEY_REFRESH = "refresh_token"

class SharedPrefsTokenStore(private val prefs: SharedPreferences) : TokenStore {
    override suspend fun save(access: String, refresh: String) {
        prefs.edit().putString(KEY_ACCESS, access).putString(KEY_REFRESH, refresh).apply()
    }
    override suspend fun access(): String? = prefs.getString(KEY_ACCESS, null)
    override suspend fun refresh(): String? = prefs.getString(KEY_REFRESH, null)
    override suspend fun clear() { prefs.edit().clear().apply() }
}

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideTokenStore(
        @ApplicationContext context: Context
    ): TokenStore {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return SharedPrefsTokenStore(prefs)
    }
}