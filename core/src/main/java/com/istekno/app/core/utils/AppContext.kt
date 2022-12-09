package com.istekno.app.core.utils

import android.content.Context

internal object AppContext {
    @Volatile
    lateinit var appContext: Context

    fun setContext(context: Context) {
        appContext = context
    }
}