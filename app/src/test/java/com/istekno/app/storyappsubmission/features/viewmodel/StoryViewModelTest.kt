package com.istekno.app.storyappsubmission.features.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.ListUpdateCallback
import com.istekno.app.storyappsubmission.db.model.Story
import com.istekno.app.storyappsubmission.db.network.story.StoryService
import com.istekno.app.storyappsubmission.db.repository.StoryRepository
import com.istekno.app.storyappsubmission.features.story.StoryViewModel
import com.istekno.app.storyappsubmission.features.utils.CoroutinesTestRule
import com.istekno.app.storyappsubmission.features.utils.DataDummy.dummyMultipartFile
import com.istekno.app.storyappsubmission.features.utils.DataDummy.dummyReqBody
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyAddStoryResponse
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyRegisterResponseError
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyStoryResponse
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyStoryResponseEmpty
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyStoryResponseError
import com.istekno.app.storyappsubmission.features.utils.ReflectUtils
import com.istekno.app.storyappsubmission.utils.Resource
import com.istekno.app.storyappsubmission.utils.Status
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel

    @Mock
    private lateinit var storyService: StoryService
    private val dummyPhoto = dummyMultipartFile()
    private val dummyDesc = dummyReqBody()
    private val dummyLat = dummyReqBody()
    private val dummyLon = dummyReqBody()
    private val dummyStoryRequest = Story.Request(
        dummyPhoto,
        dummyDesc,
        dummyLat,
        dummyLon
    )

    @Before
    fun setup() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `get stories by location success - Resource Success`() = runTest {
        ReflectUtils.setField(storyViewModel, storyViewModel.javaClass.getDeclaredField("client"), storyService)
        val expected = Resource.success(generateDummyStoryResponse())

        `when`(
            storyService.getAllStory(1)
        ).thenReturn(expected.data)
        storyViewModel.getAllStory(1)

        assertNotNull(storyViewModel.storyData.value?.data)
        assertEquals(Status.SUCCESS, storyViewModel.storyData.value?.status)
    }

    @Test
    fun `get stories by location empty - Resource Empty`() = runTest {
        ReflectUtils.setField(storyViewModel, storyViewModel.javaClass.getDeclaredField("client"), storyService)
        val expected = Resource.empty("", generateDummyStoryResponseEmpty())

        `when`(
            storyService.getAllStory(1)
        ).thenReturn(expected.data)
        storyViewModel.getAllStory(1)

        assertEquals(generateDummyStoryResponseEmpty().listStory.size, storyViewModel.storyData.value?.data?.listStory?.size)
        assertEquals(Status.EMPTY, storyViewModel.storyData.value?.status)
    }

    @Test
    fun `get stories by location failed - Resource Error`() = runTest {
        ReflectUtils.setField(storyViewModel, storyViewModel.javaClass.getDeclaredField("client"), storyService)
        val expected = Resource.error("failed", generateDummyStoryResponseError())

        `when`(
            storyService.getAllStory(1)
        ).thenReturn(expected.data)
        storyViewModel.getAllStory(1)

        assertNull(storyViewModel.storyData.value?.data)
        assertEquals(Status.ERROR, storyViewModel.storyData.value?.status)
    }

    @Test
    fun `add story success - Resource Success`() = runTest {
        ReflectUtils.setField(storyViewModel, storyViewModel.javaClass.getDeclaredField("client"), storyService)
        val expected = Resource.success(generateDummyAddStoryResponse())

        `when`(
            storyService.postStory(
                dummyPhoto,
                dummyDesc,
                dummyLat,
                dummyLon
            )
        ).thenReturn(expected.data)
        storyViewModel.addStory(dummyStoryRequest)

        assertNotNull(storyViewModel.responseData.value?.data)
        assertEquals(Status.SUCCESS, storyViewModel.responseData.value?.status)
    }

    @Test
    fun `add story failed - Resource Error`() = runTest {
        ReflectUtils.setField(storyViewModel, storyViewModel.javaClass.getDeclaredField("client"), storyService)
        val expected = Resource.error("failed", generateDummyRegisterResponseError())

        `when`(
            storyService.postStory(
                dummyPhoto,
                dummyDesc,
                dummyLat,
                dummyLon
            )
        ).thenReturn(expected.data)
        storyViewModel.addStory(dummyStoryRequest)

        assertNull(storyViewModel.responseData.value?.data)
        assertEquals(Status.ERROR, storyViewModel.responseData.value?.status)
    }

    private val storyListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}