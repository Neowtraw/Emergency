package com.codingub.emergency.data.repos

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.codingub.emergency.core.Country
import com.codingub.emergency.domain.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")

class DataStoreRepository(context: Context) {

    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
        val onUserKey = stringPreferencesKey(name = "on_user")
        val onVerificationCodeKey = stringPreferencesKey(name = "on_verification_code")
        val onCountryKey = stringPreferencesKey(name = "on_country_key")
        val onLastNewsLinkKey = stringPreferencesKey(name = "on_last_news_link_key")
    }

    private val dataStore = context.dataStore

    /*
        Last News Link
     */

    suspend fun saveLastNewsLink(link: String) {
        dataStore.edit {  preferences ->
            preferences[PreferencesKey.onLastNewsLinkKey] = link
        }
    }

    fun readLastNewsLink() : Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {preferences ->
                preferences[PreferencesKey.onLastNewsLinkKey] ?: ""
            }
    }

    /*
        Language
     */
    suspend fun saveLanguage(country: Country) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onCountryKey] = Gson().toJson(country)
        }
    }

    fun readLanguage(): Flow<Country> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                Gson().fromJson(
                    preferences[PreferencesKey.onCountryKey],
                    object : TypeToken<Country>() {}.type
                ) ?: Country.Russia
            }
    }

    /*
        Verification code
     */
    suspend fun saveVerificationCode(verificationKey: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onVerificationCodeKey] = verificationKey
        }
    }

    fun readOnVerificationCode(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val verificationCode = preferences[PreferencesKey.onVerificationCodeKey] ?: ""
                verificationCode
            }
    }

    /*
       User
    */
    suspend fun saveUserInfo(user: User) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onUserKey] = Gson().toJson(user)
        }
    }

    fun readUserInfo(): Flow<User> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                Gson().fromJson(
                    preferences[PreferencesKey.onUserKey],
                    object : TypeToken<User>() {}.type
                ) ?: User()
            }
    }

    /*
        OnBoarding
     */
    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = completed
        }
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[PreferencesKey.onBoardingKey] ?: false
                onBoardingState
            }
    }


}