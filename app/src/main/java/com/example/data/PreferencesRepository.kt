package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.i18n.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "medical_score_pro_prefs")

data class UserPreferences(
    val language: AppLanguage = AppLanguage.FR,
    val consentAccepted: Boolean = false
)

class PreferencesRepository(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("app_language")
        val CONSENT_ACCEPTED = booleanPreferencesKey("consent_accepted_v1")
    }

    val preferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            language = AppLanguage.fromCode(prefs[Keys.LANGUAGE]),
            consentAccepted = prefs[Keys.CONSENT_ACCEPTED] ?: false
        )
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LANGUAGE] = language.code
        }
    }

    suspend fun setConsentAccepted(accepted: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CONSENT_ACCEPTED] = accepted
        }
    }
}
