package com.istekno.app.storyappsubmission.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.istekno.app.core.utils.PreferencesManager
import com.istekno.app.core.utils.extensions.Context.connectionCheck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<binding: ViewDataBinding>: AppCompatActivity(), CoroutineScope {

    abstract val layout: Int
    lateinit var binding: binding
    val userPreferences by lazy { PreferencesManager.getInstance(this) }
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout)
    }

    override fun onResume() {
        super.onResume()
        connectionCheck { finishAffinity() }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}