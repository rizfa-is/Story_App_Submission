package com.istekno.app.storyappsubmission.features.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.istekno.app.core.utils.extensions.Context.showLogoutDialog
import com.istekno.app.core.utils.extensions.Views.onCLick
import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.base.BaseFragment
import com.istekno.app.storyappsubmission.databinding.FragmentProfileBinding
import com.istekno.app.storyappsubmission.features.auth.AuthActivity

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val layout: Int
        get() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            user = userPreferences.userName

            tvLanguage.onCLick {
                startActivity(
                    Intent(Settings.ACTION_LOCALE_SETTINGS)
                )
            }

            tvLogout.onCLick {
                requireActivity().showLogoutDialog(AuthActivity::class.java)
            }
        }
    }
}