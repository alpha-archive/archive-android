package com.example.archiveandroid.core.version

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.example.archiveandroid.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

enum class UpdateType {
    NONE,           // 업데이트 불필요
    OPTIONAL,       // 선택적 업데이트
    REQUIRED        // 필수 업데이트
}

data class VersionCheckResult(
    val updateType: UpdateType,
    val playStoreUrl: String,
    val currentVersion: Int,
    val latestVersion: Int,
    val minSupportedVersion: Int
)

@Singleton
class VersionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    
    init {
        val configSettings = remoteConfigSettings {
            // 개발 중에는 0으로 설정 (즉시 업데이트)
            // 프로덕션에서는 3600 (1시간) 권장
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        
        // 기본값 설정
        remoteConfig.setDefaultsAsync(
            mapOf(
                "latest_version_code" to 0L,
                "min_supported_version_code" to 0L,
                "play_store_url" to ""
            )
        )
    }
    
    suspend fun checkVersion(): VersionCheckResult {
        try {
            // Remote Config에서 최신 값 가져오기
            val fetchTask = remoteConfig.fetch(0)
            fetchTask.await()
            remoteConfig.activate().await()
        } catch (e: Exception) {
            // 네트워크 오류 등으로 실패하면 캐시된 값 사용
            e.printStackTrace()
        }
        
        val currentVersion = getCurrentVersionCode()
        val latestVersion = remoteConfig.getLong("latest_version_code").toInt()
        val minSupportedVersion = remoteConfig.getLong("min_supported_version_code").toInt()
        val playStoreUrl = remoteConfig.getString("play_store_url")
        
        val updateType = when {
            minSupportedVersion > 0 && currentVersion < minSupportedVersion -> UpdateType.REQUIRED
            latestVersion > 0 && currentVersion < latestVersion -> UpdateType.OPTIONAL
            else -> UpdateType.NONE
        }
        
        return VersionCheckResult(
            updateType = updateType,
            playStoreUrl = playStoreUrl,
            currentVersion = currentVersion,
            latestVersion = latestVersion,
            minSupportedVersion = minSupportedVersion
        )
    }
    
    private fun getCurrentVersionCode(): Int {
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
        } catch (e: Exception) {
            1 // 실패 시 기본값
        }
    }
}

