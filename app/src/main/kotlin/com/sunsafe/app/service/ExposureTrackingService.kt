package com.sunsafe.app.service

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import com.sunsafe.app.domain.model.ExposureSession
import com.sunsafe.app.domain.repository.*
import com.sunsafe.app.domain.usecase.CalculateRemainingExposureUseCase
import com.sunsafe.app.domain.usecase.CalculateSafeExposureUseCase
import com.sunsafe.app.notification.SunSafeNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ExposureTrackingService : Service(), SensorEventListener {

    @Inject lateinit var exposureRepository: ExposureRepository
    @Inject lateinit var settingsRepository: SettingsRepository
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var locationRepository: LocationRepository
    @Inject lateinit var uvRepository: UvRepository
    @Inject lateinit var calculateRemainingExposure: CalculateRemainingExposureUseCase
    @Inject lateinit var notificationManager: SunSafeNotificationManager

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null

    private var currentLux = 0f
    private var luxReadings = mutableListOf<Float>()
    private var activeSessionId: Long? = null
    private var sessionStartTime = 0L

    companion object {
        const val ACTION_START = "ACTION_START_TRACKING"
        const val ACTION_STOP = "ACTION_STOP_TRACKING"
        const val NOTIF_ID = SunSafeNotificationManager.NOTIF_ID_TRACKING
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTracking()
            ACTION_STOP -> stopTracking()
        }
        return START_STICKY
    }

    private fun startTracking() {
        val notification = notificationManager.buildTrackingNotification("Detecting location...", 0.0)
        startForeground(NOTIF_ID, notification)

        lightSensor?.let { sensor ->
            val sensorSettings = runBlocking { settingsRepository.getSensorSettings().first() }
            sensorManager.registerListener(
                this, sensor,
                sensorSettings.samplingRateMs.toInt() * 1000
            )
        } ?: Timber.w("No light sensor available")

        // Periodic check every 30 seconds
        serviceScope.launch {
            while (isActive) {
                delay(30_000L)
                checkAndUpdateExposure()
            }
        }
    }

    private suspend fun checkAndUpdateExposure() {
        try {
            val sensorSettings = settingsRepository.getSensorSettings().first()
            val isInSunlight = currentLux >= sensorSettings.sunlightThresholdLux

            if (isInSunlight && activeSessionId == null) {
                // Start new session
                val userProfile = userRepository.getUserProfile().first() ?: return
                val sunscreenStatus = settingsRepository.getSunscreenStatus().first()
                val location = locationRepository.getCurrentLocation().getOrNull()
                val uvData = if (location != null)
                    uvRepository.getCurrentUvIndex(location.first, location.second).getOrNull()
                else null

                val locationName = if (location != null)
                    locationRepository.getLocationName(location.first, location.second)
                else "Unknown"

                sessionStartTime = System.currentTimeMillis()
                luxReadings.clear()

                val session = ExposureSession(
                    userId = userProfile.id,
                    startTime = sessionStartTime,
                    avgLux = currentLux,
                    maxLux = currentLux,
                    uvIndex = uvData?.uvIndex ?: 0.0,
                    latitude = location?.first,
                    longitude = location?.second,
                    locationName = locationName,
                    sunscreenApplied = sunscreenStatus.applied,
                    sunscreenSPF = if (sunscreenStatus.applied) sunscreenStatus.spf else 0
                )
                activeSessionId = exposureRepository.startSession(session)
                Timber.d("Service: Started exposure session $activeSessionId")

            } else if (!isInSunlight && activeSessionId != null) {
                // End current session
                val id = activeSessionId ?: return
                val now = System.currentTimeMillis()
                val durationMinutes = (now - sessionStartTime) / 60_000.0
                val avgLux = if (luxReadings.isNotEmpty()) luxReadings.average().toFloat() else 0f
                val maxLux = luxReadings.maxOrNull() ?: 0f

                exposureRepository.endSession(id, now, avgLux, maxLux, durationMinutes)
                Timber.d("Service: Ended session $id after ${durationMinutes.toInt()} minutes")
                activeSessionId = null

                // Update notification
                val notif = notificationManager.buildTrackingNotification("Indoors", durationMinutes)
                startForeground(NOTIF_ID, notif)
            }

            // Update notification with current totals
            if (activeSessionId != null) {
                val elapsed = (System.currentTimeMillis() - sessionStartTime) / 60_000.0
                val notif = notificationManager.buildTrackingNotification(
                    "Tracking exposure...", elapsed
                )
                startForeground(NOTIF_ID, notif)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error updating exposure")
        }
    }

    private fun stopTracking() {
        sensorManager.unregisterListener(this)
        serviceScope.launch {
            activeSessionId?.let { id ->
                val now = System.currentTimeMillis()
                val durationMinutes = (now - sessionStartTime) / 60_000.0
                val avgLux = if (luxReadings.isNotEmpty()) luxReadings.average().toFloat() else 0f
                val maxLux = luxReadings.maxOrNull() ?: 0f
                exposureRepository.endSession(id, now, avgLux, maxLux, durationMinutes)
                activeSessionId = null
            }
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                currentLux = it.values[0]
                if (activeSessionId != null) {
                    luxReadings.add(currentLux)
                    // Keep only last 100 readings to save memory
                    if (luxReadings.size > 100) luxReadings.removeAt(0)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        serviceScope.cancel()
    }
}
