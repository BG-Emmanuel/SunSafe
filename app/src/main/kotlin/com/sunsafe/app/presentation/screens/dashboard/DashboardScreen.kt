package com.sunsafe.app.presentation.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sunsafe.app.domain.model.ExposureSession
import com.sunsafe.app.presentation.components.*
import com.sunsafe.app.presentation.theme.SunSafeColors
import com.sunsafe.app.presentation.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateToForecast: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showSunscreenDialog by remember { mutableStateOf(false) }
    var showManualLogDialog by remember { mutableStateOf(false) }

    if (showSunscreenDialog) ApplySunscreenDialog(
        defaultSpf = state.userProfile?.defaultSPF ?: 30,
        onDismiss = { showSunscreenDialog = false },
        onApply = { spf -> viewModel.applySunscreen(spf); showSunscreenDialog = false }
    )
    if (showManualLogDialog) LogManualExposureDialog(
        onDismiss = { showManualLogDialog = false },
        onLog = { d, uv, spf -> viewModel.logManualExposure(d, uv, spf); showManualLogDialog = false }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("SunSafe ☀️", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(state.locationName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) { Icon(Icons.Filled.Refresh, "Refresh") }
                    IconButton(onClick = onNavigateToSettings) { Icon(Icons.Filled.Settings, "Settings") }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, label = { Text("Dashboard") }, icon = { Icon(Icons.Filled.Home, null) })
                NavigationBarItem(selected = false, onClick = onNavigateToHistory, label = { Text("History") }, icon = { Icon(Icons.Filled.History, null) })
                NavigationBarItem(selected = false, onClick = onNavigateToForecast, label = { Text("Forecast") }, icon = { Icon(Icons.Filled.WbSunny, null) })
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error banner
            AnimatedVisibility(state.errorMessage != null) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), shape = RoundedCornerShape(12.dp)) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, null, tint = MaterialTheme.colorScheme.onErrorContainer)
                        Text(state.errorMessage ?: "", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }

            LuxIndicator(state.currentLux, state.isInSunlight, state.sensorAvailable)
            UvIndexCard(state.uvData, state.isLoading, Modifier.fillMaxWidth())

            // Circular progress card
            Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Today's Safe Exposure", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    CircularExposureIndicator(
                        percentage = state.exposurePercentage,
                        remainingMinutes = state.remainingMinutes,
                        safeMinutes = state.safeExposureMinutes,
                        status = state.exposureStatus,
                        size = 200.dp
                    )
                    if (state.vitaminDMinutes < Double.MAX_VALUE) {
                        val vdPct = (state.todayExposureMinutes / state.vitaminDMinutes).toFloat().coerceIn(0f, 1f)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("🌞 Vit D", style = MaterialTheme.typography.labelSmall)
                            LinearProgressIndicator(modifier = Modifier.weight(1f).height(6.dp), progress = { vdPct }, color = SunSafeColors.UvModerate, trackColor = MaterialTheme.colorScheme.surfaceVariant)
                            Text("${(vdPct * 100).toInt()}%", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Text("${state.todayExposureMinutes.toInt()} min exposed today", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            SunscreenStatusCard(state.sunscreenStatus, { showSunscreenDialog = true }, Modifier.fillMaxWidth())

            // Quick actions
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { showSunscreenDialog = true }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.WaterDrop, null, Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Apply SPF", style = MaterialTheme.typography.labelMedium)
                }
                OutlinedButton(onClick = { showManualLogDialog = true }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.Edit, null, Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Log", style = MaterialTheme.typography.labelMedium)
                }
                OutlinedButton(onClick = onNavigateToHistory, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.BarChart, null, Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("History", style = MaterialTheme.typography.labelMedium)
                }
            }

            // Timeline
            if (state.todaySessions.isNotEmpty()) {
                Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Today's Timeline", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        state.todaySessions.forEach { SessionTimelineItem(it) }
                    }
                }
            }

            RecommendationsCard(state.recommendations, Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SessionTimelineItem(session: ExposureSession) {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).background(SunSafeColors.SunOrange, CircleShape))
            Column {
                Text("${sdf.format(Date(session.startTime))}${if (session.endTime != null) " – ${sdf.format(Date(session.endTime))}" else " (ongoing)"}",
                    style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                Text("${session.durationMinutes.toInt()}min • UV ${session.uvIndex}${if (session.sunscreenApplied) " • SPF ${session.sunscreenSPF}" else ""}",
                    style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (session.burned) Surface(shape = RoundedCornerShape(50), color = SunSafeColors.DangerRed.copy(alpha = 0.15f)) {
            Text("Burn", Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = SunSafeColors.DangerRed)
        }
    }
}
