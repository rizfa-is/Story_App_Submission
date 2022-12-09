package com.istekno.app.core.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.istekno.app.core.utils.Dialog.cancelLoading
import com.istekno.app.core.utils.Dialog.dialog2
import com.istekno.app.core.utils.Dialog.setLoading

object NetworkUtils {
    fun <T> Context.populateState(
        data: Resource<T>,
        onSuccess:() -> Unit,
        onEmpty:() -> Unit = {},
        onStart:() -> Unit = {},
        onFinish:() -> Unit = {}
    ) {
        when(data.status) {
            Status.LOADING -> {
                onStart()
                setLoading(this)
            }
            Status.EMPTY -> {
                cancelLoading()
                onEmpty()
                onFinish()
            }
            Status.ERROR -> {
                cancelLoading()
                dialog2(
                    message = data.message ?: "",
                    action = { dialog ->
                        dialog.dismiss()
                    }
                )
                onFinish()
            }
            Status.SUCCESS -> {
                onSuccess()
                onFinish()
            }
        }
    }

    fun <T> LiveData<T>.observerOnce(observer: (T, Observer<T>) -> Unit) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T) {
                observer(t, this)
            }
        })
    }
}