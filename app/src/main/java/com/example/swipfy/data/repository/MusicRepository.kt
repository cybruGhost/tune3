package com.example.swipfy.data.repository

import com.example.swipfy.data.local.SongDao
import com.example.swipfy.data.models.Song

class MusicRepository(private val songDao: SongDao) {

    // Add song to liked songs
    suspend fun addToLikedSongs(song: Song) {
        val existingSong = songDao.getSongById(song.id)
        if (existingSong != null) {
            // Update like status if song exists
            songDao.updateLikeStatus(song.id, true)
        } else {
            // Insert new song marked as liked
            songDao.insert(song.copy(isLiked = true))
        }
    }

    // Remove song from liked songs
    suspend fun removeFromLikedSongs(songId: String) {
        songDao.updateLikeStatus(songId, false)
    }

    // Add song to a playlist
    suspend fun addToPlaylist(song: Song, playlistName: String) {
        val existingSong = songDao.getSongById(song.id)
        if (existingSong != null) {
            songDao.addToPlaylist(song.id, playlistName)
        } else {
            songDao.insert(song.copy(playlistName = playlistName))
        }
    }

    // Remove song from a playlist
    suspend fun removeFromPlaylist(songId: String) {
        songDao.addToPlaylist(songId, null)
    }

    // Get all liked songs
    suspend fun getLikedSongs(): List<Song> {
        return songDao.getLikedSongs()
    }

    // Get songs in a specific playlist
    suspend fun getPlaylistSongs(playlistName: String): List<Song> {
        return songDao.getPlaylistSongs(playlistName)
    }

    // Get all songs in the database
    suspend fun getAllSongs(): List<Song> {
        // Fixed: ensure DAO has getAllSongs() method
        return songDao.getAllSongs()
    }

    // Fetch recommended songs (delegated to AlgorithmManager)
    suspend fun getRecommendedSongs(): List<Song> {
        return emptyList()
    }

    // Get user playlists, return defaults if none exist
    suspend fun getUserPlaylists(): List<String> {
        val allSongs = getAllSongs()
        val playlistNames = allSongs.mapNotNull { it.playlistName }.distinct()
        return if (playlistNames.isEmpty()) {
            listOf("Favorites", "Recently Added", "Top Rated")
        } else {
            playlistNames
        }
    }

    // Create a playlist (placeholder, always returns true)
    suspend fun createPlaylist(playlistName: String): Boolean {
        return true
    }

    // Delete a playlist and remove association from songs
    suspend fun deletePlaylist(playlistName: String) {
        val playlistSongs = songDao.getPlaylistSongs(playlistName)
        playlistSongs.forEach { song ->
            songDao.addToPlaylist(song.id, null)
        }
    }

    // Search songs by title, artist, album, or genre
    suspend fun searchSongs(query: String): List<Song> {
        val allSongs = getAllSongs()
        return allSongs.filter { song ->
            song.title.contains(query, ignoreCase = true) ||
            song.artist.contains(query, ignoreCase = true) ||
            song.album.contains(query, ignoreCase = true) ||
            song.genre.contains(query, ignoreCase = true)
        }
    }

    // Get a single song by its ID
    suspend fun getSongById(songId: String): Song? {
        return songDao.getSongById(songId)
    }
}
