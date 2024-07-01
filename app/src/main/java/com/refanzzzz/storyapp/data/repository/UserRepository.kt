package com.refanzzzz.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.refanzzzz.storyapp.data.Result
import com.refanzzzz.storyapp.data.local.LoginPreferences
import com.refanzzzz.storyapp.model.UserSession
import com.refanzzzz.storyapp.data.remote.response.LoginResponse
import com.refanzzzz.storyapp.data.remote.response.RegisterResponse
import com.refanzzzz.storyapp.data.remote.retrofit.ApiService
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val loginPreferences: LoginPreferences
) {
    fun register(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.register(name, email, password)
            emit(Result.Success(successResponse))

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)

            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.login(email, password)
            val userResponse = successResponse.loginResult

            if (userResponse != null) {
                loginPreferences.setUserSession(userResponse)
            }

            emit(Result.Success(successResponse))

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)

            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getInfoUserLogin(): LiveData<UserSession> {
        return loginPreferences.getSessions().asLiveData()
    }

    suspend fun removeUserLogin() {
        loginPreferences.removeUserSession()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            loginPreferences: LoginPreferences
        ): UserRepository {
            if (INSTANCE == null) {
                synchronized(UserRepository::class.java) {
                    INSTANCE = UserRepository(apiService, loginPreferences)
                }
            }

            return INSTANCE as UserRepository
        }
    }
}