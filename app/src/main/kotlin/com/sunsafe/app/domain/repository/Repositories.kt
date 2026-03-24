package com.sunsafe.app.domain.repository

import com.sunsafe.app.domain.model.*
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────────────────────
// User Repository
// ─────────────────────────────────────────────────────────────────────────────
interface UserRepository {
    fun getUserProfile(): Flow<UserProfile?>
    suspend fun saveUserProfile(profile: UserProfile): Long
    suspend fun updateUserProfile(profile: UserProfile)
    suspend fun getUserById(id: Long): UserProfile?
}

// ─────────────────────────────────────────────────────────────────────────────
// UV Repository
// ─────────────────────────────────────────────────────────────────────────────
interface UvRepository {
    suspend fun getCurrentUvIndex(lat: Double, lon: Double): Result<UvData>
    suspend fun getUvForecast(lat: Double, lon: Double): Result<List<UvForecastItem>>
    fun getCachedUvData(): Flow<UvData?>
}

// ─────────────────────────────────────────────────────────────────────────────
// Exposure Repository
// ─────────────────────────────────────────────────────────────────────────────
interface ExposureRepository {
    suspend fun startSession(session: ExposureSession): Long
    suspend fun endSession(sessionId: Long, endTime: Long, avgLux: Float, maxLux: Float, durationMinutes: Double)
    suspend fun updateSession(session: ExposureSession)
    suspend fun getActiveSession(): ExposureSession?
    fun getAllSessions(): Flow<List<ExposureSession>>
    fun getSessionsInRange(startTime: Long, endTime: Long): Flow<List<ExposureSession>>
    fun getDailySummaries(days: Int): Flow<List<DailySummary>>
    suspend fun logManualExposure(session: ExposureSession): Long
    suspend fun markAsBurned(sessionId: Long)
    suspend fun deleteSession(sessionId: Long)
    suspend fun exportData(): List<ExposureSession>
}

// ─────────────────────────────────────────────────────────────────────────────
// Settings Repository
// ─────────────────────────────────────────────────────────────────────────────
interface SettingsRepository {
    fun getAppSettings(): Flow<AppSettings>
    suspend fun updateSettings(settings: AppSettings)
    suspend fun setOnboardingCompleted(completed: Boolean)
    fun isOnboardingCompleted(): Flow<Boolean>
    fun getSunscreenStatus(): Flow<SunscreenStatus>
    suspend fun applyingSunscreen(spf: Int)
    fun getNotificationSettings(): Flow<NotificationSettings>
    suspend fun updateNotificationSettings(settings: NotificationSettings)
    fun getSensorSettings(): Flow<SensorSettings>
    suspend fun updateSensorSettings(settings: SensorSettings)
}

// ─────────────────────────────────────────────────────────────────────────────
// Location Repository
// ─────────────────────────────────────────────────────────────────────────────
interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Pair<Double, Double>>
    suspend fun getLocationName(lat: Double, lon: Double): String
}

// ─────────────────────────────────────────────────────────────────────────────
// Photo Repository
// ─────────────────────────────────────────────────────────────────────────────
interface PhotoRepository {
    fun getAllPhotos(userId: Long): Flow<List<SkinPhoto>>
    suspend fun savePhoto(photo: SkinPhoto): Long
    suspend fun deletePhoto(photoId: Long)
}
