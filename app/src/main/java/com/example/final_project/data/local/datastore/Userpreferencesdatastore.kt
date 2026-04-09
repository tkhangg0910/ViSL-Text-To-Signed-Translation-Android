package com.example.final_project.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences",
)
data class UserPreferences(
    val isGlossVisible: Boolean = true,
    val isDarkMode:     Boolean = false,
    val languageCode:   String  = DEFAULT_LANG,
)
private const val DEFAULT_LANG    = "vi"

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
){
    // Keys
    private companion object Keys {
        val GLOSS_VISIBLE = booleanPreferencesKey("gloss_visible")
        val DARK_MODE     = booleanPreferencesKey("dark_mode")
        val LANGUAGE      = stringPreferencesKey("language_code")
    }


    // Read
    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            UserPreferences(
                isGlossVisible = preferences[Keys.GLOSS_VISIBLE] ?: true,
                isDarkMode     = preferences[Keys.DARK_MODE]     ?: false,
                languageCode   = preferences[Keys.LANGUAGE]      ?: DEFAULT_LANG,
            )
        }


    suspend fun saveGlossVisible(visible: Boolean) {
        context.dataStore.edit { it[Keys.GLOSS_VISIBLE] = visible }
    }

    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DARK_MODE] = enabled }
    }

    suspend fun saveLanguage(languageCode: String) {
        context.dataStore.edit { it[Keys.LANGUAGE] = languageCode }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }


}