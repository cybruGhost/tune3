package com.example.swipfy.data.models

data class UserPreference(
    val songId: String,
    val action: String,
    val genre: String,
    val timestamp: Long
)