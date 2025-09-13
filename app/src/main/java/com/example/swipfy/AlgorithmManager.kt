package com.example.swipfy

import android.content.Context
import com.example.swipfy.data.models.Song
import com.example.swipfy.data.repository.PreferenceRepository  // ADD THIS IMPORT

object AlgorithmManager {

    private var preferenceRepository: PreferenceRepository? = null
    private var isInitialized = false

    fun initialize(context: Context) {
        if (!isInitialized) {
            preferenceRepository = PreferenceRepository(context)
            isInitialized = true
        }
    }

    fun trackPreference(song: Song, action: String) {
        if (!isInitialized) return
        
        // Track user preferences for recommendations
        val weight = when (action) {
            "like" -> 1.0f
            "dislike" -> -0.5f
            "add_to_playlist" -> 0.8f
            else -> 0.0f
        }

        // Update user profile with this preference
        updateUserProfile(song, weight)
        
        // Retrain recommendation model if needed
        if (shouldRetrainModel()) {
            retrainModel()
        }
    }

    private fun updateUserProfile(song: Song, weight: Float) {
        // Update user's musical preferences based on this interaction
        val genre = detectGenre(song)
        val currentWeight = preferenceRepository?.getAlgorithmPreference("genre_$genre", "0.0")?.toFloat() ?: 0.0f
        val newWeight = currentWeight + weight
        
        preferenceRepository?.setAlgorithmPreference("genre_$genre", newWeight.toString())
        preferenceRepository?.setAlgorithmPreference("last_interaction_${song.id}", System.currentTimeMillis().toString())
    }

    private fun detectGenre(song: Song): String {
        // Simple genre detection based on song metadata
        return when {
            song.genre.contains("rock", true) -> "rock"
            song.genre.contains("pop", true) -> "pop"
            song.genre.contains("hip", true) || song.genre.contains("rap", true) -> "hiphop"
            song.genre.contains("jazz", true) -> "jazz"
            song.genre.contains("classical", true) -> "classical"
            song.genre.contains("electronic", true) || song.genre.contains("dance", true) -> "electronic"
            song.genre.contains("r&b", true) || song.genre.contains("soul", true) -> "rnb"
            else -> "unknown"
        }
    }

    fun getRecommendations(context: Context): List<Song> {
        if (!isInitialized) initialize(context)
        
        // For now, return empty list - you would implement actual recommendation logic here
        // This could be based on user's genre preferences, liked songs, etc.
        return emptyList()
    }

    private fun shouldRetrainModel(): Boolean {
        // Simple implementation: retrain after 10 interactions
        val interactionCount = preferenceRepository?.getAlgorithmPreference("interaction_count", "0")?.toInt() ?: 0
        return interactionCount >= 10
    }

    private fun retrainModel() {
        // Reset interaction count after retraining
        preferenceRepository?.setAlgorithmPreference("interaction_count", "0")
        // Here you would implement actual model retraining logic
    }

    fun getUserTasteProfile(context: Context): Map<String, Float> {
        if (!isInitialized) initialize(context)
        
        val tasteProfile = mutableMapOf<String, Float>()
        val genres = listOf("rock", "pop", "hiphop", "jazz", "classical", "electronic", "rnb")
        
        genres.forEach { genre ->
            val weight = preferenceRepository?.getAlgorithmPreference("genre_$genre", "0.0")?.toFloat() ?: 0.0f
            tasteProfile[genre] = weight
        }
        
        return tasteProfile
    }

    fun resetUserPreferences(context: Context) {
        if (!isInitialized) initialize(context)
        
        // Clear all algorithm preferences
        val genres = listOf("rock", "pop", "hiphop", "jazz", "classical", "electronic", "rnb")
        genres.forEach { genre ->
            preferenceRepository?.setAlgorithmPreference("genre_$genre", "0.0")
        }
        preferenceRepository?.setAlgorithmPreference("interaction_count", "0")
    }

    fun getTopGenres(context: Context, count: Int = 3): List<Pair<String, Float>> {
        val tasteProfile = getUserTasteProfile(context)
        return tasteProfile.entries
            .sortedByDescending { it.value }
            .take(count)
            .map { it.key to it.value }
    }

    fun getRecommendationScore(song: Song, context: Context): Float {
        if (!isInitialized) initialize(context)
        
        val genre = detectGenre(song)
        val genreWeight = preferenceRepository?.getAlgorithmPreference("genre_$genre", "0.0")?.toFloat() ?: 0.0f
        
        // Simple scoring: base score + genre preference weight
        var score = song.popularity / 100.0f // Normalize popularity
        score += genreWeight * 0.1f // Add genre preference influence
        
        return score.coerceIn(0.0f, 1.0f)
    }
}