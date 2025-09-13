package com.example.swipfy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipfy.data.models.Song
import com.example.swipfy.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recommendedAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupListeners()
        setupRecommendedList()
        loadRecommendedContent()
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_discover -> {
                    // Already on discover page
                    true
                }
                R.id.navigation_swipe -> {
                    startActivity(Intent(this, SwipeActivity::class.java))
                    true
                }
                R.id.navigation_playlists -> {
                    startActivity(Intent(this, PlaylistActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.navigation_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupListeners() {
        binding.btnStartSwiping.setOnClickListener {
            startActivity(Intent(this, SwipeActivity::class.java))
        }

        binding.ivSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun setupRecommendedList() {
        recommendedAdapter = SongAdapter(emptyList()) { song, action ->
            when (action) {
                "play" -> playSong(song)
                "like" -> handleLike(song)
                "add_to_playlist" -> addToPlaylist(song)
            }
        }

        binding.recommendedRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recommendedAdapter
        }
    }

    private fun loadRecommendedContent() {
        val recommendations = AlgorithmManager.getRecommendations(this)
        recommendedAdapter.updateSongs(recommendations)
        
        if (recommendations.isEmpty()) {
            binding.recommendedTitle.text = "Start swiping to get recommendations!"
        } else {
            binding.recommendedTitle.text = "Recommended for You"
        }
    }

    private fun playSong(song: Song) {
        // Implement song playback
        val intent = Intent(this, SwipeActivity::class.java)
        intent.putExtra("search_query", "${song.artist} ${song.title}")
        startActivity(intent)
    }

    private fun handleLike(song: Song) {
        lifecycleScope.launch {
            (application as SwipfyApp).musicRepository.addToLikedSongs(song)
            AlgorithmManager.trackPreference(song, "like")
            loadRecommendedContent() // Refresh recommendations
        }
    }

    private fun addToPlaylist(song: Song) {
        lifecycleScope.launch {
            PlaylistDialog.show(this@MainActivity, song) { playlistName ->
                lifecycleScope.launch {  // FIXED: Wrap the inner call in coroutine too
                    (application as SwipfyApp).musicRepository.addToPlaylist(song, playlistName)
                    // Show confirmation toast
                    android.widget.Toast.makeText(
                        this@MainActivity,
                        "Added to $playlistName",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}