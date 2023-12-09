package com.codingub.emergency.data.repos

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.auth.User
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
    }

    private val dataStore = context.dataStore

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

    fun readUserInfo(): Flow<User?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val user = preferences[PreferencesKey.onUserKey]
                Gson().fromJson(user, object : TypeToken<User>() {}.type)
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