# Kotlin/Coroutines 기본 keep
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Hilt/Dagger (대부분 consumerRules로 해결되지만 안전망)
-keep class dagger.hilt.** { *; }
-keep class dagger.hilt.android.internal.managers.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponentManager { *; }
-dontwarn dagger.hilt.**

# Retrofit + OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson: 모델은 필드명 유지
-keep class com.alpha.archiveandroid.**.model.** { *; }
-keep class com.alpha.archiveandroid.**.dto.** { *; }
-keepclassmembers class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Kakao SDK (대개 consumerRules 포함, 그래도 안전망)
-keep class com.kakao.** { *; }
-dontwarn com.kakao.**

# Compose(일반적으로 필요없지만 커스텀 리플렉션 시)
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**