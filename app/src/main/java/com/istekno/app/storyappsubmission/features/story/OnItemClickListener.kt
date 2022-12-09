package com.istekno.app.storyappsubmission.features.story

import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.storyappsubmission.databinding.ItemStoryBinding

interface OnItemClickListener {
    fun onStoryClick(story: Story.Response.Data, binding: ItemStoryBinding)
}