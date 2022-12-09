package com.istekno.app.core.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.istekno.app.core.R
import com.istekno.app.core.utils.AppUtils
import com.istekno.app.core.utils.AppUtils.isEmailValid
import com.istekno.app.core.utils.extensions.Views.onTextChanged

class StEditTextEmail: AppCompatEditText {

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
        hint = context.getText(R.string.hint_email)
        onTextChanged {
            AppUtils.InitTextWatcher {
                when {
                    it.isEmpty() -> error = resources.getText(R.string.error_empty_email)
                    !isEmailValid(it) -> error = resources.getText(R.string.error_email_wrong_format)
                }
            }
        }
    }
}