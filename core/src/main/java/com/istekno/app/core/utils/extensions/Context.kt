package com.istekno.app.core.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.istekno.app.core.R
import com.istekno.app.core.utils.Alert.LogD
import com.istekno.app.core.utils.AppUtils
import com.istekno.app.core.utils.Dialog.dialog
import com.istekno.app.core.utils.Dialog.dialog2
import com.istekno.app.core.utils.Dialog.dialogCheckInternet
import com.istekno.app.core.utils.PreferencesManager
import kotlin.String

object Context {

    fun Context.showImage(
        image: String,
        view: ImageView,
        @DrawableRes placeholder: Int = R.drawable.ic_outline_photo_size_select_actual_24
    ){
        Glide.with(this)
            .load(image)
            .placeholder(placeholder)
            .into(view)
    }

    fun Context.showImage(
        bitmap: Bitmap,
        view: ImageView,
        @DrawableRes placeholder: Int = R.drawable.ic_outline_photo_size_select_actual_24
    ) {
        Glide.with(this)
            .load(bitmap)
            .placeholder(placeholder)
            .into(view)
    }

    fun Activity.connectionCheck(closeAll: () -> Unit) = if (!AppUtils.isOnline()) dialogCheckInternet { closeAll() } else { LogD("isOnline","true") }

    fun Activity.sessionExpDialog(destination: Class<*>) {
        dialog2(
            message = getString(R.string.msg_session_exp)
        ) {
            PreferencesManager.getInstance(this).accessToken = ""
            startActivity(
                Intent(
                    this,
                    destination
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            finish()
        }
    }

    fun Activity.showLogoutDialog(destination: Class<*>) {
        dialog(
            message = getString(R.string.msg_logout)
        ) {
            PreferencesManager.getInstance(this).accessToken = ""
            startActivity(
                Intent(
                    this,
                    destination
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            finish()
        }
    }
}