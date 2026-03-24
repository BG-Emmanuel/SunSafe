package com.sunsafe.app.presentation.screens.history

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.sunsafe.app.domain.model.DailySummary
import com.sunsafe.app.presentation.theme.SunSafeColors
import com.sunsafe.app.presentation.theme.exposureStatusColor
import com.sunsafe.app.presentation.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onNavigateToReport: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exposure History") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null) } },
                actions = { IconButton(onClick = onNavigateToReport) { Icon(Icons.Filled.PictureAsPdf, "PDF") } }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatChip("30 Days", "${state.totalSessions} sessions", Modifier.weight(1f))
                    StatChip("Total", "${state.totalExposureMinutes.toInt()}m", Modifier.weight(1f))
                    StatChip("Burns", "${state.burnIncidents}", Modifier.weight(1f))
                }
            }
            if (state.dailySummaries.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("☀️", style = MaterialTheme.typography.displayMedium)
                            Text("No exposure data yet", style = MaterialTheme.typography.bodyLarge)
                            Text("Go outside — SunSafe will track your exposure.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                items(state.dailySummaries) { summary ->
                    DailySummaryCard(summary, state.selectedDate == summary.date) {
                        if (state.selectedDate == summary.date) viewModel.clearSelection()
                        else viewModel.selectDate(summary.date)
                    }
                    if (state.selectedDate == summary.date) {
                        state.selectedDaySessions.forEach { session ->
                            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
                            Card(
                                Modifier.fillMaxWidth().padding(start = 16.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Column {
                                        Text("${sdf.format(Date(session.startTime))} – ${session.endTime?.let { sdf.format(Date(it)) } ?: "ongoing"}",
                                            style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                                        Text("${session.durationMinutes.toInt()}min • UV ${session.uvIndex}${if (session.sunscreenApplied) " • SPF ${session.sunscreenSPF}" else ""}",
                                            style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    if (!session.burned) {
                                        TextButton(onClick = { viewModel.markAsBurned(session.id) }) { Text("Mark Burn", style = MaterialTheme.typography.labelSmall) }
                                    } else {
                                        Text("🔥 Burn", style = MaterialTheme.typography.labelSmall, color = SunSafeColors.DangerRed)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(12.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DailySummaryCard(summary: DailySummary, isSelected: Boolean, onClick: () -> Unit) {
    val sdf = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    val color = exposureStatusColor(summary.exposurePercentage)
    Card(
        onClick = onClick, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface),
        border = if (isSelected) BorderStroke(2.dp, color) else null
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(sdf.format(Date(summary.date)), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("UV ${summary.uvIndexPeak.toInt()} peak • ${summary.sessionsCount} session(s)",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("${summary.totalExposureMinutes.toInt()}m", style = MaterialTheme.typography.titleMedium, color = color, fontWeight = FontWeight.Bold)
                if (summary.burned) Text("🔥 Burn", style = MaterialTheme.typography.labelSmall, color = SunSafeColors.DangerRed)
                LinearProgressIndicator(
                    progress = { (summary.exposurePercentage / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier.width(80.dp).height(4.dp), color = color,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}
