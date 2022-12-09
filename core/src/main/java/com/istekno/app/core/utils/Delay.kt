package com.istekno.app.core.utils

import android.content.Context
import android.os.Handler

object Delay {
    fun Context.delayAction(time: Long, action: () -> Unit) {
        Handler(mainLooper).postDelayed(
            { action() }, time
        )
    }
}