package com.istekno.app.storyappsubmission.features.utils

import java.lang.reflect.Field

object ReflectUtils {
    fun setField(`object`: Any?, fld: Field?, value: Any?) {
        try {
            fld?.isAccessible = true
            fld?.set(`object`, value)
        } catch (e: IllegalAccessException) {
            val fieldName = if (null == fld) "n/a" else fld.name
            throw RuntimeException("Failed to set $fieldName of object", e)
        }
    }
}