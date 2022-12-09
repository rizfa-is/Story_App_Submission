package com.istekno.app.storyappsubmission.features.auth

import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.base.BaseActivity
import com.istekno.app.storyappsubmission.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    override val layout: Int
        get() = R.layout.activity_auth
}