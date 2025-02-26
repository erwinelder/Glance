package com.ataglance.walletglance.settings.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.ataglance.walletglance.core.domain.app.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val USER_ID = stringPreferencesKey("userId")
        val LANGUAGE = stringPreferencesKey("language")
        val SETUP_STAGE = intPreferencesKey("setupStage")
        val USE_DEVICE_THEME = booleanPreferencesKey("useDeviceTheme")
        val CHOSEN_LIGHT_THEME = stringPreferencesKey("chosenLightTheme")
        val CHOSEN_DARK_THEME = stringPreferencesKey("chosenDarkTheme")
        val LAST_CHOSEN_THEME = stringPreferencesKey("lastChosenTheme")
        const val TAG = "SettingsRepository"
    }


    val userId: Flow<String?> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading user id.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USER_ID]?.takeIf { it.isNotBlank() }
        }

    suspend fun saveUserIdPreference(userId: String) {
        dataStore.edit { it[USER_ID] = userId }
    }


    val language: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading language.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LANGUAGE] ?: AppLanguage.English.languageCode
        }

    suspend fun saveLanguagePreference(langCode: String) {
        dataStore.edit { it[LANGUAGE] = langCode }
    }


    val setupStage: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading isSetUp.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SETUP_STAGE] ?: 0
        }

    suspend fun saveIsSetUpPreference(value: Int) {
        dataStore.edit { it[SETUP_STAGE] = value }
    }


    val useDeviceTheme: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading useDeviceTheme", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USE_DEVICE_THEME] ?: true
        }

    suspend fun saveUseDeviceThemePreference(value: Boolean) {
        dataStore.edit { it[USE_DEVICE_THEME] = value }
    }


    val chosenLightTheme: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading chosenLightTheme", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[CHOSEN_LIGHT_THEME] ?: AppTheme.LightDefault.name
        }

    suspend fun saveChosenLightThemePreference(theme: String) {
        dataStore.edit { it[CHOSEN_LIGHT_THEME] = theme }
    }


    val chosenDarkTheme: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading chosenDarkTheme", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[CHOSEN_DARK_THEME] ?: AppTheme.DarkDefault.name
        }

    suspend fun saveChosenDarkThemePreference(theme: String) {
        dataStore.edit { it[CHOSEN_DARK_THEME] = theme }
    }


    val lastChosenTheme: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading lastChosenTheme", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LAST_CHOSEN_THEME] ?: AppTheme.LightDefault.name
        }

    suspend fun saveLastChosenThemePreference(theme: String) {
        dataStore.edit { it[LAST_CHOSEN_THEME] = theme }
    }


    suspend fun clearAllPreferences() {
        dataStore.edit { it.clear() }
    }

}