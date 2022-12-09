package com.istekno.app.core.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.istekno.app.core.R

object Dialog {
    private var builder: AlertDialog.Builder? = null
    private var loading: AlertDialog? = null

    fun Context.dialog(message: String, action: () -> Unit) {
        val dialog = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.notice))
            setMessage(message)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                action()
            }
            setNegativeButton(getString(R.string.no)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        }
        dialog.show()
    }

    fun Context.dialog2(message: String, action: (dialogInterface: DialogInterface) -> Unit) {
        val dialog = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.notice))
            setMessage(message)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { dialogInterface, _ ->
                action(dialogInterface)
            }
        }
        dialog.show()
    }

    fun Context.dialogCheckInternet(closeAll: () -> Unit) {
        val dialog = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.network_info))
            setMessage(getString(R.string.no_internet))
            setIcon(R.drawable.ic_no_internet)
            setCancelable(false)
            setPositiveButton("OK") { _, _ ->
                closeAll()
            }
        }
        dialog.show()
    }

    fun setLoading(context: Context) {
        if (builder == null && loading == null) {
            builder = AlertDialog.Builder(context)
            builder?.setView(R.layout.dialog_loading)
            builder?.setCancelable(false)

            loading = builder?.create()
            loading?.show()
        }
    }

    fun cancelLoading() {
        if (loading != null && loading?.isShowing == true) {
            loading = try {
                loading?.dismiss()
                null
            } catch (e: Exception) {
                null
            }
        }
        builder = null
    }
}