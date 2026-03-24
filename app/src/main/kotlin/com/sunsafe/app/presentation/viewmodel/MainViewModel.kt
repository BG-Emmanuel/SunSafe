package com.sunsafe.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunsafe.app.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val isOnboardingCompleted: StateFlow<Boolean?> = settingsRepository
        .isOnboardingCompleted()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
