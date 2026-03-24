package com.sunsafe.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sunsafe.app.MainActivity
import com.sunsafe.app.R
import com.sunsafe.app.domain.model.ExposureStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SunSafeNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_WARNINGS = "channel_warnings"
        const val CHANNEL_REMINDERS = "channel_reminders"
        const val CHANNEL_FORECAST = "channel_forecast"
        const val CHANNEL_ACHIEVEMENTS = "channel_achievements"
        const val CHANNEL_TRACKING = "channel_tracking"

        const val NOTIF_ID_WARNING = 1001
        const val NOTIF_ID_REMINDER = 1002
        const val NOTIF_ID_FORECAST = 1003
        const val NOTIF_ID_ACHIEVEMENT = 1004
        const val NOTIF_ID_TRACKING = 1005

        const val ACTION_APPLIED_SUNSCREEN = "com.sunsafe.ACTION_APPLIED_SUNSCREEN"
        const val ACTION_SNOOZE = "com.sunsafe.ACTION_SNOOZE"
        const val EXTRA_SPF = "extra_spf"
    }

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channels = listOf(
                NotificationChannel(
                    CHANNEL_WARNINGS,
                    "UV Warnings",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Alerts when approaching or exceeding safe UV exposure limits"
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_REMINDERS,
                    "Sunscreen Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Reminders to apply or reapply sunscreen"
                },
                NotificationChannel(
                    CHANNEL_FORECAST,
                    "Daily UV Forecast",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Morning UV forecast notifications"
                },
                NotificationChannel(
                    CHANNEL_ACHIEVEMENTS,
                    "Achievements",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Vitamin D goals and streak notifications"
                },
                NotificationChannel(
                    CHANNEL_TRACKING,
                    "Exposure Tracking",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Foreground service notification for exposure tracking"
                }
            )
            notificationManager.createNotificationChannels(channels)
        }
    }

    fun showExposureWarning(status: ExposureStatus, remainingMinutes: Double) {
        val (title, message) = when (status) {
            ExposureStatus.WARNING -> Pair(
                "⚠️ Approaching Safe Limit",
                "You've used 80% of your safe sun exposure today. ${remainingMinutes.toInt()} minutes remaining."
            )
            ExposureStatus.CRITICAL -> Pair(
                "🚨 Almost at Limit",
                "Only ${remainingMinutes.toInt()} minutes of safe exposure remaining!"
            )
            ExposureStatus.EXCEEDED -> Pair(
                "🔴 Safe Limit Exceeded",
                "You've exceeded your safe UV exposure for today. Seek shade immediately."
            )
            else -> return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("screen", "dashboard")
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_WARNINGS)
            .setSmallIcon(R.drawable.ic_sun)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_WARNING, notification)
    }

    fun showSunscreenReminder(isReapplication: Boolean, defaultSpf: Int) {
        val title = if (isReapplication) "⏰ Time to Reapply Sunscreen" else "☀️ Apply Sunscreen"
        val message = if (isReapplication)
            "It's been 2 hours since your last application. Reapply SPF $defaultSpf now."
        else
            "Morning reminder: Apply SPF $defaultSpf sunscreen before going outside today."

        val appliedIntent = Intent(context, com.sunsafe.app.receiver.NotificationActionReceiver::class.java).apply {
            action = ACTION_APPLIED_SUNSCREEN
            putExtra(EXTRA_SPF, defaultSpf)
        }
        val appliedPending = PendingIntent.getBroadcast(
            context, 1, appliedIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(context, com.sunsafe.app.receiver.NotificationActionReceiver::class.java).apply {
            action = ACTION_SNOOZE
        }
        val snoozePending = PendingIntent.getBroadcast(
            context, 2, snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDERS)
            .setSmallIcon(R.drawable.ic_sunscreen)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_check, "Applied ✓", appliedPending)
            .addAction(R.drawable.ic_snooze, "Snooze 30m", snoozePending)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_REMINDER, notification)
    }

    fun showDailyForecast(uvMax: Double, uvMaxTime: String) {
        val severity = com.sunsafe.app.domain.model.UvSeverity.fromIndex(uvMax)
        val message = "Peak UV ${uvMax.toInt()} (${severity.label}) expected around $uvMaxTime. Plan your outdoor activities accordingly."

        val notification = NotificationCompat.Builder(context, CHANNEL_FORECAST)
            .setSmallIcon(R.drawable.ic_uv)
            .setContentTitle("☀️ Today's UV Forecast")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_FORECAST, notification)
    }

    fun showAchievement(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ACHIEVEMENTS)
            .setSmallIcon(R.drawable.ic_achievement)
            .setContentTitle("🏆 $title")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_ACHIEVEMENT, notification)
    }

    fun buildTrackingNotification(locationName: String, minutesElapsed: Double): android.app.Notification =
        NotificationCompat.Builder(context, CHANNEL_TRACKING)
            .setSmallIcon(R.drawable.ic_sun)
            .setContentTitle("☀️ Tracking Sun Exposure")
            .setContentText("$locationName • ${minutesElapsed.toInt()} min today")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
}
