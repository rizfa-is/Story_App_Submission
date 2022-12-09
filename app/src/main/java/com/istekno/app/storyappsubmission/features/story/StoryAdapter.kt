package com.istekno.app.storyappsubmission.features.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.core.utils.extensions.Context.showImage
import com.istekno.app.core.utils.extensions.String.formatDate
import com.istekno.app.core.utils.extensions.Views.onCLick
import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.databinding.ItemStoryBinding

class StoryAdapter constructor(private val listener: OnItemClickListener): PagingDataAdapter<Story.Response.Data, StoryAdapter.ViewHolder>(DIFFUTIL_CALLBACK) {

    inner class ViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story.Response.Data) {
            binding.apply {
                data = story

                tvTime.text = story.createdAt.formatDate()
                root.let {
                    it.context.showImage(story.photoUrl, ivPhoto)
                    it.onCLick { listener.onStoryClick(story, binding) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_story,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFFUTIL_CALLBACK = object : DiffUtil.ItemCallback<Story.Response.Data>() {
            override fun areItemsTheSame(
                oldItem: Story.Response.Data,
                newItem: Story.Response.Data
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Story.Response.Data,
                newItem: Story.Response.Data
            ) = oldItem == newItem
        }
    }
}