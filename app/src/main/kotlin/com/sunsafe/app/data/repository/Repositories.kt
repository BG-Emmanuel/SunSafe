package com.sunsafe.app.data.repository

import com.sunsafe.app.BuildConfig
import com.sunsafe.app.data.local.dao.*
import com.sunsafe.app.data.local.entity.*
import com.sunsafe.app.data.remote.api.*
import com.sunsafe.app.domain.model.*
import com.sunsafe.app.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────────────────────────
// User Repository Implementation
// ─────────────────────────────────────────────────────────────────────────────
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getUserProfile(): Flow<UserProfile?> =
        userDao.getUserProfile().map { it?.toDomain() }

    override suspend fun saveUserProfile(profile: UserProfile): Long =
        userDao.insertUser(UserEntity.fromDomain(profile))

    override suspend fun updateUserProfile(profile: UserProfile) =
        userDao.updateUser(UserEntity.fromDomain(profile))

    override suspend fun getUserById(id: Long): UserProfile? =
        userDao.getUserById(id)?.toDomain()
}

// ─────────────────────────────────────────────────────────────────────────────
// UV Repository Implementation
// ─────────────────────────────────────────────────────────────────────────────
@Singleton
class UvRepositoryImpl @Inject constructor(
    private val openUvApi: OpenUvApi,
    private val owmApi: OpenWeatherApi,
    private val settingsRepository: SettingsRepository
) : UvRepository {

    private var cachedUvData: UvData? = null
    private var cacheTimestamp: Long = 0L
    private val cacheDurationMs = 30 * 60 * 1000L // 30 minutes

    override suspend fun getCurrentUvIndex(lat: Double, lon: Double): Result<UvData> {
        val now = System.currentTimeMillis()
        if (cachedUvData != null && (now - cacheTimestamp) < cacheDurationMs) {
            return Result.success(cachedUvData!!)
        }

        return try {
            val response = openUvApi.getCurrentUv(
                apiKey = BuildConfig.OPENUV_API_KEY,
                lat = lat,
                lng = lon
            )
            if (response.isSuccessful && response.body() != null) {
                val result = response.body()!!.result
                val uvData = UvData(
                    uvIndex = result.uv,
                    uvMax = result.uvMax,
                    uvMaxTime = parseIsoTime(result.uvMaxTime),
                    ozone = result.ozone,
                    latitude = lat,
                    longitude = lon
                )
                cachedUvData = uvData
                cacheTimestamp = now
                Result.success(uvData)
            } else {
                // Fallback to OWM
                fetchOwmUvIndex(lat, lon)
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch UV from OpenUV API, trying OWM fallback")
            fetchOwmUvIndex(lat, lon)
        }
    }

    private suspend fun fetchOwmUvIndex(lat: Double, lon: Double): Result<UvData> {
        return try {
            val response = owmApi.getUvIndex(
                apiKey = BuildConfig.OWM_API_KEY,
                lat = lat,
                lon = lon
            )
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val uvData = UvData(
                    uvIndex = body.value,
                    uvMax = body.value,
                    uvMaxTime = body.date * 1000L,
                    ozone = null,
                    latitude = lat,
                    longitude = lon
                )
                cachedUvData = uvData
                cacheTimestamp = System.currentTimeMillis()
                Result.success(uvData)
            } else {
                // Last resort: estimate from time of day
                Result.success(estimateFallbackUv(lat))
            }
        } catch (e: Exception) {
            Timber.e(e, "OWM UV also failed, using estimation")
            Result.success(estimateFallbackUv(lat))
        }
    }

    private fun estimateFallbackUv(lat: Double): UvData {
        val cal = Calendar.getInstance()
        val hourOfDay = cal.get(Calendar.HOUR_OF_DAY)
        val month = cal.get(Calendar.MONTH)
        // Simple estimation based on time of day and hemisphere
        val peakHour = 12
        val hourDiff = Math.abs(hourOfDay - peakHour)
        val baseUv = if (lat > 0) {
            // Northern hemisphere: higher UV in summer (Apr-Sep)
            if (month in 3..8) 6.0 else 3.0
        } else {
            // Southern hemisphere: higher UV in winter (Oct-Mar)
            if (month in 9..11 || month in 0..2) 6.0 else 3.0
        }
        val estimatedUv = (baseUv * (1.0 - hourDiff / 8.0)).coerceAtLeast(0.0)
        return UvData(
            uvIndex = estimatedUv,
            uvMax = baseUv,
            uvMaxTime = null,
            ozone = null,
            latitude = lat,
            longitude = 0.0
        )
    }

    override suspend fun getUvForecast(lat: Double, lon: Double): Result<List<UvForecastItem>> {
        return try {
            val response = openUvApi.getForecast(
                apiKey = BuildConfig.OPENUV_API_KEY,
                lat = lat,
                lng = lon
            )
            if (response.isSuccessful && response.body() != null) {
                val forecast = response.body()!!.result.map { item ->
                    UvForecastItem(
                        date = item.uvTime.take(10),
                        uvMax = item.uvMax,
                        uvMaxTime = parseIsoTime(item.uvMaxTime) ?: System.currentTimeMillis()
                    )
                }
                Result.success(forecast)
            } else {
                fetchOwmForecast(lat, lon)
            }
        } catch (e: Exception) {
            fetchOwmForecast(lat, lon)
        }
    }

    private suspend fun fetchOwmForecast(lat: Double, lon: Double): Result<List<UvForecastItem>> {
        return try {
            val response = owmApi.getUvForecast(
                apiKey = BuildConfig.OWM_API_KEY,
                lat = lat,
                lon = lon
            )
            if (response.isSuccessful && response.body() != null) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val items = response.body()!!.map { item ->
                    UvForecastItem(
                        date = sdf.format(Date(item.date * 1000L)),
                        uvMax = item.value,
                        uvMaxTime = item.date * 1000L
                    )
                }
                Result.success(items)
            } else {
                Result.failure(Exception("UV forecast unavailable"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCachedUvData(): Flow<UvData?> {
        // Simple flow that emits the in-memory cached value
        // In a full implementation you'd persist this to DataStore
        return kotlinx.coroutines.flow.flow { emit(cachedUvData) }
    }

    private fun parseIsoTime(isoString: String?): Long? {
        if (isoString == null) return null
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(isoString)?.time
        } catch (e: Exception) {
            null
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Exposure Repository Implementation
// ─────────────────────────────────────────────────────────────────────────────
@Singleton
class ExposureRepositoryImpl @Inject constructor(
    private val exposureDao: ExposureDao
) : ExposureRepository {

    override suspend fun startSession(session: ExposureSession): Long =
        exposureDao.insertSession(ExposureEntity.fromDomain(session))

    override suspend fun endSession(
        sessionId: Long, endTime: Long, avgLux: Float, maxLux: Float, durationMinutes: Double
    ) = exposureDao.closeSession(sessionId, endTime, avgLux, maxLux, durationMinutes)

    override suspend fun updateSession(session: ExposureSession) =
        exposureDao.updateSession(ExposureEntity.fromDomain(session))

    override suspend fun getActiveSession(): ExposureSession? =
        exposureDao.getActiveSession()?.toDomain()

    override fun getAllSessions(): Flow<List<ExposureSession>> =
        exposureDao.getAllSessions().map { list -> list.map { it.toDomain() } }

    override fun getSessionsInRange(startTime: Long, endTime: Long): Flow<List<ExposureSession>> =
        exposureDao.getSessionsInRange(startTime, endTime).map { list -> list.map { it.toDomain() } }

    override fun getDailySummaries(days: Int): Flow<List<DailySummary>> {
        val since = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)
        return exposureDao.getDailyAggregates(since).map { rows ->
            rows.map { row ->
                DailySummary(
                    date = row.date,
                    totalExposureMinutes = row.totalMinutes,
                    safeExposureMinutes = 60.0, // placeholder; ViewModel fills this in
                    uvIndexPeak = row.peakUv,
                    sunscreenApplications = row.sunscreenCount,
                    burned = row.wasBurned,
                    sessionsCount = row.sessionsCount
                )
            }
        }
    }

    override suspend fun logManualExposure(session: ExposureSession): Long =
        exposureDao.insertSession(ExposureEntity.fromDomain(session))

    override suspend fun markAsBurned(sessionId: Long) =
        exposureDao.markAsBurned(sessionId)

    override suspend fun deleteSession(sessionId: Long) =
        exposureDao.deleteSession(sessionId)

    override suspend fun exportData(): List<ExposureSession> {
        var result = emptyList<ExposureSession>()
        exposureDao.getAllSessions().collect { result = it.map { e -> e.toDomain() } }
        return result
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Photo Repository Implementation
// ─────────────────────────────────────────────────────────────────────────────
@Singleton
class PhotoRepositoryImpl @Inject constructor(
    private val photoDao: SkinPhotoDao
) : PhotoRepository {
    override fun getAllPhotos(userId: Long): Flow<List<SkinPhoto>> =
        photoDao.getPhotosForUser(userId).map { it.map { p -> p.toDomain() } }

    override suspend fun savePhoto(photo: SkinPhoto): Long =
        photoDao.insertPhoto(SkinPhotoEntity.fromDomain(photo))

    override suspend fun deletePhoto(photoId: Long) =
        photoDao.deletePhoto(photoId)
}
