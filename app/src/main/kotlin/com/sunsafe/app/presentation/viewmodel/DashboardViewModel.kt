package com.sunsafe.app.presentation.viewmodel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunsafe.app.domain.model.*
import com.sunsafe.app.domain.repository.*
import com.sunsafe.app.domain.usecase.CalculateRemainingExposureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true,
    val userProfile: UserProfile? = null,
    val uvData: UvData? = null,
    val currentLux: Float = 0f,
    val isInSunlight: Boolean = false,
    val sunscreenStatus: SunscreenStatus = SunscreenStatus(false, 0, null, null),
    val todayExposureMinutes: Double = 0.0,
    val safeExposureMinutes: Double = 60.0,
    val remainingMinutes: Double = 60.0,
    val exposurePercentage: Float = 0f,
    val exposureStatus: ExposureStatus = ExposureStatus.SAFE,
    val locationName: String = "Getting location…",
    val todaySessions: List<ExposureSession> = emptyList(),
    val recommendations: List<String> = emptyList(),
    val vitaminDMinutes: Double = Double.MAX_VALUE,
    val errorMessage: String? = null,
    val sensorAvailable: Boolean = true,
    val locationPermissionDenied: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val uvRepository: UvRepository,
    private val exposureRepository: ExposureRepository,
    private val settingsRepository: SettingsRepository,
    private val locationRepository: LocationRepository,
    private val calculateRemainingExposure: CalculateRemainingExposureUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private var sensorListener: SensorEventListener? = null
    private var sunlightThreshold = 10_000f

    init {
        startObserving()
        startLightSensorMonitoring()
    }

    private fun startObserving() {
        viewModelScope.launch {
            settingsRepository.getSensorSettings().collect { s ->
                sunlightThreshold = s.sunlightThresholdLux
            }
        }

        viewModelScope.launch {
            combine(
                userRepository.getUserProfile(),
                settingsRepository.getSunscreenStatus()
            ) { user, sunscreen -> Pair(user, sunscreen) }
            .collectLatest { (user, sunscreen) ->
                if (user == null) { _uiState.update { it.copy(isLoading = false) }; return@collectLatest }
                _uiState.update { it.copy(userProfile = user, sunscreenStatus = sunscreen) }
                fetchUvAndLocation(user, sunscreen)
            }
        }

        viewModelScope.launch {
            val start = getTodayStart()
            val end = start + 86_400_000L
            exposureRepository.getSessionsInRange(start, end).collect { sessions ->
                val total = sessions.sumOf { it.durationMinutes }
                val state = _uiState.value
                val user = state.userProfile ?: return@collect
                val remaining = calculateRemainingExposure(
                    CalculateRemainingExposureUseCase.Input(
                        skinType = user.skinType,
                        uvIndex = state.uvData?.uvIndex ?: 3.0,
                        sunscreenStatus = state.sunscreenStatus,
                        elapsedMinutesToday = total,
                        vitaminDDeficient = user.vitaminDDeficient
                    )
                )
                _uiState.update {
                    it.copy(
                        todaySessions = sessions,
                        todayExposureMinutes = total,
                        remainingMinutes = remaining.remainingMinutes,
                        exposurePercentage = remaining.percentageUsed,
                        exposureStatus = remaining.status,
                        safeExposureMinutes = remaining.safeExposureResult.safeMinutes,
                        vitaminDMinutes = remaining.safeExposureResult.vitaminDMinutes,
                        recommendations = remaining.safeExposureResult.recommendations
                    )
                }
            }
        }
    }

    private fun fetchUvAndLocation(user: UserProfile, sunscreen: SunscreenStatus) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val locResult = locationRepository.getCurrentLocation()
                if (locResult.isSuccess) {
                    val (lat, lon) = locResult.getOrThrow()
                    _uiState.update { it.copy(locationName = locationRepository.getLocationName(lat, lon)) }
                    val uvResult = uvRepository.getCurrentUvIndex(lat, lon)
                    if (uvResult.isSuccess) {
                        val uv = uvResult.getOrThrow()
                        val remaining = calculateRemainingExposure(
                            CalculateRemainingExposureUseCase.Input(
                                skinType = user.skinType,
                                uvIndex = uv.uvIndex,
                                sunscreenStatus = sunscreen,
                                elapsedMinutesToday = _uiState.value.todayExposureMinutes,
                                vitaminDDeficient = user.vitaminDDeficient
                            )
                        )
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                uvData = uv,
                                safeExposureMinutes = remaining.safeExposureResult.safeMinutes,
                                remainingMinutes = remaining.remainingMinutes,
                                exposurePercentage = remaining.percentageUsed,
                                exposureStatus = remaining.status,
                                vitaminDMinutes = remaining.safeExposureResult.vitaminDMinutes,
                                recommendations = remaining.safeExposureResult.recommendations
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "UV data unavailable — using estimate") }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, locationPermissionDenied = true, locationName = "Location unavailable") }
                }
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    private fun startLightSensorMonitoring() {
        if (lightSensor == null) { _uiState.update { it.copy(sensorAvailable = false) }; return }
        sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val lux = event?.values?.firstOrNull() ?: return
                val inSun = lux >= sunlightThreshold
                _uiState.update { it.copy(currentLux = lux, isInSunlight = inSun) }
                if (inSun) autoStartSession() else autoEndSession()
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun autoStartSession() {
        viewModelScope.launch {
            if (exposureRepository.getActiveSession() != null) return@launch
            val state = _uiState.value
            val user = state.userProfile ?: return@launch
            exposureRepository.startSession(ExposureSession(
                userId = user.id,
                startTime = System.currentTimeMillis(),
                avgLux = state.currentLux,
                maxLux = state.currentLux,
                uvIndex = state.uvData?.uvIndex ?: 0.0,
                latitude = state.uvData?.latitude,
                longitude = state.uvData?.longitude,
                locationName = state.locationName,
                sunscreenApplied = state.sunscreenStatus.applied,
                sunscreenSPF = if (state.sunscreenStatus.applied) state.sunscreenStatus.spf else 0
            ))
        }
    }

    private fun autoEndSession() {
        viewModelScope.launch {
            val active = exposureRepository.getActiveSession() ?: return@launch
            val now = System.currentTimeMillis()
            exposureRepository.endSession(active.id, now, _uiState.value.currentLux, active.maxLux,
                (now - active.startTime) / 60_000.0)
        }
    }

    fun applySunscreen(spf: Int) { viewModelScope.launch { settingsRepository.applyingSunscreen(spf) } }

    fun refresh() {
        viewModelScope.launch {
            val user = _uiState.value.userProfile ?: return@launch
            fetchUvAndLocation(user, _uiState.value.sunscreenStatus)
        }
    }

    fun logManualExposure(durationMinutes: Double, uvIndex: Double, spf: Int) {
        viewModelScope.launch {
            val user = _uiState.value.userProfile ?: return@launch
            exposureRepository.logManualExposure(ExposureSession(
                userId = user.id,
                startTime = System.currentTimeMillis() - (durationMinutes * 60_000).toLong(),
                endTime = System.currentTimeMillis(),
                durationMinutes = durationMinutes,
                uvIndex = uvIndex,
                locationName = _uiState.value.locationName,
                sunscreenApplied = spf > 0,
                sunscreenSPF = spf
            ))
        }
    }

    private fun getTodayStart() = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    override fun onCleared() { super.onCleared(); sensorListener?.let { sensorManager.unregisterListener(it) } }
}
