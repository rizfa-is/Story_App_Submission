package com.istekno.app.storyappsubmission.features.story.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.istekno.app.core.di.StoryModule
import com.istekno.app.storyappsubmission.features.story.StoryViewModel

class StoryViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(StoryModule.provideStoryRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}