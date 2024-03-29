/*
 * Copyright (c) 2023.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.app.citrarb.features.tv.ui.tvlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.devstrike.app.citrarb.base.BaseFragment
import org.devstrike.app.citrarb.databinding.FragmentTvVideoListBinding
import org.devstrike.app.citrarb.features.tv.data.TVVideos
import org.devstrike.app.citrarb.features.tv.data.api.TVApi
import org.devstrike.app.citrarb.features.tv.data.model.TVListItem
import org.devstrike.app.citrarb.features.tv.repositories.TVRepoImpl
import org.devstrike.app.citrarb.features.tv.ui.TVViewModel
import org.devstrike.app.citrarb.network.Resource
import org.devstrike.app.citrarb.network.handleApiError
import org.devstrike.app.citrarb.utils.Common
import org.devstrike.app.citrarb.utils.visible
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class TvVideoList : BaseFragment<TVViewModel, FragmentTvVideoListBinding, TVRepoImpl>() {

    @set:Inject
    var tvApi: TVApi by Delegates.notNull<TVApi>()

    private val TAG = "TvVideoList"
    private val tvViewModel: TVViewModel by activityViewModels()
    private lateinit var tvListAdapter: TVListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Common.toolBarTitle = "TV"
        Common.toolBarSubTitle = "Feast your eyes with engaging content"

        subscribeToTVList()
        setupRecyclerView()
        //binding.videoListProgressBar.isVisible = true

//        tvViewModel.tvVideoList.observe(viewLifecycleOwner) {
//            tvListAdapter.submitList(it.data)
//            binding.videoListProgressBar.isVisible = false
        //}

    }

    private fun subscribeToTVList() {
        tvViewModel.getTvVideos()
        tvViewModel.tvVideos.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    //binding.videoListProgressBar.isVisible = false
                    binding.tvShimmerLayout.stopShimmer()
                    binding.tvShimmerLayout.visible(false)
                    //tvListAdapter.submitList(response.value!!.data)
                    addToList(response.value!!.data)

                }
                is Resource.Failure -> {
                    //binding.videoListProgressBar.isVisible = false
                    binding.tvShimmerLayout.stopShimmer()
                    // TODO: handle no internet issue here
                    handleApiError(response.error) { subscribeToTVList() }
                }
                is Resource.Loading -> {
                    //binding.videoListProgressBar.isVisible = true
                    binding.tvShimmerLayout.startShimmer()
                }
            }
        })
    }

    private fun addToList(data: List<TVListItem>) {
        var tvVideos = TVVideos()
        for (video in data) {
            Log.d(TAG, "addToList: ${video.title}")
            tvVideos = TVVideos(
                videoTitle = video.title,
                videoDescription = video.description,
                videoLink = video.Link.toUri().encodedQuery!!.drop(2),
                videoPublishedDate = video.publishedAt.replace("T", " | ")
                    .removeSuffix("Z"),
                videoThumbnail = video.thumbnails.default!!.url
            )
            Common.tvVideos.add(tvVideos)
            tvListAdapter.submitList(Common.tvVideos)
        }
        val videosListLinks = mutableListOf<String>()
        for (links in Common.tvVideos){
            videosListLinks.add(links.videoLink)
        }
        Log.d(TAG, "addToList: ${Common.tvVideos}")
        Log.d(TAG, "addToList: $videosListLinks")
    }

    private fun setupRecyclerView() {
        binding.rvVideoList.visible(true)
        tvListAdapter = TVListAdapter(requireContext())
        val tvListLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvVideoList.apply {
            adapter = tvListAdapter
            layoutManager = tvListLayoutManager
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), tvListLayoutManager.orientation
                )
            )
        }

    }

    override fun getFragmentRepo() = TVRepoImpl(tvApi)

    override fun getViewModel() = TVViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTvVideoListBinding.inflate(inflater, container, false)
}