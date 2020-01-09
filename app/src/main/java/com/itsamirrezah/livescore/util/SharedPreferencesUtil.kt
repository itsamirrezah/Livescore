package com.itsamirrezah.livescore.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itsamirrezah.livescore.ui.model.CompetitionUi

class SharedPreferencesUtil(context: Context) {

    var pref: SharedPreferences? = null

    init {
        pref = PreferenceManager.getDefaultSharedPreferences(context)
    }

    var statusSelected: String?
        get() = pref!!.getString("STATUS_SELECTED", "")
        set(value) = pref!!.edit().putString("STATUS_SELECTED", value).apply()

    var competitionsSelected: Set<String>?
        get() = pref!!.getStringSet("COMPETITIONS_SELECTED", setOf())
        set(value) = pref!!.edit().putStringSet("COMPETITIONS_SELECTED", value).apply()

    var serverCompetitions: List<CompetitionUi>
        get() = mapGsonToCompUiModel(pref!!.getString("SERVER_COMPETITION", null))
        set(value) = pref!!.edit().putString("SERVER_COMPETITION", Gson().toJson(value)).apply()

    var localCompetitions: Set<String>?
        get() = pref!!.getStringSet("LOCAL_COMPETITION", setOf())
        set(value) = pref!!.edit().putStringSet("LOCAL_COMPETITION", value).apply()

    private fun mapGsonToCompUiModel(gson: String?): List<CompetitionUi> {
        if (gson == null)
            return listOf()
        val compListType = object : TypeToken<List<CompetitionUi>>() {}.type
        return Gson().fromJson(gson, compListType) as List<CompetitionUi>
    }

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