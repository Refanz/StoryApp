package com.refanzzzz.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.refanzzzz.storyapp.data.Result
import com.refanzzzz.storyapp.data.StoryPagingSource
import com.refanzzzz.storyapp.data.remote.response.AddStoryResponse
import com.refanzzzz.storyapp.data.remote.response.ListStoryItem
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
            emit(Result.Error(e.message()))
        }
    }

    fun getStoriesWithLocation() = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.getStoriesWithLocation()
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            emit(Result.Error(e.message()))
        }
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    companion object {

        private const val TAG = "StoryRepository"

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
}