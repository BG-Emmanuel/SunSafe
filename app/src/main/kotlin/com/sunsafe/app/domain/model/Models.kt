package com.sunsafe.app.domain.model

// ─────────────────────────────────────────────────────────────────────────────
// Fitzpatrick Skin Types
// ─────────────────────────────────────────────────────────────────────────────
enum class SkinType(
    val displayName: String,
    val description: String,
    val characteristics: String,
    val baseBurnMinutes: Int,
    val emoji: String
) {
    TYPE_I(
        "Type I",
        "Very Fair",
        "Always burns, never tans. Very pale skin, blonde or red hair, blue/green eyes.",
        10,
        "👱"
    ),
    TYPE_II(
        "Type II",
        "Fair",
        "Usually burns, sometimes tans. Fair skin, blonde or light brown hair.",
        20,
        "🧑"
    ),
    TYPE_III(
        "Type III",
        "Medium",
        "Sometimes burns, gradually tans. Light brown skin.",
        30,
        "🧑‍🦱"
    ),
    TYPE_IV(
        "Type IV",
        "Olive",
        "Rarely burns, always tans. Olive or light brown skin.",
        50,
        "🧑‍🦰"
    ),
    TYPE_V(
        "Type V",
        "Brown",
        "Very rarely burns, tans very easily. Dark brown skin.",
        80,
        "👩‍🦱"
    ),
    TYPE_VI(
        "Type VI",
        "Dark Brown/Black",
        "Never burns, deeply pigmented. Very dark brown or black skin.",
        120,
        "🧑🏿"
    );

    companion object {
        fun fromOrdinal(ordinal: Int): SkinType = entries.getOrElse(ordinal) { TYPE_III }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// User Profile
// ─────────────────────────────────────────────────────────────────────────────
data class UserProfile(
    val id: Long = 0,
    val skinType: SkinType = SkinType.TYPE_III,
    val defaultSPF: Int = 30,
    val vitaminDDeficient: Boolean = false,
    val age: Int = 30,
    val name: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

// ─────────────────────────────────────────────────────────────────────────────
// UV Data
// ─────────────────────────────────────────────────────────────────────────────
data class UvData(
    val uvIndex: Double,
    val uvMax: Double,
    val uvMaxTime: Long?,
    val ozone: Double?,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class UvForecastItem(
    val date: String,
    val uvMax: Double,
    val uvMaxTime: Long
)

// ─────────────────────────────────────────────────────────────────────────────
// Exposure Session
// ─────────────────────────────────────────────────────────────────────────────
data class ExposureSession(
    val id: Long = 0,
    val userId: Long,
    val startTime: Long,
    val endTime: Long? = null,
    val durationMinutes: Double = 0.0,
    val avgLux: Float = 0f,
    val maxLux: Float = 0f,
    val uvIndex: Double = 0.0,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationName: String = "Unknown",
    val sunscreenApplied: Boolean = false,
    val sunscreenSPF: Int = 0,
    val burned: Boolean = false,
    val notes: String = "",
    val synced: Boolean = false
)

// ─────────────────────────────────────────────────────────────────────────────
// Exposure Calculation Result
// ─────────────────────────────────────────────────────────────────────────────
data class SafeExposureResult(
    val safeMinutes: Double,
    val vitaminDMinutes: Double,
    val uvMultiplier: Double,
    val spfMultiplier: Double,
    val skinBaseBurnMinutes: Int,
    val recommendations: List<String>
)

// ─────────────────────────────────────────────────────────────────────────────
// Daily Summary
// ─────────────────────────────────────────────────────────────────────────────
data class DailySummary(
    val date: Long,
    val totalExposureMinutes: Double,
    val safeExposureMinutes: Double,
    val uvIndexPeak: Double,
    val sunscreenApplications: Int,
    val burned: Boolean,
    val sessionsCount: Int
) {
    val exposurePercentage: Float
        get() = if (safeExposureMinutes > 0)
            (totalExposureMinutes / safeExposureMinutes * 100).toFloat().coerceIn(0f, 100f)
        else 0f

    val statusLevel: ExposureStatus
        get() = when {
            totalExposureMinutes >= safeExposureMinutes -> ExposureStatus.EXCEEDED
            totalExposureMinutes >= safeExposureMinutes * 0.95 -> ExposureStatus.CRITICAL
            totalExposureMinutes >= safeExposureMinutes * 0.80 -> ExposureStatus.WARNING
            else -> ExposureStatus.SAFE
        }
}

enum class ExposureStatus { SAFE, WARNING, CRITICAL, EXCEEDED }

// ─────────────────────────────────────────────────────────────────────────────
// UV Severity Level
// ─────────────────────────────────────────────────────────────────────────────
enum class UvSeverity(val label: String, val range: ClosedRange<Double>) {
    LOW("Low", 0.0..2.9),
    MODERATE("Moderate", 3.0..5.9),
    HIGH("High", 6.0..7.9),
    VERY_HIGH("Very High", 8.0..10.9),
    EXTREME("Extreme", 11.0..Double.MAX_VALUE);

    companion object {
        fun fromIndex(uvIndex: Double): UvSeverity =
            entries.firstOrNull { uvIndex in it.range } ?: EXTREME
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sunscreen Status
// ─────────────────────────────────────────────────────────────────────────────
data class SunscreenStatus(
    val applied: Boolean,
    val spf: Int,
    val appliedAt: Long?,
    val minutesSinceApplication: Long?
) {
    val effectiveness: Double
        get() = when {
            !applied || appliedAt == null -> 1.0
            minutesSinceApplication != null && minutesSinceApplication <= 80 -> 1.0
            minutesSinceApplication != null && minutesSinceApplication <= 120 -> 0.7
            else -> 0.5
        }

    val needsReapplication: Boolean
        get() = applied && (minutesSinceApplication ?: 0) >= 120
}

// ─────────────────────────────────────────────────────────────────────────────
// Notification Types
// ─────────────────────────────────────────────────────────────────────────────
enum class ReminderType {
    MORNING_SUNSCREEN,
    REAPPLICATION,
    UV_WARNING_80,
    UV_WARNING_95,
    UV_WARNING_EXCEEDED,
    DAILY_FORECAST,
    VITAMIN_D_ACHIEVED,
    STREAK_MAINTAINED
}

data class ReminderSetting(
    val id: Long = 0,
    val userId: Long,
    val time: String,         // "HH:mm"
    val type: ReminderType,
    val enabled: Boolean = true
)

// ─────────────────────────────────────────────────────────────────────────────
// Skin Photo
// ─────────────────────────────────────────────────────────────────────────────
data class SkinPhoto(
    val id: Long = 0,
    val userId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val imagePath: String,
    val bodyPart: String,
    val note: String = ""
)

// ─────────────────────────────────────────────────────────────────────────────
// App Settings
// ─────────────────────────────────────────────────────────────────────────────
data class AppSettings(
    val onboardingCompleted: Boolean = false,
    val darkMode: Boolean = false,
    val notifications: NotificationSettings = NotificationSettings(),
    val sensorSettings: SensorSettings = SensorSettings(),
    val userId: Long = -1L
)

data class NotificationSettings(
    val warningsEnabled: Boolean = true,
    val remindersEnabled: Boolean = true,
    val forecastEnabled: Boolean = true,
    val achievementsEnabled: Boolean = true,
    val morningReminderTime: String = "08:00",
    val reapplicationIntervalMinutes: Int = 120
)

data class SensorSettings(
    val sunlightThresholdLux: Float = 10_000f,
    val batterySaverMode: Boolean = false,
    val samplingRateMs: Long = 1_000L
)
