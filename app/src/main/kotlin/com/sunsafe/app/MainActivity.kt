package com.sunsafe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.*
import com.sunsafe.app.notification.SunSafeNotificationManager
import com.sunsafe.app.presentation.navigation.Screen
import com.sunsafe.app.presentation.navigation.SunSafeNavGraph
import com.sunsafe.app.presentation.theme.SunSafeTheme
import com.sunsafe.app.presentation.viewmodel.MainViewModel
import com.sunsafe.app.worker.ExposureTrackingWorker
import com.sunsafe.app.worker.UvDataRefreshWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var notificationManager: SunSafeNotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Keep splash visible until onboarding status is resolved
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isOnboardingCompleted.value == null
        }

        // Create notification channels
        notificationManager.createNotificationChannels()

        // Enqueue background workers
        scheduleWorkers()

        setContent {
            // Observe dark mode preference
            val settings by mainViewModel.isOnboardingCompleted.collectAsStateWithLifecycle()

            SunSafeTheme {
                Surface(Modifier.fillMaxSize()) {
                    when (settings) {
                        null -> {
                            // Still loading — splash handles this
                        }
                        else -> {
                            val start = if (settings == true) Screen.Dashboard.route
                            else Screen.Onboarding.route

                            SunSafeNavGraph(startDestination = start)
                        }
                    }
                }
            }
        }
    }

    private fun scheduleWorkers() {
        val workManager = WorkManager.getInstance(this)

        // Exposure tracking every 15 minutes
        workManager.enqueueUniquePeriodicWork(
            ExposureTrackingWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            ExposureTrackingWorker.buildRequest()
        )

        // UV data refresh daily
        workManager.enqueueUniquePeriodicWork(
            UvDataRefreshWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            UvDataRefreshWorker.buildRequest()
        )
    }
}
