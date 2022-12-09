package com.istekno.app.storyappsubmission.features.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.istekno.app.core.data.source.remote.model.Register
import com.istekno.app.core.utils.Validator
import com.istekno.app.core.utils.AppUtils
import com.istekno.app.core.utils.Dialog.dialog2
import com.istekno.app.core.utils.NetworkUtils.populateState
import com.istekno.app.core.utils.extensions.Views.navigateInto
import com.istekno.app.core.utils.extensions.Views.onCLick
import com.istekno.app.core.utils.extensions.Views.onTextChanged
import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.base.BaseFragment
import com.istekno.app.storyappsubmission.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val layout: Int
        get() = R.layout.fragment_register

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var registerValidator: Validator.Register

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupViews()
    }

    private fun setupData() {
        registerValidator = Validator.Register()

        observerRegister()
    }

    private fun setupViews() {
        binding.apply {
            etName.onTextChanged {
                AppUtils.InitTextWatcher {
                    registerValidator.name = it.isNotEmpty()
                    btnRegister.isEnabled = registerValidator.filled()
                }
            }

            etEmail.onTextChanged {
                AppUtils.InitTextWatcher {
                    registerValidator.email = it.isNotEmpty() && AppUtils.isEmailValid(it)
                    btnRegister.isEnabled = registerValidator.filled()
                }
            }

            etPassword.onTextChanged {
                AppUtils.InitTextWatcher {
                    registerValidator.password = it.isNotEmpty() && it.length >= 6
                    btnRegister.isEnabled = registerValidator.filled()
                }
            }

            btnRegister.onCLick {
                userRegister(
                    Register.Request(
                        etName.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )
                )
            }
        }
    }

    private fun userRegister(request: Register.Request) {
        authViewModel.userRegister(request)
    }

    private fun observerRegister() {
        authViewModel.registerData.observe(requireActivity()) {
            requireActivity().apply {
                populateState(
                    it,
                    onSuccess = {
                        hideLoading()
                        dialog2(it.data?.message ?: "") {
                            requireView().navigateInto(R.id.action_registerFragment_to_loginFragment)
                        }
                    }
                )
            }
        }
    }
}