package com.istekno.app.core.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager constructor(context: Context) {
    private var preferences: SharedPreferences
    private var editor: SharedPreferences.Editor
    private val mode = Context.MODE_PRIVATE

    init {
        preferences = context.getSharedPreferences(dbName, mode)
        editor = preferences.edit()
        editor.apply()
    }

    companion object {
        private const val dbName = "STORY_APP"
        private lateinit var instance: PreferencesManager

        @Synchronized
        fun getInstance(context: Context): PreferencesManager {
            instance = PreferencesManager(context)
            return instance
        }
    }

    var userId: String?
        get() {
            return preferences.getString(dbName + "_UID", "")
        }
        set(status) {
            if (status != null) editor.putString(dbName + "_UID", status).apply()
        }

    var userName: String?
        get() {
            return preferences.getString(dbName + "_UNAME", "")
        }
        set(status) {
            if (status != null) editor.putString(dbName + "_UNAME", status).apply()
        }

    var accessToken: String?
        get() {
            return preferences.getString(dbName + "_TOKEN", "")
        }
        set(status) {
            if (status != null) editor.putString(dbName + "_TOKEN", status).apply()
        }

    var isFirstTime: Boolean
        get() {
            return preferences.getBoolean(dbName + "_FIRST", true)
        }
        set(status) {
            editor.putBoolean(dbName + "_FIRST", status).apply()
        }
}