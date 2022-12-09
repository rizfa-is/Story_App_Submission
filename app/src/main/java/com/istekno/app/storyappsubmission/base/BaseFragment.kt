package com.istekno.app.storyappsubmission.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.istekno.app.core.utils.Dialog.cancelLoading
import com.istekno.app.core.utils.Dialog.setLoading
import com.istekno.app.core.utils.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<binding: ViewDataBinding>: Fragment(), CoroutineScope {

    abstract val layout: Int
    lateinit var binding: binding

    val userPreferences by lazy { PreferencesManager.getInstance(requireContext()) }

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)

        return binding.root
    }

    fun setupBackPressed() {
        activity.let {
            activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { activity?.finish() }
            })
        }
    }

    fun showLoading() { setLoading(requireActivity()) }

    fun hideLoading() { cancelLoading() }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}