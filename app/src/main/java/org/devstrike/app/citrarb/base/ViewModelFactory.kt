/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.app.citrarb.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.devstrike.app.citrarb.features.landing.repositories.LandingRepo
import org.devstrike.app.citrarb.features.landing.ui.LandingViewModel
import org.devstrike.app.citrarb.features.news.newsLanding.NewsViewModel
import org.devstrike.app.citrarb.features.news.repositories.NewsRepoImpl

/**
 * The viewModel factory class is a base class to create a viewModel and provide it to a fragment only if it is found
 * At this base point, it assigns the repo of each of the fragments to the viewModels
 * Created by Richard Uzor  on 26/12/2022
 */
class ViewModelFactory(
    private val repo: BaseRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            //... it checks the viewModel which is using it and casts the base repo to that viewModel's repo

            modelClass.isAssignableFrom(LandingViewModel::class.java) -> LandingViewModel(repo as LandingRepo) as T
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> NewsViewModel(repo as NewsRepoImpl) as T
            else -> throw IllegalAccessException("ViewModelClass Not Found")
        }
    }
}