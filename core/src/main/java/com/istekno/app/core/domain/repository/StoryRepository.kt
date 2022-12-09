package com.istekno.app.core.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.core.data.source.remote.network.story.StoryService
import com.istekno.app.core.paging.StoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(private val storyService: StoryService) {
    fun getAllStories(): LiveData<PagingData<Story.Response.Data>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(storyService)
            }
        ).liveData
    }

    suspend fun getAllStoryByLocation(location: Int) =
        storyService.getAllStory(location)

    suspend fun addStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) = storyService.postStory(
        photo,
        description,
        lat,
        lon
    )
}