package com.example.swipfy.data.repository

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("swipfy_prefs", Context.MODE_PRIVATE)

    fun setWelcomeSeen(seen: Boolean) {
        prefs.edit().putBoolean("welcome_seen", seen).apply()
    }

    fun hasSeenWelcome(): Boolean {
        return prefs.getBoolean("welcome_seen", false)
    }

    fun setAlgorithmPreference(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getAlgorithmPreference(key: String, defaultValue: String = ""): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }
}