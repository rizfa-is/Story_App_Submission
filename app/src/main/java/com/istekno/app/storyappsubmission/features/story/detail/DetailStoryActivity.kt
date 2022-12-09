package com.istekno.app.storyappsubmission.features.story.detail

import android.os.Bundle
import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.core.utils.extensions.Context.showImage
import com.istekno.app.core.utils.extensions.String.formatDate
import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.base.BaseActivityAuth
import com.istekno.app.storyappsubmission.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : BaseActivityAuth<ActivityDetailStoryBinding>() {

    override val layout: Int
        get() = R.layout.activity_detail_story

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupData()
    }

    private fun setupData() {
        intent?.getParcelableExtra<Story.Response.Data>("story").let {
            binding.apply {
                data = it

                tvTime.text = it?.createdAt?.formatDate()
                it?.photoUrl?.let { it1 -> showImage(it1, ivPhoto) }
            }
        }
    }
}