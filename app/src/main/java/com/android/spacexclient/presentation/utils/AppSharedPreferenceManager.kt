package com.android.spacexclient.presentation.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject


class AppSharedPreferenceManager @Inject constructor(private val sharedPreferences: SharedPreferences) {
    /**
     * Get If the launch is first time value from the SharedPreference
     * @param context the current context
     * @returns if its the first time the user is opening the app
     *
     */
    fun getIsFirstTime(): Boolean {
        val key = "FIRST_TIME_USER"
        if (sharedPreferences.getBoolean(key, true))
            with(sharedPreferences.edit()) {
                putBoolean(key, false)
                apply()
                return true
            }
        return false
    }
}