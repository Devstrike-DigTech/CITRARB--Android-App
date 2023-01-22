/*
 * Copyright (c) 2023.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.app.citrarb.features.account.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.devstrike.app.citrarb.R
import org.devstrike.app.citrarb.base.BaseFragment
import org.devstrike.app.citrarb.databinding.FragmentAccountCreateBinding
import org.devstrike.app.citrarb.databinding.FragmentAccountLogInBinding
import org.devstrike.app.citrarb.features.account.data.UserApi
import org.devstrike.app.citrarb.features.account.repositories.UserRepoImpl
import org.devstrike.app.citrarb.network.Resource
import org.devstrike.app.citrarb.utils.SessionManager
import org.devstrike.app.citrarb.utils.toast
import org.devstrike.app.citrarb.utils.visible
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class AccountCreate : BaseFragment<UserViewModel, FragmentAccountCreateBinding, UserRepoImpl>() {

    @set:Inject
    var userApi: UserApi by Delegates.notNull()
    @set:Inject
    var sessionManager: SessionManager by Delegates.notNull()

    private val userViewModel: UserViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToRegisterEvents()

        with(binding){
            //get the values from the UI
            accountCreateBtn.setOnClickListener {
                val name = createAccountUsername.text.toString()
                val email = createAccountEmail.text.toString()
                val password = createAccountPassword.text.toString()
                val confirmPassword = accountCreateConfirmPassword.text.toString()

                //call the create user function from the  user view model and pass the fetched UI values
                userViewModel.createUser(
                    name.trim(),
                    email.trim(),
                    password.trim(),
                    confirmPassword.trim()
                )
            }
        }

    }


    //function to handle the responses from the network call using the register state in view model
    private fun subscribeToRegisterEvents() = lifecycleScope.launch {
        userViewModel.registerState.collect { result ->
            when(result){
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Account Successfully Created",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }
                is Resource.Failure -> {
                    hideProgressBar()
                    requireContext().toast(result.value!!)
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }


    private fun showProgressBar(){
        binding.createAccountProgressBar.visible(true)
    }


    private fun hideProgressBar(){

        binding.createAccountProgressBar.visible(false)
    }



    override fun getFragmentRepo() = UserRepoImpl(userApi, sessionManager)

    override fun getViewModel() = UserViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAccountCreateBinding.inflate(inflater, container, false)

}