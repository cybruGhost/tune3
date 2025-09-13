package com.example.swipfy

import android.content.Context
import com.example.swipfy.data.models.Song
import com.example.swipfy.data.repository.PreferenceRepository
import kotlin.random.Random

object AlgorithmManager {

    private var preferenceRepository: PreferenceRepository? = null
    private var isInitialized = false
    private val random = Random(System.currentTimeMillis())
    private val recommendationHistory = mutableSetOf<String>()

    fun initialize(context: Context) {
        if (!isInitialized) {
            preferenceRepository = PreferenceRepository(context)
            isInitialized = true
        }
    }

    fun trackPreference(song: Song, action: String) {
        if (!isInitialized) return
        
        val weight = when (action) {
            "like" -> 1.5f
            "dislike" -> -1.0f
            "add_to_playlist" -> 1.0f
            else -> 0.5f
        }

        updateUserProfile(song, weight)
        incrementInteractionCount()
    }

    private fun updateUserProfile(song: Song, weight: Float) {
        val genre = detectGenre(song)
        val currentWeight = preferenceRepository?.getAlgorithmPreference("genre_$genre", "0.0")?.toFloat() ?: 0.0f
        val newWeight = (currentWeight + weight).coerceIn(-5.0f, 10.0f)
        
        preferenceRepository?.setAlgorithmPreference("genre_$genre", newWeight.toString())
        preferenceRepository?.setAlgorithmPreference("last_interaction_${song.id}", System.currentTimeMillis().toString())
    }

    private fun detectGenre(song: Song): String {
        return when {
            song.genre.contains("rock", true) -> "rock"
            song.genre.contains("pop", true) -> "pop"
            song.genre.contains("hip", true) || song.genre.contains("rap", true) -> "hiphop"
            song.genre.contains("jazz", true) -> "jazz"
            song.genre.contains("classical", true) -> "classical"
            song.genre.contains("electronic", true) || song.genre.contains("dance", true) || song.genre.contains("edm", true) -> "electronic"
            song.genre.contains("r&b", true) || song.genre.contains("soul", true) -> "rnb"
            song.genre.contains("country", true) -> "country"
            song.genre.contains("reggae", true) -> "reggae"
            song.genre.contains("metal", true) -> "metal"
            else -> "unknown"
        }
    }

    fun getRecommendations(context: Context): List<Song> {
        if (!isInitialized) initialize(context)
        
        return try {
            val tasteProfile = getUserTasteProfile(context)
            
            if (hasStrongPreferences(tasteProfile)) {
                getPersonalizedRecommendations(tasteProfile)
            } else {
                getDiversePopularSongs()
            }
        } catch (e: Exception) {
            getDiversePopularSongs()
        }
    }

    private fun hasStrongPreferences(tasteProfile: Map<String, Float>): Boolean {
        return tasteProfile.any { it.value > 2.0f } || getInteractionCount() > 5
    }

    private fun getPersonalizedRecommendations(tasteProfile: Map<String, Float>): List<Song> {
        val topGenres = getTopGenresFromProfile(tasteProfile, 2)
        val allRecommendations = mutableListOf<Song>()
        
        // Add songs from top genres (60%)
        topGenres.forEach { genre ->
            val genreSongs = getSongsByGenre(genre)
                .filterNot { recommendationHistory.contains(it.id) }
                .shuffled()
                .take(3)
            allRecommendations.addAll(genreSongs)
        }
        
        // Add some diversity (30% from other genres)
        val otherGenres = getAllGenres().minus(topGenres.toSet())
        if (otherGenres.isNotEmpty()) {
            val randomOtherGenre = otherGenres.random()
            val diverseSongs = getSongsByGenre(randomOtherGenre)
                .filterNot { recommendationHistory.contains(it.id) }
                .shuffled()
                .take(2)
            allRecommendations.addAll(diverseSongs)
        }
        
        // Add some random popular songs (10%)
        val popularSongs = getPopularSongs()
            .filterNot { recommendationHistory.contains(it.id) }
            .shuffled()
            .take(1)
        allRecommendations.addAll(popularSongs)
        
        // Update recommendation history
        allRecommendations.forEach { recommendationHistory.add(it.id) }
        
        // If history gets too large, clear some entries
        if (recommendationHistory.size > 50) {
            recommendationHistory.clear()
        }
        
        return allRecommendations.distinctBy { it.id }.shuffled()
    }

    private fun getTopGenresFromProfile(tasteProfile: Map<String, Float>, count: Int): List<String> {
        return tasteProfile.entries
            .sortedByDescending { it.value }
            .take(count)
            .map { it.key }
            .filter { it != "unknown" }
    }

    private fun getDiversePopularSongs(): List<Song> {
        val allSongs = mutableListOf<Song>()
        val genres = getAllGenres()
        
        // Get 1-2 songs from each major genre
        genres.forEach { genre ->
            if (random.nextBoolean()) { // 50% chance to include each genre
                val genreSongs = getSongsByGenre(genre)
                    .filterNot { recommendationHistory.contains(it.id) }
                    .shuffled()
                    .take(random.nextInt(1, 3))
                allSongs.addAll(genreSongs)
            }
        }
        
        // Ensure we have at least 5 songs
        while (allSongs.size < 5) {
            val randomGenre = genres.random()
            val genreSongs = getSongsByGenre(randomGenre)
                .filterNot { recommendationHistory.contains(it.id) }
                .shuffled()
                .take(1)
            allSongs.addAll(genreSongs)
        }
        
        // Update recommendation history
        allSongs.forEach { recommendationHistory.add(it.id) }
        
        return allSongs.distinctBy { it.id }.shuffled().take(10)
    }

    private fun getAllGenres(): List<String> {
        return listOf("rock", "pop", "hiphop", "electronic", "rnb", "country", "jazz")
    }

    private fun getSongsByGenre(genre: String): List<Song> {
        return when (genre.toLowerCase()) {
            "rock" -> getRockSongs()
            "pop" -> getPopSongs()
            "hiphop" -> getHipHopSongs()
            "electronic" -> getElectronicSongs()
            "rnb" -> getRnBSongs()
            "country" -> getCountrySongs()
            "jazz" -> getJazzSongs()
            else -> getPopularSongs()
        }
    }

    private fun incrementInteractionCount() {
        val currentCount = getInteractionCount()
        preferenceRepository?.setAlgorithmPreference("interaction_count", (currentCount + 1).toString())
    }

    private fun getInteractionCount(): Int {
        return preferenceRepository?.getAlgorithmPreference("interaction_count", "0")?.toInt() ?: 0
    }

    fun getUserTasteProfile(context: Context): Map<String, Float> {
        if (!isInitialized) initialize(context)
        
        val tasteProfile = mutableMapOf<String, Float>()
        val genres = getAllGenres() + "unknown"
        
        genres.forEach { genre ->
            val weight = preferenceRepository?.getAlgorithmPreference("genre_$genre", "0.0")?.toFloat() ?: 0.0f
            tasteProfile[genre] = weight
        }
        
        return tasteProfile
    }

    // Song databases for each genre
    private fun getRockSongs(): List<Song> = listOf(
        createSong("1", "Bohemian Rhapsody", "Queen", "Rock", 95),
        createSong("2", "Sweet Child O' Mine", "Guns N' Roses", "Rock", 92),
        createSong("3", "Hotel California", "Eagles", "Rock", 90),
        createSong("4", "Back In Black", "AC/DC", "Rock", 88),
        createSong("5", "Smells Like Teen Spirit", "Nirvana", "Rock", 94),
        createSong("6", "Stairway to Heaven", "Led Zeppelin", "Rock", 96),
        createSong("7", "Wonderwall", "Oasis", "Rock", 89),
        createSong("8", "Sweet Home Alabama", "Lynyrd Skynyrd", "Rock", 87)
    )

    private fun getPopSongs(): List<Song> = listOf(
        createSong("9", "Blinding Lights", "The Weeknd", "Pop", 98),
        createSong("10", "Shape of You", "Ed Sheeran", "Pop", 97),
        createSong("11", "Bad Guy", "Billie Eilish", "Pop", 95),
        createSong("12", "Uptown Funk", "Mark Ronson ft. Bruno Mars", "Pop", 96),
        createSong("13", "Levitating", "Dua Lipa", "Pop", 93),
        createSong("14", "Watermelon Sugar", "Harry Styles", "Pop", 91),
        createSong("15", "Don't Start Now", "Dua Lipa", "Pop", 94),
        createSong("16", "drivers license", "Olivia Rodrigo", "Pop", 92)
    )

    private fun getHipHopSongs(): List<Song> = listOf(
        createSong("17", "Sicko Mode", "Travis Scott", "HipHop", 90),
        createSong("18", "God's Plan", "Drake", "HipHop", 95),
        createSong("19", "HUMBLE.", "Kendrick Lamar", "HipHop", 93),
        createSong("20", "Hotline Bling", "Drake", "HipHop", 88),
        createSong("21", "Lose Yourself", "Eminem", "HipHop", 96),
        createSong("22", "In Da Club", "50 Cent", "HipHop", 89),
        createSong("23", "Gold Digger", "Kanye West", "HipHop", 87),
        createSong("24", "Empire State of Mind", "Jay-Z ft. Alicia Keys", "HipHop", 91)
    )

    private fun getElectronicSongs(): List<Song> = listOf(
        createSong("25", "Titanium", "David Guetta ft. Sia", "Electronic", 92),
        createSong("26", "Wake Me Up", "Avicii", "Electronic", 95),
        createSong("27", "Animals", "Martin Garrix", "Electronic", 88),
        createSong("28", "Clarity", "Zedd ft. Foxes", "Electronic", 90),
        createSong("29", "Summertime Sadness", "Lana Del Rey vs Cedric Gervais", "Electronic", 87),
        createSong("30", "Levels", "Avicii", "Electronic", 93),
        createSong("31", "Tsunami", "DVBBS & Borgeous", "Electronic", 86),
        createSong("32", "Turn Down for What", "DJ Snake & Lil Jon", "Electronic", 89)
    )

    private fun getRnBSongs(): List<Song> = listOf(
        createSong("33", "Blinding Lights", "The Weeknd", "R&B", 98),
        createSong("34", "Adorn", "Miguel", "R&B", 90),
        createSong("35", "Thinking Out Loud", "Ed Sheeran", "R&B", 91),
        createSong("36", "Versace on the Floor", "Bruno Mars", "R&B", 89),
        createSong("37", "Love on Top", "Beyoncé", "R&B", 93),
        createSong("38", "That's What I Like", "Bruno Mars", "R&B", 92),
        createSong("39", "Slow Hands", "Niall Horan", "R&B", 87),
        createSong("40", "Location", "Khalid", "R&B", 88)
    )

    private fun getCountrySongs(): List<Song> = listOf(
        createSong("41", "Old Town Road", "Lil Nas X ft. Billy Ray Cyrus", "Country", 97),
        createSong("42", "The Dance", "Garth Brooks", "Country", 90),
        createSong("43", "Before He Cheats", "Carrie Underwood", "Country", 91),
        createSong("44", "Friends in Low Places", "Garth Brooks", "Country", 89),
        createSong("45", "Need You Now", "Lady A", "Country", 92),
        createSong("46", "Die a Happy Man", "Thomas Rhett", "Country", 88),
        createSong("47", "Body Like a Back Road", "Sam Hunt", "Country", 87),
        createSong("48", "Tennessee Whiskey", "Chris Stapleton", "Country", 93)
    )

    private fun getJazzSongs(): List<Song> = listOf(
        createSong("49", "Take Five", "Dave Brubeck", "Jazz", 95),
        createSong("50", "So What", "Miles Davis", "Jazz", 96),
        createSong("51", "Fly Me to the Moon", "Frank Sinatra", "Jazz", 94),
        createSong("52", "Autumn Leaves", "Cannonball Adderley", "Jazz", 90),
        createSong("53", "My Favorite Things", "John Coltrane", "Jazz", 92),
        createSong("54", "Round Midnight", "Thelonious Monk", "Jazz", 91),
        createSong("55", "All of Me", "Billie Holiday", "Jazz", 93),
        createSong("56", "What a Wonderful World", "Louis Armstrong", "Jazz", 97)
    )

    private fun getPopularSongs(): List<Song> {
        return getRockSongs().take(2) + 
               getPopSongs().take(2) + 
               getHipHopSongs().take(2) +
               getElectronicSongs().take(2) +
               getRnBSongs().take(2)
    }

    private fun createSong(id: String, title: String, artist: String, genre: String, popularity: Int): Song {
        return Song(
            id = id,
            title = title,
            artist = artist,
            album = "Best of $artist",
            albumCover = "https://example.com/cover$id.jpg",
            previewUrl = "https://example.com/preview$id.mp3",
            duration = 180 + id.toInt() % 120,
            genre = genre,
            releaseDate = "202${id.toInt() % 3}-${(id.toInt() % 12) + 1}-${(id.toInt() % 28) + 1}",
            popularity = popularity,
            externalUrl = "https://example.com/track$id",
            isLiked = false,
            playlistName = null
        )
    }

    fun resetUserPreferences(context: Context) {
        if (!isInitialized) initialize(context)
        
        getAllGenres().forEach { genre ->
            preferenceRepository?.setAlgorithmPreference("genre_$genre", "0.0")
        }
        preferenceRepository?.setAlgorithmPreference("interaction_count", "0")
        recommendationHistory.clear()
    }

    fun getTopGenres(context: Context, count: Int = 3): List<Pair<String, Float>> {
        val tasteProfile = getUserTasteProfile(context)
        return tasteProfile.entries
            .sortedByDescending { it.value }
            .take(count)
            .map { it.key to it.value }
    }
}