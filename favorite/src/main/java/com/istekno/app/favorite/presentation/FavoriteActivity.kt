package com.istekno.app.favorite.presentation

import android.os.Bundle
import com.istekno.app.favorite.R
import com.istekno.app.favorite.databinding.ActivityFavoriteBinding
import com.istekno.app.storyappsubmission.base.BaseActivity

class FavoriteActivity : BaseActivity<ActivityFavoriteBinding>() {

    override val layout: Int
        get() = R.layout.activity_favorite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
    }
}