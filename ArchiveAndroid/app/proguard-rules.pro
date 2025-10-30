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
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Gson: 모델은 필드명 유지
-keep class com.alpha.archive.**.model.** { *; }
-keep class com.alpha.archive.**.dto.** { *; }
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
-keepclassmembers class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
# Gson 직렬화/역직렬화를 위한 규칙
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Kakao SDK (대개 consumerRules 포함, 그래도 안전망)
-keep class com.kakao.** { *; }
-dontwarn com.kakao.**

# Compose(일반적으로 필요없지만 커스텀 리플렉션 시)
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# 네트워크 관련 추가 규칙
-keep class java.net.** { *; }
-keep class javax.net.** { *; }
-keep class javax.net.ssl.** { *; }
-dontwarn java.net.**
-dontwarn javax.net.**

# HTTP 로깅 인터셉터 (릴리즈에서도 필요할 수 있음)
-keep class okhttp3.logging.** { *; }
-dontwarn okhttp3.logging.**

# MultipartBody 관련 (이미지 업로드용)
-keep class okhttp3.MultipartBody { *; }
-keep class okhttp3.MultipartBody$* { *; }
-keep class okhttp3.RequestBody { *; }
-keep class okhttp3.MediaType { *; }

# 코루틴 관련 (네트워크 호출에서 사용)
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**