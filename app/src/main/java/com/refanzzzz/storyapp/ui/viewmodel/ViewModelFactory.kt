package com.refanzzzz.storyapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.refanzzzz.storyapp.di.Injection

class ViewModelFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(Injection.provideUserRepository(mApplication)) as T
        }

        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(Injection.provideStoryRepository(mApplication)) as T
        }

        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(Injection.provideStoryRepository(mApplication)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }

            return INSTANCE as ViewModelFactory
        }
    }

}