package com.sunsafe.app.worker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.sunsafe.app.domain.model.*
import com.sunsafe.app.domain.repository.*
import com.sunsafe.app.domain.usecase.CalculateRemainingExposureUseCase
import com.sunsafe.app.domain.usecase.CalculateSafeExposureUseCase
import com.sunsafe.app.notification.SunSafeNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

// ─────────────────────────────────────────────────────────────────────────────
// Exposure Tracking Worker — runs every 15 minutes
// ─────────────────────────────────────────────────────────────────────────────
@HiltWorker
class ExposureTrackingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val exposureRepository: ExposureRepository,
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository,
    private val uvRepository: UvRepository,
    private val calculateRemainingExposure: CalculateRemainingExposureUseCase,
    private val notificationManager: SunSafeNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val sensorSettings = settingsRepository.getSensorSettings().first()
            val luxReading = readLightSensor(sensorSettings.sunlightThresholdLux)
            val isInSunlight = (luxReading ?: 0f) >= sensorSettings.sunlightThresholdLux

            if (!isInSunlight) {
                // Close any active session if user is now indoors
                val activeSession = exposureRepository.getActiveSession()
                if (activeSession != null) {
                    val now = System.currentTimeMillis()
                    val duration = (now - activeSession.startTime) / 60_000.0
                    exposureRepository.endSession(
                        activeSession.id, now,
                        luxReading ?: 0f, activeSession.maxLux, duration
                    )
                    Timber.d("Closed exposure session: ${duration.toInt()} minutes")
                }
                return@withContext Result.success()
            }

            // User is outdoors — check/create session
            val userProfile = userRepository.getUserProfile().first() ?: return@withContext Result.success()
            val sunscreenStatus = settingsRepository.getSunscreenStatus().first()

            // Get location and UV
            val location = locationRepository.getCurrentLocation().getOrNull()
            val uvData = if (location != null) {
                uvRepository.getCurrentUvIndex(location.first, location.second).getOrNull()
            } else null

            val activeSession = exposureRepository.getActiveSession()
            if (activeSession == null) {
                // Start new session
                val locationName = if (location != null)
                    locationRepository.getLocationName(location.first, location.second)
                else "Unknown"

                val newSession = ExposureSession(
                    userId = userProfile.id,
                    startTime = System.currentTimeMillis(),
                    avgLux = luxReading ?: 0f,
                    maxLux = luxReading ?: 0f,
                    uvIndex = uvData?.uvIndex ?: 0.0,
                    latitude = location?.first,
                    longitude = location?.second,
                    locationName = locationName,
                    sunscreenApplied = sunscreenStatus.applied,
                    sunscreenSPF = if (sunscreenStatus.applied) sunscreenStatus.spf else 0
                )
                exposureRepository.startSession(newSession)
                Timber.d("Started new exposure session")
            }

            // Check if approaching limits and notify
            val todayStart = getTodayStart()
            val todayTotal = exposureRepository
                .getSessionsInRange(todayStart, System.currentTimeMillis())
                .first()
                .sumOf { it.durationMinutes }

            val remainingResult = calculateRemainingExposure(
                CalculateRemainingExposureUseCase.Input(
                    skinType = userProfile.skinType,
                    uvIndex = uvData?.uvIndex ?: 3.0,
                    sunscreenStatus = sunscreenStatus,
                    elapsedMinutesToday = todayTotal,
                    vitaminDDeficient = userProfile.vitaminDDeficient
                )
            )

            // Send warnings based on status
            when (remainingResult.status) {
                ExposureStatus.WARNING, ExposureStatus.CRITICAL, ExposureStatus.EXCEEDED -> {
                    notificationManager.showExposureWarning(
                        remainingResult.status,
                        remainingResult.remainingMinutes
                    )
                }
                else -> {}
            }

            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "ExposureTrackingWorker failed")
            Result.retry()
        }
    }

    private suspend fun readLightSensor(threshold: Float): Float? {
        val sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) ?: return null

        return try {
            withTimeout(3000L) {
                suspendCancellableCoroutine { cont ->
                    val listener = object : SensorEventListener {
                        override fun onSensorChanged(event: SensorEvent?) {
                            val lux = event?.values?.firstOrNull()
                            sensorManager.unregisterListener(this)
                            cont.resume(lux)
                        }
                        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
                    }
                    sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
                    cont.invokeOnCancellation { sensorManager.unregisterListener(listener) }
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getTodayStart(): Long {
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    companion object {
        const val WORK_NAME = "exposure_tracking"

        fun buildRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ExposureTrackingWorker>(15, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// UV Data Refresh Worker — runs daily
// ─────────────────────────────────────────────────────────────────────────────
@HiltWorker
class UvDataRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val uvRepository: UvRepository,
    private val locationRepository: LocationRepository,
    private val settingsRepository: SettingsRepository,
    private val notificationManager: SunSafeNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val location = locationRepository.getCurrentLocation().getOrNull()
                ?: return@withContext Result.retry()

            val forecast = uvRepository.getUvForecast(location.first, location.second).getOrNull()

            val notifSettings = settingsRepository.getNotificationSettings().first()
            if (notifSettings.forecastEnabled && !forecast.isNullOrEmpty()) {
                val todayForecast = forecast.firstOrNull()
                if (todayForecast != null) {
                    val timeStr = java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault())
                        .format(java.util.Date(todayForecast.uvMaxTime))
                    notificationManager.showDailyForecast(todayForecast.uvMax, timeStr)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "UvDataRefreshWorker failed")
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "uv_data_refresh"

        fun buildRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<UvDataRefreshWorker>(1, TimeUnit.DAYS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .setInitialDelay(calculateDelayToMorning(), TimeUnit.MILLISECONDS)
                .build()

        private fun calculateDelayToMorning(): Long {
            val now = java.util.Calendar.getInstance()
            val target = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, 7)
                set(java.util.Calendar.MINUTE, 30)
                set(java.util.Calendar.SECOND, 0)
                if (before(now)) add(java.util.Calendar.DAY_OF_YEAR, 1)
            }
            return target.timeInMillis - now.timeInMillis
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sunscreen Reminder Worker
// ─────────────────────────────────────────────────────────────────────────────
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
    private val notificationManager: SunSafeNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val isReapplication = inputData.getBoolean("is_reapplication", false)
            val userProfile = userRepository.getUserProfile().first()
            val spf = userProfile?.defaultSPF ?: 30

            val notifSettings = settingsRepository.getNotificationSettings().first()
            if (notifSettings.remindersEnabled) {
                notificationManager.showSunscreenReminder(isReapplication, spf)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME_MORNING = "sunscreen_reminder_morning"
        const val WORK_NAME_REAPPLICATION = "sunscreen_reapplication"
    }
}
