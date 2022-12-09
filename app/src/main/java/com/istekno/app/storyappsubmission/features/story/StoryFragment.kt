package com.istekno.app.storyappsubmission.features.story

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.core.paging.LoadingStateAdapter
import com.istekno.app.core.utils.extensions.Views.gone
import com.istekno.app.core.utils.extensions.Views.navigateInto
import com.istekno.app.core.utils.extensions.Views.onCLick
import com.istekno.app.core.utils.extensions.Views.show
import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.base.BaseFragment
import com.istekno.app.storyappsubmission.databinding.FragmentStoryBinding
import com.istekno.app.storyappsubmission.databinding.ItemStoryBinding
import com.istekno.app.storyappsubmission.features.maps.MapsActivity
import com.istekno.app.storyappsubmission.features.story.detail.DetailStoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryFragment : BaseFragment<FragmentStoryBinding>() {

    override val layout: Int
        get() = R.layout.fragment_story

    private val storyViewModel: StoryViewModel by viewModels()

    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            fabAddStory.onCLick {
                requireView().navigateInto(R.id.action_storyFragment_to_addStoryFragment)
            }

            ivProfile.onCLick {
                requireView().navigateInto(R.id.action_storyFragment_to_profileFragment)
            }

            ivOpenMap.onCLick {
                startActivity(
                    Intent(
                        requireContext(),
                        MapsActivity::class.java
                    )
                )
            }
        }

        initAdapter()
    }

    private fun initAdapter() {
        storyAdapter = StoryAdapter(
            object : OnItemClickListener {
                override fun onStoryClick(story: Story.Response.Data, binding: ItemStoryBinding) {
                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        Pair(binding.ivPhoto, "image"),
                        Pair(binding.tvName, "name"),
                        Pair(binding.tvDesc, "description"),
                        Pair(binding.tvTime, "date"),
                    )

                    startActivity(
                        Intent(
                            requireContext(),
                            DetailStoryActivity::class.java
                        ).putExtra("story", story),
                        optionsCompat.toBundle()
                    )
                }
            }
        )
        storyLoadingState()
        getAllStory()

        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { storyAdapter.retry() }
        )
    }

    private fun storyLoadingState() {
        storyAdapter.addLoadStateListener {
            when(it.refresh) {
                is LoadState.Loading -> {
                    binding.rvStory.gone()
                    binding.shimLayout.shimStory.show()
                    binding.shimLayout.shimStory.startShimmer()
                }
                is LoadState.NotLoading -> {
                    binding.shimLayout.shimStory.gone()
                    binding.rvStory.show()
                }
                is LoadState.Error -> {
                    binding.rvPlaceholder.show()
                }
            }
        }
    }

    private fun getAllStory() {
        storyViewModel.getAllStory().observe(requireActivity()) {
            storyAdapter.submitData(lifecycle, it)
        }
    }
}