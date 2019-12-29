package com.itsamirrezah.livescore.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedPreferencesUtil(context: Context) {

    var pref: SharedPreferences? = null

    init {
        pref = PreferenceManager.getDefaultSharedPreferences(context)
    }

    var statusSelected: String
        get() = pref!!.getString("STATUS_SELECTED", "") as String
        set(value) = pref!!.edit().putString("STATUS_SELECTED", value).apply()

    var competitionsSelected: Set<String>
        get() = pref!!.getStringSet("COMPETITIONS_SELECTED", setOf()) as Set<String>
        set(value) = pref!!.edit().putStringSet("COMPETITIONS_SELECTED", value).apply()

    companion object {
        var instance: SharedPreferencesUtil? = null

        fun getInstance(context: Context): SharedPreferencesUtil {
            if (instance == null) {
                instance = SharedPreferencesUtil(context)
            }
            return instance as SharedPreferencesUtil
        }
    }
}