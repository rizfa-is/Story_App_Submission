package com.istekno.app.storyappsubmission.features.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.istekno.app.storyappsubmission.db.model.Story
import com.istekno.app.storyappsubmission.db.repository.StoryRepository
import com.istekno.app.storyappsubmission.features.story.StoryAdapter
import com.istekno.app.storyappsubmission.features.story.StoryViewModel
import com.istekno.app.storyappsubmission.features.utils.CoroutinesTestRule
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyStories
import com.istekno.app.storyappsubmission.features.utils.PagedTestDataSource
import com.istekno.app.storyappsubmission.features.utils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class GetStoriesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel

    @Before
    fun setup() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `get all stories success`() = runTest {
        val dummyList = PagedTestDataSource.snapshot(generateDummyStories())
        val dummyStories = MutableLiveData<PagingData<Story.Response.Data>>()
        dummyStories.value = dummyList

        `when`(storyRepository.getAllStories()).thenReturn(dummyStories)
        val actualStories = storyViewModel.getAllStory().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFFUTIL_CALLBACK,
            updateCallback = storyListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        assertNotNull(differ.snapshot())
        assertEquals(generateDummyStories().size, differ.snapshot().size)
    }

    private val storyListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}