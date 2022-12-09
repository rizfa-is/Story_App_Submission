package com.istekno.app.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import com.auth0.android.jwt.JWT
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.regex.Pattern

object AppUtils {

    class InitTextWatcher(
        private val onTextChange:(String) -> Unit
    ): TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChange(p0.toString())
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    fun isOnline(): Boolean {
        val connectionManager = AppContext.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectionManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) return true
        return false
    }

    fun isTokenExpired(): Boolean {
        val token = if (PreferencesManager.getInstance(AppContext.appContext).accessToken.isNullOrEmpty()) return true
        else PreferencesManager.getInstance(AppContext.appContext).accessToken

        val jwt = JWT(token!!)

        return jwt.isExpired(10)
    }

    fun isEmailValid(email: String): Boolean {
        val p = Pattern.compile("[A-Z0-9a-z.-_]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}")
        return p.matcher(email).matches()
    }

    fun isPermissionGranted(context: Context, myPermission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            myPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getPath(context: Context, contentUri: Uri): String? {
        var result: String? = null
        val imageProfile = arrayOf(MediaStore.Images.Media.DATA)

        val cursorLoader = CursorLoader(context, contentUri, imageProfile, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }

        return result
    }

    fun createPartFromFile(file: File): MultipartBody.Part {
        val reqFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("photo", file.name, reqFile)
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }
}