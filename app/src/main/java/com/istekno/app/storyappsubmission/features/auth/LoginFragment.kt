package com.istekno.app.storyappsubmission.features.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.istekno.app.core.data.source.remote.model.Login
import com.istekno.app.core.utils.Validator
import com.istekno.app.core.utils.AppUtils
import com.istekno.app.core.utils.AppUtils.isEmailValid
import com.istekno.app.core.utils.NetworkUtils.populateState
import com.istekno.app.core.utils.extensions.Views.navigateInto
import com.istekno.app.core.utils.extensions.Views.onCLick
import com.istekno.app.core.utils.extensions.Views.onTextChanged
import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.base.BaseFragment
import com.istekno.app.storyappsubmission.databinding.FragmentLoginBinding
import com.istekno.app.storyappsubmission.features.story.StoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val layout: Int
        get() = R.layout.fragment_login

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var loginValidator: Validator.Login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupViews()
    }

    private fun setupData() {
        loginValidator = Validator.Login()

        observerLogin()
    }

    private fun setupViews() {
        binding.apply {
            etEmail.onTextChanged {
                AppUtils.InitTextWatcher {
                    loginValidator.email = it.isNotEmpty() && isEmailValid(it)
                    btnLogin.isEnabled = loginValidator.filled()
                }
            }

            etPassword.onTextChanged {
                AppUtils.InitTextWatcher {
                    loginValidator.password = it.isNotEmpty() && it.length >= 6
                    btnLogin.isEnabled = loginValidator.filled()
                }
            }

            tvRegisterHere.onCLick {
                requireView().navigateInto(R.id.action_loginFragment_to_registerFragment)
            }

            btnLogin.onCLick {
                userLogin(
                    Login.Request(
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )
                )
            }
        }
    }

    private fun userLogin(request: Login.Request) {
        authViewModel.userLogin(request)
    }

    private fun observerLogin() {
        authViewModel.loginData.observe(requireActivity()) {
            requireActivity().apply {
                populateState(
                    it,
                    onSuccess = {
                        hideLoading()
                        userPreferences.apply {
                            userId = it.data?.loginResult?.userId
                            userName = it.data?.loginResult?.name
                            accessToken = it.data?.loginResult?.token
                            isFirstTime = false
                        }
                        startActivity(
                            Intent(
                                this,
                                StoryActivity::class.java
                            )
                        )
                        finish()
                    }
                )
            }
        }
    }
}