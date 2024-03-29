/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.app.citrarb.features.news.savednews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.devstrike.app.citrarb.base.BaseFragment
import org.devstrike.app.citrarb.databinding.FragmentSavedNewsListBinding
import org.devstrike.app.citrarb.features.news.data.NewsApi
import org.devstrike.app.citrarb.features.news.data.NewsDao
import org.devstrike.app.citrarb.features.news.newsLanding.NewsViewModel
import org.devstrike.app.citrarb.features.news.repositories.NewsRepoImpl
import javax.inject.Inject
import kotlin.properties.Delegates

/*
* UI fragment to display the list of all locally saved news
* */
@AndroidEntryPoint
class SavedNewsList : BaseFragment<NewsViewModel, FragmentSavedNewsListBinding, NewsRepoImpl>() {

    @set:Inject
    var newsApi: NewsApi by Delegates.notNull<NewsApi>()
    @set:Inject
    var newsDao: NewsDao by Delegates.notNull<NewsDao>()

    private lateinit var savedNewsAdapter: SavedNewsAdapter
    private val newsViewModel: NewsViewModel by activityViewModels()
    private val TAG = "savedNewsList"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        subscribeToNews()
    }

    private fun setUpRecyclerView() {
        savedNewsAdapter = SavedNewsAdapter()
        savedNewsAdapter.setOnItemClickListener {
            val navToDetail = SavedNewsListDirections.actionSavedNewsListToSavedNewsDetail(it)
            findNavController().navigate(navToDetail)
        }
        val savedNewsLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSavedNewsList.apply {
            adapter = savedNewsAdapter
            layoutManager = savedNewsLayoutManager
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), savedNewsLayoutManager.orientation
                )
            )
        }
    }

    //fetch the news from teh local db
    private fun subscribeToNews() = lifecycleScope.launch {
        newsViewModel.savedNewsFromDB.collect {
            savedNewsAdapter.savedNews = it
        }

    }

    override fun getFragmentRepo() = NewsRepoImpl(newsApi, newsDao)

    override fun getViewModel() = NewsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSavedNewsListBinding.inflate(inflater, container, false)

}