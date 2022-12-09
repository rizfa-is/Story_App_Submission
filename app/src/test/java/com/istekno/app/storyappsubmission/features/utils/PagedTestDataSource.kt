package com.istekno.app.storyappsubmission.features.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.istekno.app.storyappsubmission.db.model.Story

class PagedTestDataSource:
    PagingSource<Int, LiveData<List<Story.Response.Data>>>() {

    override fun getRefreshKey(
        state: PagingState<Int,
                LiveData<List<Story.Response.Data>>>
    ) = 0

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, LiveData<List<Story.Response.Data>>> =
        LoadResult.Page(emptyList(), 0, 1)

    companion object {
        fun snapshot(
            items: List<Story.Response.Data>
        ) = PagingData.from(items)
    }
}