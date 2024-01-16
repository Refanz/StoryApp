package com.refanzzzz.storyapp.data.di

import android.content.Context
import com.refanzzzz.storyapp.data.local.LoginPreferences
import com.refanzzzz.storyapp.data.local.dataSore
import com.refanzzzz.storyapp.data.remote.retrofit.ApiConfig
import com.refanzzzz.storyapp.data.repository.StoryRepository
import com.refanzzzz.storyapp.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val loginPreferences = LoginPreferences.getInstance(context.dataSore)
        val user = runBlocking {
            loginPreferences.getSessions().first()
        }
        val apiService = ApiConfig.getApiService(user.token.toString())


        return UserRepository.getInstance(apiService, loginPreferences)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val loginPreferences = LoginPreferences.getInstance(context.dataSore)
        val user = runBlocking {
            loginPreferences.getSessions().first()
        }

        val apiService = ApiConfig.getApiService(user.token.toString())

        return StoryRepository.getInstance(apiService)
    }
}