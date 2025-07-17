package com.farizz.suitmediaapp

import android.content.Context

class UserPreference(context: Context) {

    companion object {
        private const val PREF_NAME = "user_pref"
        private const val KEY_NAME = "key_name"
    }

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveName(name: String) {
        preferences.edit().putString(KEY_NAME, name).apply()
    }

    fun getName(): String {
        return preferences.getString(KEY_NAME, "") ?: ""
    }
}
