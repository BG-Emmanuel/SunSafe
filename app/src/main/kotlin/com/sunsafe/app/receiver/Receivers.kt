package com.sunsafe.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.*
import com.sunsafe.app.notification.SunSafeNotificationManager
import com.sunsafe.app.worker.ExposureTrackingWorker
import com.sunsafe.app.worker.ReminderWorker
import com.sunsafe.app.worker.UvDataRefreshWorker
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val wm = WorkManager.getInstance(context)
        wm.enqueueUniquePeriodicWork(ExposureTrackingWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, ExposureTrackingWorker.buildRequest())
        wm.enqueueUniquePeriodicWork(UvDataRefreshWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, UvDataRefreshWorker.buildRequest())
    }
}

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            SunSafeNotificationManager.ACTION_APPLIED_SUNSCREEN -> {
                val spf = intent.getIntExtra(SunSafeNotificationManager.EXTRA_SPF, 30)
                WorkManager.getInstance(context).enqueue(
                    OneTimeWorkRequestBuilder<ReminderWorker>().setInputData(workDataOf("spf" to spf)).build()
                )
            }
            SunSafeNotificationManager.ACTION_SNOOZE -> {
                WorkManager.getInstance(context).enqueue(
                    OneTimeWorkRequestBuilder<ReminderWorker>()
                        .setInputData(workDataOf("is_reapplication" to true))
                        .setInitialDelay(30, TimeUnit.MINUTES)
                        .build()
                )
            }
        }
    }
}
