package com.example.final_project.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.local.datastore.UserPreferences
import com.example.final_project.data.local.datastore.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ViewModel(){
    val settingUiState = userPreferencesDataStore.userPreferencesFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserPreferences()
        )

    fun changeGlossVisible(visible: Boolean) {
        viewModelScope.launch {
            userPreferencesDataStore.saveGlossVisible(visible)
        }

    }
    fun changeDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesDataStore.saveDarkMode(enabled)
        }
    }
    fun changeLanguage(languageCode: String) {
        viewModelScope.launch {
            userPreferencesDataStore.saveLanguage(languageCode)
        }
    }
}