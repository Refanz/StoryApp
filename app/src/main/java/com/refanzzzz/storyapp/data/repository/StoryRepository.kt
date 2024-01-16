package com.refanzzzz.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.refanzzzz.storyapp.data.Result
import com.refanzzzz.storyapp.data.remote.response.AddStoryResponse
import com.refanzzzz.storyapp.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService
) {

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository {
            if (INSTANCE == null) {
                synchronized(StoryRepository::class.java) {
                    INSTANCE = StoryRepository(apiService)
                }
            }

            return INSTANCE as StoryRepository
        }
    }

    fun addNewStory(imageFile: File, description: String) = liveData {
        emit(Result.Loading)

        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        try {
            val successResponse = apiService.addNewStory(multipartBody, requestBody)
            emit(Result.Success(successResponse))

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getAllStory() = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.getAllStory()
            emit(Result.Success(successResponse))

        } catch (e: HttpException) {
            Log.d("StoryRepository", "getStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
}