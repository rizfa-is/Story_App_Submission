package com.istekno.app.core.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

object Alert {
    fun Context.showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun Context.LogD(tag: String, text: String) {
        Log.d(tag, text)
    }
}