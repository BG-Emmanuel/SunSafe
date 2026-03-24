package com.sunsafe.app.presentation.screens.forecast

import androidx.compose.foundation.background
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
import com.sunsafe.app.domain.model.UvForecastItem
import com.sunsafe.app.domain.model.UvSeverity
import com.sunsafe.app.presentation.components.UvIndexCard
import com.sunsafe.app.presentation.theme.SunSafeColors
import com.sunsafe.app.presentation.theme.uvIndexColor
import com.sunsafe.app.presentation.viewmodel.ForecastViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    onBack: () -> Unit,
    viewModel: ForecastViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UV Forecast") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null) } },
                actions = { IconButton(onClick = { viewModel.refresh() }) { Icon(Icons.Filled.Refresh, null) } }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                if (state.locationName.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.LocationOn, null, tint = SunSafeColors.SunOrange, modifier = Modifier.size(16.dp))
                        Text(state.locationName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            item { UvIndexCard(state.currentUv, false, Modifier.fillMaxWidth()) }
            item {
                Text("5-Day Forecast", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            if (state.forecast.isEmpty()) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("Forecast unavailable. Check your internet connection.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                items(state.forecast) { item -> ForecastDayCard(item) }
            }
            item { UvScaleLegend() }
        }
    }
}

@Composable
private fun ForecastDayCard(item: UvForecastItem) {
    val color = uvIndexColor(item.uvMax)
    val severity = UvSeverity.fromIndex(item.uvMax)
    val dateSdf = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    val timeSdf = SimpleDateFormat("h:mm a", Locale.getDefault())

    Card(
        Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(
                    runCatching { dateSdf.format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.date)!!) }.getOrDefault(item.date),
                    style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold
                )
                Text("Peak at ${timeSdf.format(Date(item.uvMaxTime))}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // UV bar
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${item.uvMax.toInt()}", style = MaterialTheme.typography.headlineSmall, color = color, fontWeight = FontWeight.Bold)
                    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.2f)) {
                        Text(severity.label, Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.SemiBold)
                    }
                }
                UvBar(item.uvMax)
            }
        }
    }
}

@Composable
private fun UvBar(uvMax: Double) {
    val color = uvIndexColor(uvMax)
    val pct = (uvMax / 12.0).toFloat().coerceIn(0f, 1f)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.width(12.dp).height(60.dp)) {
            Box(Modifier.fillMaxWidth().fillMaxHeight().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp)))
            Box(
                Modifier.fillMaxWidth().fillMaxHeight(pct).align(Alignment.BottomCenter)
                    .background(color, RoundedCornerShape(6.dp))
            )
        }
    }
}

@Composable
private fun UvScaleLegend() {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("UV Index Scale", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            listOf(
                SunSafeColors.UvLow to "0–2  Low – Safe for most",
                SunSafeColors.UvModerate to "3–5  Moderate – Apply SPF 30+",
                SunSafeColors.UvHigh to "6–7  High – Limit midday exposure",
                SunSafeColors.UvVeryHigh to "8–10 Very High – Avoid 10am–4pm",
                SunSafeColors.UvExtreme to "11+  Extreme – Avoid outdoors"
            ).forEach { (color, desc) ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.size(14.dp).background(color, RoundedCornerShape(3.dp)))
                    Text(desc, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
