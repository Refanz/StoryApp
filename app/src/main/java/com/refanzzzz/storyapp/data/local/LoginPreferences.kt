package com.refanzzzz.storyapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.refanzzzz.storyapp.model.UserSession
import com.refanzzzz.storyapp.data.remote.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataSore: DataStore<Preferences> by preferencesDataStore(name = "sessions")

class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val TOKEN_KEY = stringPreferencesKey("token")

    fun getSessions(): Flow<UserSession> {
        return dataStore.data.map { preferences ->
            UserSession(
                preferences[USER_ID_KEY] ?: "",
                preferences[USER_NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: ""
            )
        }
    }

    suspend fun setUserSession(user: LoginResult) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.userId.toString()
            preferences[USER_NAME_KEY] = user.name.toString()
            preferences[TOKEN_KEY] = user.token.toString()
        }
    }

    suspend fun removeUserSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}