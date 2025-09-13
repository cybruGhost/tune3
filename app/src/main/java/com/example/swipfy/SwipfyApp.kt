package com.example.swipfy

import android.app.Application
import com.example.swipfy.data.local.AppDatabase
import com.example.swipfy.data.repository.MusicRepository
import com.example.swipfy.data.repository.PreferenceRepository

class SwipfyApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val musicRepository by lazy { MusicRepository(database.songDao()) }
    val preferenceRepository by lazy { PreferenceRepository(this) }

    override fun onCreate() {
        super.onCreate()
        // Initialize algorithm with user preferences
        AlgorithmManager.initialize(this)
    }
}