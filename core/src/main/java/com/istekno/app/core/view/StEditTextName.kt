package com.istekno.app.core.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.istekno.app.core.R
import com.istekno.app.core.utils.AppUtils
import com.istekno.app.core.utils.extensions.Views.onTextChanged

class StEditTextName: AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        onTextChanged {
            AppUtils.InitTextWatcher {
                when {
                    it.isEmpty() -> error = resources.getText(R.string.error_empty)
                }
            }
        }
    }
}