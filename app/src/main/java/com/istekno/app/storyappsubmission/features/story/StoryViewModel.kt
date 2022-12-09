package com.istekno.app.storyappsubmission.features.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.core.utils.Validator
import com.istekno.app.core.domain.repository.StoryRepository
import com.istekno.app.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(private val storyRepository: StoryRepository): ViewModel() {

    private var _storyData = MutableLiveData<Resource<Story.Response>>()
    var storyData: MutableLiveData<Resource<Story.Response>>
        get() = _storyData
        set(value) {
            _storyData = value
        }

    private var _responseData = MutableLiveData<Resource<Validator.Response>>()
    var responseData: MutableLiveData<Resource<Validator.Response>>
        get() = _responseData
        set(value) {
            _responseData = value
        }

    fun addStory(request: Story.Request) {
        responseData.postValue(Resource.loading())

        viewModelScope.launch {
            try {
                val result = storyRepository.addStory(
                    request.photo!!,
                    request.description!!,
                    request.lat,
                    request.lon
                )

                if (!result.error)
                    responseData.postValue(Resource.success(result))
                else
                    responseData.postValue(Resource.error(result.message))

            } catch (e: Exception) {
                e.printStackTrace()
                responseData.postValue(Resource.error(e.message))
            }
        }
    }

    fun getAllStory(): LiveData<PagingData<Story.Response.Data>> {
        return storyRepository.getAllStories().cachedIn(viewModelScope)
    }

    fun getAllStory(location: Int) {
        storyData.postValue(Resource.loading())

        viewModelScope.launch {
            try {
                val result = storyRepository.getAllStoryByLocation(location)
                if (!result.error) {
                    if (result.listStory.isNotEmpty())
                        storyData.postValue(Resource.success(result))
                    else
                        storyData.postValue(Resource.empty("", result))
                } else
                    storyData.postValue(Resource.error(result.message))

            } catch (e: Exception) {
                e.printStackTrace()
                storyData.postValue(Resource.error(e.message))
            }
        }
    }
}