package com.istekno.app.storyappsubmission.features.story

import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.base.BaseActivityAuth
import com.istekno.app.storyappsubmission.databinding.ActivityStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryActivity : BaseActivityAuth<ActivityStoryBinding>() {
    override val layout: Int
        get() = R.layout.activity_story
}