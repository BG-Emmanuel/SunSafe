package com.sunsafe.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.sunsafe.app.domain.model.*
import com.sunsafe.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object Keys {
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val USER_ID = longPreferencesKey("user_id")

        // Notification settings
        val NOTIF_WARNINGS = booleanPreferencesKey("notif_warnings")
        val NOTIF_REMINDERS = booleanPreferencesKey("notif_reminders")
        val NOTIF_FORECAST = booleanPreferencesKey("notif_forecast")
        val NOTIF_ACHIEVEMENTS = booleanPreferencesKey("notif_achievements")
        val MORNING_REMINDER_TIME = stringPreferencesKey("morning_reminder_time")
        val REAPPLICATION_INTERVAL = intPreferencesKey("reapplication_interval_min")

        // Sensor settings
        val SUNLIGHT_THRESHOLD = floatPreferencesKey("sunlight_threshold_lux")
        val BATTERY_SAVER = booleanPreferencesKey("battery_saver_mode")
        val SAMPLING_RATE = longPreferencesKey("sampling_rate_ms")

        // Sunscreen tracking
        val SUNSCREEN_APPLIED = booleanPreferencesKey("sunscreen_applied")
        val SUNSCREEN_SPF = intPreferencesKey("sunscreen_spf")
        val SUNSCREEN_APPLIED_AT = longPreferencesKey("sunscreen_applied_at")
    }

    override fun getAppSettings(): Flow<AppSettings> = dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { prefs ->
            AppSettings(
                onboardingCompleted = prefs[Keys.ONBOARDING_DONE] ?: false,
                darkMode = prefs[Keys.DARK_MODE] ?: false,
                notifications = NotificationSettings(
                    warningsEnabled = prefs[Keys.NOTIF_WARNINGS] ?: true,
                    remindersEnabled = prefs[Keys.NOTIF_REMINDERS] ?: true,
                    forecastEnabled = prefs[Keys.NOTIF_FORECAST] ?: true,
                    achievementsEnabled = prefs[Keys.NOTIF_ACHIEVEMENTS] ?: true,
                    morningReminderTime = prefs[Keys.MORNING_REMINDER_TIME] ?: "08:00",
                    reapplicationIntervalMinutes = prefs[Keys.REAPPLICATION_INTERVAL] ?: 120
                ),
                sensorSettings = SensorSettings(
                    sunlightThresholdLux = prefs[Keys.SUNLIGHT_THRESHOLD] ?: 10_000f,
                    batterySaverMode = prefs[Keys.BATTERY_SAVER] ?: false,
                    samplingRateMs = prefs[Keys.SAMPLING_RATE] ?: 1_000L
                ),
                userId = prefs[Keys.USER_ID] ?: -1L
            )
        }

    override suspend fun updateSettings(settings: AppSettings) {
        dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_DONE] = settings.onboardingCompleted
            prefs[Keys.DARK_MODE] = settings.darkMode
            prefs[Keys.USER_ID] = settings.userId
        }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.ONBOARDING_DONE] = completed }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.ONBOARDING_DONE] ?: false }

    override fun getSunscreenStatus(): Flow<SunscreenStatus> = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs ->
            val applied = prefs[Keys.SUNSCREEN_APPLIED] ?: false
            val spf = prefs[Keys.SUNSCREEN_SPF] ?: 0
            val appliedAt = prefs[Keys.SUNSCREEN_APPLIED_AT]
            val minutesSince = if (appliedAt != null)
                (System.currentTimeMillis() - appliedAt) / 60_000L
            else null

            SunscreenStatus(
                applied = applied,
                spf = spf,
                appliedAt = appliedAt,
                minutesSinceApplication = minutesSince
            )
        }

    override suspend fun applyingSunscreen(spf: Int) {
        dataStore.edit { prefs ->
            prefs[Keys.SUNSCREEN_APPLIED] = true
            prefs[Keys.SUNSCREEN_SPF] = spf
            prefs[Keys.SUNSCREEN_APPLIED_AT] = System.currentTimeMillis()
        }
    }

    override fun getNotificationSettings(): Flow<NotificationSettings> = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs ->
            NotificationSettings(
                warningsEnabled = prefs[Keys.NOTIF_WARNINGS] ?: true,
                remindersEnabled = prefs[Keys.NOTIF_REMINDERS] ?: true,
                forecastEnabled = prefs[Keys.NOTIF_FORECAST] ?: true,
                achievementsEnabled = prefs[Keys.NOTIF_ACHIEVEMENTS] ?: true,
                morningReminderTime = prefs[Keys.MORNING_REMINDER_TIME] ?: "08:00",
                reapplicationIntervalMinutes = prefs[Keys.REAPPLICATION_INTERVAL] ?: 120
            )
        }

    override suspend fun updateNotificationSettings(settings: NotificationSettings) {
        dataStore.edit { prefs ->
            prefs[Keys.NOTIF_WARNINGS] = settings.warningsEnabled
            prefs[Keys.NOTIF_REMINDERS] = settings.remindersEnabled
            prefs[Keys.NOTIF_FORECAST] = settings.forecastEnabled
            prefs[Keys.NOTIF_ACHIEVEMENTS] = settings.achievementsEnabled
            prefs[Keys.MORNING_REMINDER_TIME] = settings.morningReminderTime
            prefs[Keys.REAPPLICATION_INTERVAL] = settings.reapplicationIntervalMinutes
        }
    }

    override fun getSensorSettings(): Flow<SensorSettings> = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs ->
            SensorSettings(
                sunlightThresholdLux = prefs[Keys.SUNLIGHT_THRESHOLD] ?: 10_000f,
                batterySaverMode = prefs[Keys.BATTERY_SAVER] ?: false,
                samplingRateMs = prefs[Keys.SAMPLING_RATE] ?: 1_000L
            )
        }

    override suspend fun updateSensorSettings(settings: SensorSettings) {
        dataStore.edit { prefs ->
            prefs[Keys.SUNLIGHT_THRESHOLD] = settings.sunlightThresholdLux
            prefs[Keys.BATTERY_SAVER] = settings.batterySaverMode
            prefs[Keys.SAMPLING_RATE] = settings.samplingRateMs
        }
    }
}
