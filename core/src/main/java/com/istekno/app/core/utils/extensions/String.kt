package com.istekno.app.core.utils.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.String

object String {
    fun String.formatDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val output = SimpleDateFormat("dd MMM yyyy")
        val d: Date = sdf.parse(this)

        return output.format(d)
    }
}