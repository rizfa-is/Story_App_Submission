package com.istekno.app.core.utils.extensions

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.navigation.Navigation

object Views {
    fun View.onCLick(action: () -> Unit) {
        this.setOnClickListener { action() }
    }

    fun EditText.onTextChanged(action: () -> TextWatcher) {
        this.addTextChangedListener(action())
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide() {
        this.visibility = View.INVISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.navigateInto(destination: Int, bundle: Bundle? = null) {
        val controller = Navigation.findNavController(this)
        controller.navigate(destination, bundle)
    }
}