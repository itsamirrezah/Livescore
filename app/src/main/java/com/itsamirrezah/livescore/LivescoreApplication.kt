package com.itsamirrezah.livescore

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.itsamirrezah.livescore.util.SharedPreferencesUtil

class LivescoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupAppTheme()
    }

    private fun setupAppTheme() {
        if (SharedPreferencesUtil.getInstance(this).isNightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}