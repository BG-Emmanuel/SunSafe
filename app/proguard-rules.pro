# SunSafe ProGuard Rules

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }

# Moshi
-keep class com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonClass class * { *; }
-keepclassmembers class * {
    @com.squareup.moshi.FromJson *;
    @com.squareup.moshi.ToJson *;
}

# Retrofit
-keep class retrofit2.** { *; }
-keepattributes Signature, Exceptions

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# iText PDF
-keep class com.itextpdf.** { *; }
-dontwarn com.itextpdf.**

# App models
-keep class com.sunsafe.app.domain.model.** { *; }
-keep class com.sunsafe.app.data.local.entity.** { *; }
-keep class com.sunsafe.app.data.remote.api.** { *; }

# WorkManager
-keep class * extends androidx.work.Worker { *; }
-keep class * extends androidx.work.CoroutineWorker { *; }

# Timber
-dontwarn org.jetbrains.annotations.**
