package com.example.swipfy.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val albumCover: String,
    val previewUrl: String,
    val duration: Int,
    val genre: String,
    val releaseDate: String,
    val popularity: Int,
    val externalUrl: String,
    val isLiked: Boolean = false,
    val playlistName: String? = null
)