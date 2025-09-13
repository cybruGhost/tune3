package com.example.swipfy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.swipfy.data.models.Song

@Dao
interface SongDao {

    @Insert
    suspend fun insert(song: Song)

    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSongById(id: String): Song?

    @Query("SELECT * FROM songs WHERE isLiked = 1")
    suspend fun getLikedSongs(): List<Song>

    @Query("SELECT * FROM songs WHERE playlistName = :playlistName")
    suspend fun getPlaylistSongs(playlistName: String): List<Song>

    @Query("UPDATE songs SET isLiked = :isLiked WHERE id = :id")
    suspend fun updateLikeStatus(id: String, isLiked: Boolean)

    @Query("UPDATE songs SET playlistName = :playlistName WHERE id = :id")
    suspend fun addToPlaylist(id: String, playlistName: String?)

    // ✅ Add this method to fix your compile error
    @Query("SELECT * FROM songs")
    suspend fun getAllSongs(): List<Song>
}
