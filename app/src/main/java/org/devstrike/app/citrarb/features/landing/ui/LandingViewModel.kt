/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.app.citrarb.features.landing.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import org.devstrike.app.citrarb.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by Richard Uzor  on 26/12/2022
 */
/**
 * Created by Richard Uzor  on 26/12/2022
 */
@HiltViewModel
class LandingViewModel @Inject constructor(
    private val repo: LandingRepo
): BaseViewModel(repo) {
}