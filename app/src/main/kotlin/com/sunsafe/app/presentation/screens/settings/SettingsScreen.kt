package com.sunsafe.app.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.sunsafe.app.domain.model.*
import com.sunsafe.app.presentation.theme.SunSafeColors
import com.sunsafe.app.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null) } }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile
            item { SectionHeader("👤 Profile") }
            item {
                state.userProfile?.let { profile ->
                    ProfileSettingsCard(profile, onSave = { viewModel.updateProfile(it) })
                }
            }

            // Notifications
            item { SectionHeader("🔔 Notifications") }
            item {
                NotificationSettingsCard(state.notificationSettings, onChange = { viewModel.updateNotificationSettings(it) })
            }

            // Sensor
            item { SectionHeader("💡 Sensor") }
            item {
                SensorSettingsCard(state.sensorSettings, onChange = { viewModel.updateSensorSettings(it) })
            }

            // Appearance
            item { SectionHeader("🎨 Appearance") }
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.DarkMode, null, tint = SunSafeColors.SkyBlue)
                            Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                        }
                        Switch(checked = state.darkMode, onCheckedChange = { viewModel.toggleDarkMode(it) })
                    }
                }
            }

            // About
            item { SectionHeader("ℹ️ About") }
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column {
                        SettingsRow("App Version", "1.0.0")
                        HorizontalDivider()
                        SettingsRow("Privacy Policy", null, showChevron = true)
                        HorizontalDivider()
                        SettingsRow("Open Source Licences", null, showChevron = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
}

@Composable
private fun SettingsRow(label: String, value: String?, showChevron: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value != null) Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (showChevron) Icon(Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ProfileSettingsCard(profile: UserProfile, onSave: (UserProfile) -> Unit) {
    var skinType by remember(profile) { mutableStateOf(profile.skinType) }
    var spf by remember(profile) { mutableStateOf(profile.defaultSPF) }
    var vitD by remember(profile) { mutableStateOf(profile.vitaminDDeficient) }
    var expanded by remember { mutableStateOf(false) }

    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Skin type dropdown
            Text("Skin Type", style = MaterialTheme.typography.titleSmall)
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = "${skinType.emoji} ${skinType.displayName} — ${skinType.description}",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    SkinType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text("${type.emoji} ${type.displayName} — ${type.description}") },
                            onClick = { skinType = type; expanded = false }
                        )
                    }
                }
            }

            // Default SPF
            Text("Default SPF: $spf", style = MaterialTheme.typography.titleSmall)
            Slider(value = spf.toFloat(), onValueChange = { spf = it.toInt() }, valueRange = 15f..100f,
                colors = SliderDefaults.colors(thumbColor = SunSafeColors.SunOrange, activeTrackColor = SunSafeColors.SunOrange))

            // Vitamin D
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Vitamin D Deficient", style = MaterialTheme.typography.bodyMedium)
                Switch(checked = vitD, onCheckedChange = { vitD = it })
            }

            Button(
                onClick = { onSave(profile.copy(skinType = skinType, defaultSPF = spf, vitaminDDeficient = vitD)) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SunSafeColors.SunOrange)
            ) { Text("Save Profile") }
        }
    }
}

@Composable
private fun NotificationSettingsCard(settings: NotificationSettings, onChange: (NotificationSettings) -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            NotifToggleRow("⚠️ UV Warnings", settings.warningsEnabled) { onChange(settings.copy(warningsEnabled = it)) }
            HorizontalDivider()
            NotifToggleRow("🧴 Sunscreen Reminders", settings.remindersEnabled) { onChange(settings.copy(remindersEnabled = it)) }
            HorizontalDivider()
            NotifToggleRow("🌤 Daily Forecast", settings.forecastEnabled) { onChange(settings.copy(forecastEnabled = it)) }
            HorizontalDivider()
            NotifToggleRow("🏆 Achievements", settings.achievementsEnabled) { onChange(settings.copy(achievementsEnabled = it)) }
        }
    }
}

@Composable
private fun NotifToggleRow(label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

@Composable
private fun SensorSettingsCard(settings: SensorSettings, onChange: (SensorSettings) -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column {
                Text("Sunlight Threshold: ${settings.sunlightThresholdLux.toInt()} lux", style = MaterialTheme.typography.titleSmall)
                Text("Higher = less sensitive to indoor light", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Slider(
                    value = settings.sunlightThresholdLux,
                    onValueChange = { onChange(settings.copy(sunlightThresholdLux = it)) },
                    valueRange = 1_000f..50_000f,
                    colors = SliderDefaults.colors(thumbColor = SunSafeColors.SunOrange, activeTrackColor = SunSafeColors.SunOrange)
                )
            }
            HorizontalDivider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Battery Saver Mode", style = MaterialTheme.typography.bodyMedium)
                    Text("Reduces sensor sampling frequency", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = settings.batterySaverMode, onCheckedChange = { onChange(settings.copy(batterySaverMode = it)) })
            }
        }
    }
}
