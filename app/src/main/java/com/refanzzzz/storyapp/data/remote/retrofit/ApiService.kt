package com.refanzzzz.storyapp.data.remote.retrofit

import com.refanzzzz.storyapp.data.remote.response.AddStoryResponse
import com.refanzzzz.storyapp.data.remote.response.DetailStoryResponse
import com.refanzzzz.storyapp.data.remote.response.LoginResponse
import com.refanzzzz.storyapp.data.remote.response.RegisterResponse
import com.refanzzzz.storyapp.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : AddStoryResponse

    @GET("stories")
    suspend fun getAllStory() : StoryResponse
}