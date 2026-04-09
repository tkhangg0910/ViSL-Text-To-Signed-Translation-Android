package com.example.final_project

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.final_project.ui.navigation.AppNavGraph
import com.example.final_project.ui.theme.ViSLTheme
import com.example.final_project.ui.screens.debug.TranslationDebugScreen
import com.example.final_project.ui.screens.settings.SettingsViewModel
import com.example.final_project.ui.screens.translate.TranslateScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import androidx.core.content.edit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("language_code", "vi") ?: "vi"
        val locale = Locale.forLanguageTag(lang)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingVm: SettingsViewModel = hiltViewModel()
            val uiState by settingVm.settingUiState.collectAsState()

            var prevLang by remember { mutableStateOf(uiState.languageCode) }
            LaunchedEffect(uiState.languageCode) {
                if (uiState.languageCode != prevLang) {
                    prevLang = uiState.languageCode
                    getSharedPreferences("settings", Context.MODE_PRIVATE)
                        .edit {
                            putString("language_code", uiState.languageCode)
                        }
                    recreate()
                }
            }

            ViSLTheme(darkTheme = uiState.isDarkMode) {
                AppNavGraph()
            }
        }
    }

}

