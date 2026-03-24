package com.sunsafe.app.presentation.screens.onboarding

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.*
import com.sunsafe.app.domain.model.SkinType
import com.sunsafe.app.presentation.components.SkinTypeCard
import com.sunsafe.app.presentation.theme.SunSafeColors
import com.sunsafe.app.presentation.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 5 })
    val scope = rememberCoroutineScope()

    val locationPermissions = rememberMultiplePermissionsState(listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ))

    fun goNext() { scope.launch {
        if (pagerState.currentPage < 4) pagerState.animateScrollToPage(pagerState.currentPage + 1)
        else viewModel.completeOnboarding(onFinished)
    }}
    fun goBack() { scope.launch {
        if (pagerState.currentPage > 0) pagerState.animateScrollToPage(pagerState.currentPage - 1)
    }}

    Box(Modifier.fillMaxSize().background(
        Brush.verticalGradient(listOf(SunSafeColors.BackgroundLight, Color(0xFFFFF3E0)))
    )) {
        Column(Modifier.fillMaxSize()) {
            HorizontalPager(state = pagerState, userScrollEnabled = false, modifier = Modifier.weight(1f)) { page ->
                when (page) {
                    0 -> WelcomePage()
                    1 -> SkinTypePage(state.skinType, viewModel::setSkinType)
                    2 -> SunscreenPage(state.defaultSPF, viewModel::setSPF)
                    3 -> ProfilePage(state.age, viewModel::setAge, state.name, viewModel::setName, state.vitaminDDeficient, viewModel::setVitaminDDeficient)
                    4 -> PermissionsPage(locationPermissions)
                }
            }

            // Page dots
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center) {
                repeat(5) { i ->
                    Box(Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (pagerState.currentPage == i) 24.dp else 8.dp, 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (pagerState.currentPage == i) SunSafeColors.SunOrange else SunSafeColors.SunOrange.copy(alpha = 0.3f))
                    )
                }
            }

            // Buttons
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                if (pagerState.currentPage > 0) {
                    TextButton(onClick = ::goBack) { Icon(Icons.Filled.ArrowBack, null); Spacer(Modifier.width(4.dp)); Text("Back") }
                } else Spacer(Modifier.width(80.dp))

                Button(
                    onClick = ::goNext,
                    enabled = !state.isSaving,
                    colors = ButtonDefaults.buttonColors(containerColor = SunSafeColors.SunOrange),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.height(52.dp).widthIn(min = 140.dp)
                ) {
                    if (state.isSaving) CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    else { Text(if (pagerState.currentPage == 4) "Get Started!" else "Next"); Spacer(Modifier.width(4.dp)); Icon(Icons.Filled.ArrowForward, null) }
                }
            }
        }
    }
}

@Composable
private fun WelcomePage() {
    Column(Modifier.fillMaxSize().padding(32.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("☀️", fontSize = 96.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Text("SunSafe", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold,
            color = SunSafeColors.SunOrange, textAlign = TextAlign.Center)
        Text("Your personal UV exposure tracker", style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp, bottom = 40.dp))
        listOf("🌡️" to "Real-time UV index", "⏱️" to "Personalized safe exposure time",
            "🔔" to "Sunscreen reminders", "📊" to "Exposure history & trends", "🌤️" to "5-day UV forecast"
        ).forEach { (e, d) ->
            Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(e, fontSize = 24.sp); Text(d, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun SkinTypePage(selected: SkinType, onSelect: (SkinType) -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Column(Modifier.padding(horizontal = 8.dp, vertical = 24.dp)) {
            Text("What's your skin type?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Calculated using the Fitzpatrick Scale for personalised sun safety.",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
        }
        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(SkinType.entries.size) { i ->
                SkinTypeCard(SkinType.entries[i], selected == SkinType.entries[i], { onSelect(SkinType.entries[i]) })
            }
        }
    }
}

@Composable
private fun SunscreenPage(defaultSpf: Int, onSpfChange: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🧴", fontSize = 72.sp)
        Spacer(Modifier.height(16.dp))
        Text("Sunscreen Preferences", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text("Set your default SPF for reminders.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 8.dp))
        Spacer(Modifier.height(16.dp))
        listOf(15 to "Basic", 30 to "Recommended", 50 to "High", 70 to "Very High", 100 to "Maximum").forEach { (spf, desc) ->
            Card(
                Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onSpfChange(spf) },
                colors = CardDefaults.cardColors(containerColor = if (defaultSpf == spf) SunSafeColors.SunOrange.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface),
                border = if (defaultSpf == spf) BorderStroke(2.dp, SunSafeColors.SunOrange) else null
            ) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("SPF $spf", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (defaultSpf == spf) Icon(Icons.Filled.CheckCircle, null, tint = SunSafeColors.SunOrange)
                }
            }
        }
    }
}

@Composable
private fun ProfilePage(age: Int, onAgeChange: (Int) -> Unit, name: String, onNameChange: (String) -> Unit, vitaminD: Boolean, onVitaminDChange: (Boolean) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(32.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text("👤", fontSize = 64.sp)
        Text("Your Profile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Optional — helps us personalise your experience.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedTextField(value = name, onValueChange = onNameChange, label = { Text("Name (optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Column {
            Text("Age: $age", style = MaterialTheme.typography.titleSmall)
            Slider(value = age.toFloat(), onValueChange = { onAgeChange(it.toInt()) }, valueRange = 10f..90f, steps = 80, colors = SliderDefaults.colors(thumbColor = SunSafeColors.SunOrange, activeTrackColor = SunSafeColors.SunOrange))
        }
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Vitamin D Deficient?", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text("We'll adjust recommendations to help you get enough Vitamin D.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = vitaminD, onCheckedChange = onVitaminDChange)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionsPage(permissionsState: MultiplePermissionsState) {
    Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("📍", fontSize = 72.sp)
        Spacer(Modifier.height(16.dp))
        Text("Permissions Needed", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("SunSafe needs these to work correctly:", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        listOf("📍" to "Location" to "Fetches real-time UV for your area", "🔔" to "Notifications" to "Sunscreen reminders & UV warnings", "💡" to "Light Sensor" to "Auto-detects when you're outdoors").forEach { (p, desc) ->
            val (emoji, perm) = p
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(12.dp)) {
                Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, fontSize = 32.sp)
                    Column { Text(perm, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold); Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        if (!permissionsState.allPermissionsGranted) {
            Button(onClick = { permissionsState.launchMultiplePermissionRequest() }, colors = ButtonDefaults.buttonColors(containerColor = SunSafeColors.SunOrange), modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(50)) { Text("Grant Permissions") }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.CheckCircle, null, tint = SunSafeColors.SafeGreen, modifier = Modifier.size(24.dp))
                Text("All permissions granted!", style = MaterialTheme.typography.titleSmall, color = SunSafeColors.SafeGreen, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("You can change these anytime in Settings.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}
