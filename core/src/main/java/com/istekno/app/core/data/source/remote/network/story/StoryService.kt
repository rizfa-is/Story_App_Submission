package com.istekno.app.core.data.source.remote.network.story

import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.core.utils.Validator
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryService {
    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") long: RequestBody?
    ): Validator.Response

    @GET("stories")
    fun getAllStory(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Deferred<Story.Response>

    @GET("stories")
    suspend fun getAllStory(
        @Query("location") location: Int
    ): Story.Response
}