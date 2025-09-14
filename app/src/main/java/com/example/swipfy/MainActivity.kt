package com.example.swipfy

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.example.swipfy.data.models.Song
import com.example.swipfy.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recommendedAdapter: SongAdapter
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupListeners()
        setupRecommendedList()
        setupSwipeRefresh()
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
                    startActivity(Intent(this@MainActivity, SwipeActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    true
                }
                R.id.navigation_playlists -> {
                    // Start playlist activity (create this activity later)
                    showSnackbar("Playlists coming soon!")
                    true
                }
                R.id.navigation_profile -> {
                    // Start profile activity (create this activity later)
                    showSnackbar("Profile coming soon!")
                    true
                }
                else -> false
            }
        }
    }

    private fun setupListeners() {
        binding.btnStartSwiping.setOnClickListener {
            animateButton(binding.btnStartSwiping)
            startActivity(Intent(this, SwipeActivity::class.java))
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
        }

        // Genre filters
        binding.chipRock.setOnClickListener { filterByGenre("Rock") }
        binding.chipPop.setOnClickListener { filterByGenre("Pop") }
        binding.chipHipHop.setOnClickListener { filterByGenre("HipHop") }
        binding.chipElectronic.setOnClickListener { filterByGenre("Electronic") }

        // View all recommendations
        binding.tvViewAll.setOnClickListener {
            loadRecommendedContent()
            animateButton(binding.tvViewAll)
        }
    }

    private fun setupRecommendedList() {
        recommendedAdapter = SongAdapter(emptyList()) { song, action ->
            when (action) {
                "play" -> playSong(song)
                "like" -> handleLike(song)
                "add_to_playlist" -> addToPlaylist(song)
                "share" -> shareSong(song)
            }
        }

        binding.recommendedRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, 
                LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.accent)
            setProgressBackgroundColorSchemeResource(R.color.card_background)
            setOnRefreshListener {
                loadRecommendedContent()
            }
        }
    }

    private fun loadRecommendedContent() {
        if (isLoading) return
        isLoading = true

        binding.progressBar.isVisible = true
        binding.tvEmptyState.isVisible = false
        binding.swipeRefreshLayout.isRefreshing = true

        lifecycleScope.launch {
            try {
                delay(1000) // Simulate loading

                // For now, use dummy data - replace with your actual API calls
                // Replace lines 124-126 with proper Song constructors:
                val recommendations = listOf(
                    Song("1", "Song 1", "Artist 1", "Album 1", "rock", "2023", 80, "https://example.com", 180, "https://example.com/cover.jpg", "https://example.com/preview.mp3"),
                    Song("2", "Song 2", "Artist 2", "Album 2", "pop", "2023", 85, "https://example.com", 200, "https://example.com/cover.jpg", "https://example.com/preview.mp3"),
                    Song("3", "Song 3", "Artist 3", "Album 3", "hiphop", "2023", 90, "https://example.com", 220, "https://example.com/cover.jpg", "https://example.com/preview.mp3")
                )
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    binding.swipeRefreshLayout.isRefreshing = false
                    
                    if (recommendations.isEmpty()) {
                        showEmptyState()
                    } else {
                        showRecommendations(recommendations)
                    }
                    
                    isLoading = false
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    binding.swipeRefreshLayout.isRefreshing = false
                    showErrorState()
                    isLoading = false
                }
            }
        }
    }

    private fun showRecommendations(recommendations: List<Song>) {
        recommendedAdapter.updateSongs(recommendations)
        binding.recommendedSection.isVisible = true
        binding.tvEmptyState.isVisible = false
        binding.emptyStateAnimation.isVisible = false

        // Show personalized message
        binding.recommendedSubtitle.text = "Popular tracks you might enjoy"
        binding.recommendedSubtitle.isVisible = true

        // Show new user encouragement
        binding.tvNewUserTip.isVisible = true
    }

    private fun showEmptyState() {
        binding.recommendedSection.isVisible = false
        binding.tvEmptyState.isVisible = true
        binding.emptyStateAnimation.isVisible = true
        
        binding.tvEmptyState.text = "🎵 Start swiping to discover your music taste!"
        
        binding.emptyStateAnimation.apply {
            setAnimation(R.raw.music_discovery)
            playAnimation()
            repeatCount = LottieDrawable.INFINITE
        }
    }

    private fun showErrorState() {
        binding.tvEmptyState.isVisible = true
        binding.emptyStateAnimation.isVisible = true
        binding.tvEmptyState.text = "⚠️ Couldn't load recommendations"
        binding.emptyStateAnimation.setAnimation(R.raw.error_state)
    }

    private fun filterByGenre(genre: String) {
        showSnackbar("Filtering by $genre")
        // Implement genre filtering logic here
    }

    private fun playSong(song: Song) {
        startActivity(Intent(this, SwipeActivity::class.java).apply {
            putExtra("search_query", "${song.artist} ${song.title}")
        })
    }

    private fun handleLike(song: Song) {
        showSnackbar("❤️ Liked ${song.title}")
    }

    private fun addToPlaylist(song: Song) {
        showSnackbar("📚 Added ${song.title} to playlist")
    }

    private fun shareSong(song: Song) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out: ${song.title} by ${song.artist}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share song"))
    }

    private fun animateButton(view: View) {
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}