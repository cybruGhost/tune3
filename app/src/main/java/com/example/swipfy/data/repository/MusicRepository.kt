package com.example.swipfy.data.repository

import com.example.swipfy.data.local.SongDao
import com.example.swipfy.data.models.Song

class MusicRepository(private val songDao: SongDao) {

    suspend fun addToLikedSongs(song: Song) {
        songDao.insert(song.copy(isLiked = true))
    }

    suspend fun addToPlaylist(song: Song, playlistName: String) {
        songDao.insert(song.copy(playlistName = playlistName))
    }

    suspend fun getLikedSongs(): List<Song> {
        return songDao.getLikedSongs()
    }

    suspend fun getPlaylistSongs(playlistName: String): List<Song> {
        return songDao.getPlaylistSongs(playlistName)
    }

    suspend fun getRecommendedSongs(): List<Song> {
        // For now, return empty list - you can implement recommendation logic here
        return emptyList()
    }

    suspend fun getUserPlaylists(): List<String> {
        // Return default playlists for now
        return listOf("Favorites", "Recently Played", "My Playlist")
    }
}