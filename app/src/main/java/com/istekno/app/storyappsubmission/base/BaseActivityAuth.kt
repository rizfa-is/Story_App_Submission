package com.istekno.app.storyappsubmission.base

import androidx.databinding.ViewDataBinding
import com.istekno.app.core.utils.extensions.Context.sessionExpDialog
import com.istekno.app.storyappsubmission.features.auth.AuthActivity

abstract class BaseActivityAuth<binding: ViewDataBinding>: BaseActivity<binding>() {

    override fun onResume() {
        super.onResume()
        if (!userPreferences.isFirstTime && userPreferences.accessToken.isNullOrEmpty()) sessionExpDialog(AuthActivity::class.java)
    }
}