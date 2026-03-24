package com.sunsafe.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sunsafe.app.presentation.screens.dashboard.DashboardScreen
import com.sunsafe.app.presentation.screens.history.HistoryScreen
import com.sunsafe.app.presentation.screens.onboarding.OnboardingScreen
import com.sunsafe.app.presentation.screens.report.ReportScreen
import com.sunsafe.app.presentation.screens.settings.SettingsScreen
import com.sunsafe.app.presentation.screens.forecast.ForecastScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Dashboard  : Screen("dashboard")
    object History    : Screen("history")
    object Forecast   : Screen("forecast")
    object Report     : Screen("report")
    object Settings   : Screen("settings")
}

@Composable
fun SunSafeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToHistory  = { navController.navigate(Screen.History.route) },
                onNavigateToForecast = { navController.navigate(Screen.Forecast.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onBack             = { navController.popBackStack() },
                onNavigateToReport = { navController.navigate(Screen.Report.route) }
            )
        }

        composable(Screen.Forecast.route) {
            ForecastScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Report.route) {
            ReportScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
