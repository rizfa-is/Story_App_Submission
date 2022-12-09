package com.istekno.app.storyappsubmission.features.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.istekno.app.core.utils.Delay.delayAction
import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.base.BaseActivity
import com.istekno.app.storyappsubmission.databinding.ActivitySplashScreenBinding
import com.istekno.app.storyappsubmission.features.auth.AuthActivity
import com.istekno.app.storyappsubmission.features.story.StoryActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashScreenBinding>() {

    override val layout: Int
        get() = R.layout.activity_splash_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        delayAction(DELAY_TIME) {
            startActivity(
                Intent(
                    this,
                    if (userPreferences.isFirstTime || userPreferences.accessToken.isNullOrEmpty())
                        AuthActivity::class.java
                    else
                        StoryActivity::class.java
                )
            )
            finish()
        }
    }

    companion object {
        private const val DELAY_TIME = 2000L
    }
}