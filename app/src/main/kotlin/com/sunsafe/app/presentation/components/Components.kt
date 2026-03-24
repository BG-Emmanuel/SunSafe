package com.sunsafe.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunsafe.app.domain.model.*
import com.sunsafe.app.presentation.theme.*
import kotlin.math.min

// ─────────────────────────────────────────────────────────────────────────────
// Circular Exposure Progress Bar
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CircularExposureIndicator(
    percentage: Float,
    remainingMinutes: Double,
    safeMinutes: Double,
    status: ExposureStatus,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage.coerceIn(0f, 100f),
        animationSpec = tween(1000, easing = EaseInOutCubic),
        label = "exposure_progress"
    )

    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = exposureStatusColor(animatedPercentage)

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = size.toPx() * 0.10f
            val radius = (this.size.minDimension - strokeWidth) / 2f
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val sweepAngle = animatedPercentage / 100f * 300f  // 300° arc

            // Background track
            drawArc(
                color = trackColor,
                startAngle = 120f,
                sweepAngle = 300f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
            // Progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        progressColor.copy(alpha = 0.7f),
                        progressColor,
                        progressColor
                    ),
                    center = center
                ),
                startAngle = 120f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val displayMinutes = if (status == ExposureStatus.EXCEEDED) 0 else remainingMinutes.toInt()
            Text(
                text = if (status == ExposureStatus.EXCEEDED) "Exceeded" else "${displayMinutes}m",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = progressColor
            )
            Text(
                text = if (status == ExposureStatus.EXCEEDED) "Seek shade" else "remaining",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "of ${safeMinutes.toInt()}m safe",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// UV Index Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun UvIndexCard(
    uvData: UvData?,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (uvData != null)
                uvIndexColor(uvData.uvIndex).copy(alpha = 0.15f)
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        if (isLoading) {
            Box(Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
            }
        } else if (uvData != null) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "UV Index",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "%.1f".format(uvData.uvIndex),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = uvIndexColor(uvData.uvIndex)
                    )
                    val severity = UvSeverity.fromIndex(uvData.uvIndex)
                    SeverityChip(severity)
                }
                UvSeverityScale(uvData.uvIndex)
            }
        } else {
            Box(Modifier.fillMaxWidth().padding(20.dp)) {
                Text("UV data unavailable", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun SeverityChip(severity: UvSeverity) {
    val color = when (severity) {
        UvSeverity.LOW -> SunSafeColors.UvLow
        UvSeverity.MODERATE -> SunSafeColors.UvModerate
        UvSeverity.HIGH -> SunSafeColors.UvHigh
        UvSeverity.VERY_HIGH -> SunSafeColors.UvVeryHigh
        UvSeverity.EXTREME -> SunSafeColors.UvExtreme
    }
    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = severity.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun UvSeverityScale(currentUv: Double, modifier: Modifier = Modifier) {
    val levels = listOf(
        SunSafeColors.UvLow,
        SunSafeColors.UvModerate,
        SunSafeColors.UvHigh,
        SunSafeColors.UvVeryHigh,
        SunSafeColors.UvExtreme
    )
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        listOf("11+","8-10","6-7","3-5","0-2").forEachIndexed { i, label ->
            val active = when (i) {
                0 -> currentUv >= 11
                1 -> currentUv in 8.0..10.9
                2 -> currentUv in 6.0..7.9
                3 -> currentUv in 3.0..5.9
                else -> currentUv < 3.0
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    Modifier
                        .width(if (active) 32.dp else 20.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(levels[i].copy(alpha = if (active) 1f else 0.4f))
                )
                Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp,
                    color = if (active) levels[i] else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sunscreen Status Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SunscreenStatusCard(
    status: SunscreenStatus,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    val needsAttention = !status.applied || status.needsReapplication
    Card(
        modifier = modifier.clickable { onApply() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (needsAttention)
                SunSafeColors.WarningAmber.copy(alpha = 0.1f)
            else SunSafeColors.SafeGreen.copy(alpha = 0.1f)
        )
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    imageVector = if (status.applied && !status.needsReapplication)
                        Icons.Filled.CheckCircle else Icons.Filled.Warning,
                    contentDescription = null,
                    tint = if (needsAttention) SunSafeColors.WarningAmber else SunSafeColors.SafeGreen,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = when {
                            !status.applied -> "Apply Sunscreen"
                            status.needsReapplication -> "Reapply Sunscreen"
                            else -> "SPF ${status.spf} Applied"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = when {
                            !status.applied -> "Tap to log sunscreen"
                            status.minutesSinceApplication != null ->
                                "${status.minutesSinceApplication}m ago"
                            else -> "Protection active"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Light Sensor Indicator
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun LuxIndicator(lux: Float, isInSunlight: Boolean, sensorAvailable: Boolean) {
    if (!sensorAvailable) {
        Text("Light sensor not available", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        return
    }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isInSunlight) SunSafeColors.UvHigh else MaterialTheme.colorScheme.outline)
        )
        Text(
            text = if (isInSunlight) "☀ Outdoors — ${lux.toInt()} lux"
            else "🏠 Indoors — ${lux.toInt()} lux",
            style = MaterialTheme.typography.bodySmall,
            color = if (isInSunlight) SunSafeColors.UvHigh else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Recommendations Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun RecommendationsCard(recommendations: List<String>, modifier: Modifier = Modifier) {
    if (recommendations.isEmpty()) return
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "💡 Recommendations",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            recommendations.forEach { rec ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("•", color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text(
                        rec,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Skin Type Selector Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SkinTypeCard(
    skinType: SkinType,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = skinTypeColor(skinType.ordinal)
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) color.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.surface
        ),
        border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, color) else null,
        elevation = CardDefaults.cardElevation(if (selected) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(skinType.emoji, fontSize = 28.sp)
            }
            Text(skinType.displayName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(skinType.description, style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                "Burns in ${skinType.baseBurnMinutes}min",
                style = MaterialTheme.typography.labelSmall,
                color = color, fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Apply Sunscreen Dialog
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ApplySunscreenDialog(
    defaultSpf: Int,
    onDismiss: () -> Unit,
    onApply: (Int) -> Unit
) {
    var selectedSpf by remember { mutableStateOf(defaultSpf) }
    val spfOptions = listOf(15, 30, 50, 70, 100)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Apply Sunscreen") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Select your SPF level:", style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    spfOptions.forEach { spf ->
                        FilterChip(
                            selected = selectedSpf == spf,
                            onClick = { selectedSpf = spf },
                            label = { Text("$spf") }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onApply(selectedSpf) }) { Text("Applied ✓") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Manual Exposure Log Dialog
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun LogManualExposureDialog(
    onDismiss: () -> Unit,
    onLog: (durationMinutes: Double, uvIndex: Double, spf: Int) -> Unit
) {
    var duration by remember { mutableStateOf("30") }
    var uvIndex by remember { mutableStateOf("5") }
    var spf by remember { mutableStateOf("0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Manual Exposure") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = duration, onValueChange = { duration = it },
                    label = { Text("Duration (minutes)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = uvIndex, onValueChange = { uvIndex = it },
                    label = { Text("UV Index") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = spf, onValueChange = { spf = it },
                    label = { Text("SPF used (0 = none)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onLog(
                    duration.toDoubleOrNull() ?: 30.0,
                    uvIndex.toDoubleOrNull() ?: 5.0,
                    spf.toIntOrNull() ?: 0
                )
            }) { Text("Log") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
