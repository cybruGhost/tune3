package com.example.swipfy

import com.example.swipfy.data.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

object MusicApi {
    private const val LYRICS_OVH_BASE_URL = "https://api.lyrics.ovh"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun searchTracks(query: String, limit: Int = 20): List<Song> {
        return withContext(Dispatchers.IO) {
            try {
                searchLyricsOvhSuggest(query, limit)
            } catch (e: Exception) {
                // Fallback to mock data if API fails
                createMockSongs().take(limit)
            }
        }
    }

    private fun searchLyricsOvhSuggest(query: String, limit: Int): List<Song> {
        val encodedQuery = URLEncoder.encode(query, "UTF-8")
        val url = "$LYRICS_OVH_BASE_URL/suggest/$encodedQuery"
        
        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .header("User-Agent", "Swipfy Music App")
            .build()
        
        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw Exception("Empty response")
        
        if (!response.isSuccessful) {
            throw Exception("Lyrics.ovh API request failed: ${response.code}")
        }
        
        // Check if response is HTML instead of JSON (sometimes happens with lyrics.ovh)
        if (body.trim().startsWith("<!DOCTYPE") || body.trim().startsWith("<html")) {
            throw Exception("Lyrics.ovh returned HTML instead of JSON")
        }
        
        val json = JSONObject(body)
        val data = json.getJSONArray("data")
        val songs = mutableListOf<Song>()
        
        for (i in 0 until minOf(data.length(), limit)) {
            val item = data.getJSONObject(i)
            val artist = item.getJSONObject("artist")
            val album = item.getJSONObject("album")
            
            val song = Song(
                id = item.getString("id"),
                title = item.getString("title"),
                artist = artist.getString("name"),
                album = album.getString("title"),
                albumCover = album.getString("cover_medium"),
                previewUrl = item.getString("preview"),
                duration = item.getInt("duration"),
                genre = detectGenreFromTitle(item.getString("title")),
                releaseDate = "", // Lyrics.ovh doesn't provide release date
                popularity = 0,
                externalUrl = item.getString("link"),
                isLiked = false,
                playlistName = null
            )
            songs.add(song)
        }
        
        return songs
    }

    private fun detectGenreFromTitle(title: String): String {
        return when {
            title.contains("rock", true) -> "Rock"
            title.contains("pop", true) -> "Pop"
            title.contains("hip", true) || title.contains("rap", true) -> "HipHop"
            title.contains("jazz", true) -> "Jazz"
            title.contains("classical", true) -> "Classical"
            title.contains("electronic", true) || title.contains("dance", true) -> "Electronic"
            title.contains("r&b", true) || title.contains("soul", true) -> "R&B"
            else -> "Unknown"
        }
    }

    suspend fun getLyrics(artist: String, title: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val encodedArtist = URLEncoder.encode(artist, "UTF-8")
                val encodedTitle = URLEncoder.encode(title, "UTF-8")
                val url = "$LYRICS_OVH_BASE_URL/v1/$encodedArtist/$encodedTitle"
                
                val request = Request.Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("User-Agent", "Swipfy Music App")
                    .build()
                
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: throw Exception("Empty response")
                
                if (!response.isSuccessful) {
                    throw Exception("Lyrics API request failed: ${response.code}")
                }
                
                val json = JSONObject(body)
                val lyrics = json.optString("lyrics", "Lyrics not available")
                
                // Format lyrics with proper line breaks
                lyrics.replace("\\r\\n|\\r|\\n".toRegex(), "\n")
            } catch (e: Exception) {
                "Lyrics not available for this song\n\nError: ${e.message}"
            }
        }
    }

    suspend fun getArtistInfo(artist: String): JSONObject {
        return withContext(Dispatchers.IO) {
            try {
                // Since lyrics.ovh doesn't have artist info endpoint,
                // we'll create a simple mock response
                JSONObject().apply {
                    put("name", artist)
                    put("picture_medium", "")
                    put("nb_fan", 0)
                    put("nb_album", 0)
                }
            } catch (e: Exception) {
                throw Exception("Could not get artist info: ${e.message}")
            }
        }
    }

    private fun createMockSongs(): List<Song> {
        return listOf(
            Song(
                id = "3135556",
                title = "Bohemian Rhapsody",
                artist = "Queen",
                album = "A Night at the Opera",
                albumCover = "https://e-cdns-images.dzcdn.net/images/cover/500a5bcf349b1e6b3e2c5f5b5c5c5c5c/300x300-000000-80-0-0.jpg",
                previewUrl = "https://cdns-preview-7.dzcdn.net/stream/c-7b5f561c588839fc70912d5f5b79b2a9-7.mp3",
                duration = 355,
                genre = "Rock",
                releaseDate = "1975-10-31",
                popularity = 95,
                externalUrl = "https://www.deezer.com/track/3135556",
                isLiked = false,
                playlistName = null
            ),
            Song(
                id = "3135557",
                title = "Hotel California",
                artist = "Eagles",
                album = "Hotel California",
                albumCover = "https://e-cdns-images.dzcdn.net/images/cover/500a5bcf349b1e6b3e2c5f5b5c5c5c5c/300x300-000000-80-0-0.jpg",
                previewUrl = "https://cdns-preview-7.dzcdn.net/stream/c-7b5f561c588839fc70912d5f5b79b2a9-7.mp3",
                duration = 390,
                genre = "Rock",
                releaseDate = "1976-12-08",
                popularity = 90,
                externalUrl = "https://www.deezer.com/track/3135557",
                isLiked = false,
                playlistName = null
            ),
            Song(
                id = "3135558",
                title = "Sweet Child O' Mine",
                artist = "Guns N' Roses",
                album = "Appetite for Destruction",
                albumCover = "https://e-cdns-images.dzcdn.net/images/cover/500a5bcf349b1e6b3e2c5f5b5c5c5c5c/300x300-000000-80-0-0.jpg",
                previewUrl = "https://cdns-preview-7.dzcdn.net/stream/c-7b5f561c588839fc70912d5f5b79b2a9-7.mp3",
                duration = 356,
                genre = "Rock",
                releaseDate = "1987-07-21",
                popularity = 92,
                externalUrl = "https://www.deezer.com/track/3135558",
                isLiked = false,
                playlistName = null
            ),
            Song(
                id = "3135559",
                title = "Billie Jean",
                artist = "Michael Jackson",
                album = "Thriller",
                albumCover = "https://e-cdns-images.dzcdn.net/images/cover/500a5bcf349b1e6b3e2c5f5b5c5c5c5c/300x300-000000-80-0-0.jpg",
                previewUrl = "https://cdns-preview-7.dzcdn.net/stream/c-7b5f561c588839fc70912d5f5b79b2a9-7.mp3",
                duration = 294,
                genre = "Pop",
                releaseDate = "1982-11-30",
                popularity = 98,
                externalUrl = "https://www.deezer.com/track/3135559",
                isLiked = false,
                playlistName = null
            ),
            Song(
                id = "3135560",
                title = "Smells Like Teen Spirit",
                artist = "Nirvana",
                album = "Nevermind",
                albumCover = "https://e-cdns-images.dzcdn.net/images/cover/500a5bcf349b1e6b3e2c5f5b5c5c5c5c/300x300-000000-80-0-0.jpg",
                previewUrl = "https://cdns-preview-7.dzcdn.net/stream/c-7b5f561c588839fc70912d5f5b79b2a9-7.mp3",
                duration = 301,
                genre = "Grunge",
                releaseDate = "1991-09-10",
                popularity = 94,
                externalUrl = "https://www.deezer.com/track/3135560",
                isLiked = false,
                playlistName = null
            )
        )
    }
}