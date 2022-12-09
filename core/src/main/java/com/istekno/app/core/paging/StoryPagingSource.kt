package com.istekno.app.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.core.data.source.remote.network.story.StoryService

class StoryPagingSource(private val storyService: StoryService): PagingSource<Int, Story.Response.Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story.Response.Data> =
        try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = storyService.getAllStory(position, params.loadSize).await().listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, Story.Response.Data>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}