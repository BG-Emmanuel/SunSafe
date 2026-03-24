package com.sunsafe.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunsafe.app.domain.model.*
import com.sunsafe.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─────────────────────────────────────────────────────────────────────────────
// OnboardingViewModel
// ─────────────────────────────────────────────────────────────────────────────
data class OnboardingUiState(
    val currentPage: Int = 0,
    val skinType: SkinType = SkinType.TYPE_III,
    val defaultSPF: Int = 30,
    val vitaminDDeficient: Boolean = false,
    val age: Int = 30,
    val name: String = "",
    val isSaving: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun nextPage() = _uiState.update { it.copy(currentPage = it.currentPage + 1) }
    fun prevPage() = _uiState.update { it.copy(currentPage = (it.currentPage - 1).coerceAtLeast(0)) }
    fun setSkinType(type: SkinType) = _uiState.update { it.copy(skinType = type) }
    fun setSPF(spf: Int) = _uiState.update { it.copy(defaultSPF = spf) }
    fun setVitaminDDeficient(v: Boolean) = _uiState.update { it.copy(vitaminDDeficient = v) }
    fun setAge(age: Int) = _uiState.update { it.copy(age = age) }
    fun setName(name: String) = _uiState.update { it.copy(name = name) }

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val state = _uiState.value
            val profile = UserProfile(
                skinType = state.skinType,
                defaultSPF = state.defaultSPF,
                vitaminDDeficient = state.vitaminDDeficient,
                age = state.age,
                name = state.name
            )
            val userId = userRepository.saveUserProfile(profile)
            settingsRepository.setOnboardingCompleted(true)
            _uiState.update { it.copy(isSaving = false) }
            onSuccess()
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// HistoryViewModel
// ─────────────────────────────────────────────────────────────────────────────
data class HistoryUiState(
    val isLoading: Boolean = true,
    val dailySummaries: List<DailySummary> = emptyList(),
    val selectedDaySessions: List<ExposureSession> = emptyList(),
    val selectedDate: Long? = null,
    val totalExposureMinutes: Double = 0.0,
    val totalSessions: Int = 0,
    val burnIncidents: Int = 0
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val exposureRepository: ExposureRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            exposureRepository.getDailySummaries(30).collect { summaries ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        dailySummaries = summaries,
                        totalExposureMinutes = summaries.sumOf { s -> s.totalExposureMinutes },
                        totalSessions = summaries.sumOf { s -> s.sessionsCount },
                        burnIncidents = summaries.count { s -> s.burned }
                    )
                }
            }
        }
    }

    fun selectDate(date: Long) {
        _uiState.update { it.copy(selectedDate = date) }
        viewModelScope.launch {
            val dayStart = date
            val dayEnd = date + 86_400_000L
            exposureRepository.getSessionsInRange(dayStart, dayEnd)
                .collect { sessions ->
                    _uiState.update { it.copy(selectedDaySessions = sessions) }
                }
        }
    }

    fun clearSelection() = _uiState.update { it.copy(selectedDate = null, selectedDaySessions = emptyList()) }

    fun markAsBurned(sessionId: Long) {
        viewModelScope.launch { exposureRepository.markAsBurned(sessionId) }
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch { exposureRepository.deleteSession(sessionId) }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SettingsViewModel
// ─────────────────────────────────────────────────────────────────────────────
data class SettingsUiState(
    val isLoading: Boolean = true,
    val userProfile: UserProfile? = null,
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val sensorSettings: SensorSettings = SensorSettings(),
    val darkMode: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepository.getUserProfile(),
                settingsRepository.getAppSettings()
            ) { user, settings -> Pair(user, settings) }
            .collect { (user, settings) ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userProfile = user,
                        notificationSettings = settings.notifications,
                        sensorSettings = settings.sensorSettings,
                        darkMode = settings.darkMode
                    )
                }
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            userRepository.updateUserProfile(profile)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun updateNotificationSettings(settings: NotificationSettings) {
        viewModelScope.launch { settingsRepository.updateNotificationSettings(settings) }
    }

    fun updateSensorSettings(settings: SensorSettings) {
        viewModelScope.launch { settingsRepository.updateSensorSettings(settings) }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            val current = settingsRepository.getAppSettings().first()
            settingsRepository.updateSettings(current.copy(darkMode = enabled))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ForecastViewModel
// ─────────────────────────────────────────────────────────────────────────────
data class ForecastUiState(
    val isLoading: Boolean = true,
    val forecast: List<UvForecastItem> = emptyList(),
    val currentUv: UvData? = null,
    val locationName: String = "",
    val error: String? = null
)

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val uvRepository: UvRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForecastUiState())
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    init { loadForecast() }

    private fun loadForecast() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val locResult = locationRepository.getCurrentLocation()
                if (locResult.isSuccess) {
                    val (lat, lon) = locResult.getOrThrow()
                    val name = locationRepository.getLocationName(lat, lon)
                    val uv = uvRepository.getCurrentUvIndex(lat, lon).getOrNull()
                    val forecast = uvRepository.getUvForecast(lat, lon).getOrNull() ?: emptyList()
                    _uiState.update {
                        it.copy(isLoading = false, forecast = forecast, currentUv = uv, locationName = name)
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Location unavailable") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun refresh() = loadForecast()
}
