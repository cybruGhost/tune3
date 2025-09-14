package com.example.swipfy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        loadRecommendedContent()
        setupSwipeRefresh()
    }

    private fun setupNavigation() {
        binding.bottomNavigation.apply {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_discover -> {
                        animateTabSelection(binding.navigationDiscover)
                        true
                    }
                    R.id.navigation_swipe -> {
                        startActivity(Intent(this@MainActivity, SwipeActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        true
                    }
                    R.id.navigation_playlists -> {
                        startActivity(Intent(this@MainActivity, PlaylistActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        true
                    }
                    R.id.navigation_profile -> {
                        startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        true
                    }
                    R.id.navigation_search -> {
                        startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        true
                    }
                    else -> false
                }
            }
            selectedItemId = R.id.navigation_discover
        }
    }

    private fun setupListeners() {
        binding.btnStartSwiping.setOnClickListener {
            animateButton(binding.btnStartSwiping)
            startActivity(Intent(this, SwipeActivity::class.java))
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
        }

        binding.ivSearch.setOnClickListener {
            animateButton(binding.ivSearch)
            startActivity(Intent(this, SearchActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Quick genre filters
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
        recommendedAdapter = SongAdapter(emptyList(), showPlayButton = true) { song, action ->
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
            addItemDecoration(ItemSpacingDecoration(16))
        }

        // Add scroll listener for parallax effect
        binding.recommendedRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                updateParallaxEffect()
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.accent, R.color.purple_500, R.color.teal_200)
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
                // Simulate loading for better UX
                delay(800)

                val recommendations = AlgorithmManager.getRecommendations(this@MainActivity)
                
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

        // Show personalized message based on user preferences
        val topGenres = AlgorithmManager.getTopGenres(this, 2)
        if (topGenres.isNotEmpty()) {
            val genreMessage = if (topGenres.size == 1) {
                "Based on your love for ${topGenres[0].first}"
            } else {
                "Based on your taste in ${topGenres[0].first} & ${topGenres[1].first}"
            }
            binding.recommendedSubtitle.text = genreMessage
            binding.recommendedSubtitle.isVisible = true
        } else {
            binding.recommendedSubtitle.text = "Popular tracks you might enjoy"
            binding.recommendedSubtitle.isVisible = true
        }

        // Show new user encouragement if needed
        val interactionCount = getInteractionCount()
        if (interactionCount < 3) {
            binding.tvNewUserTip.isVisible = true
            binding.tvNewUserTip.text = "💡 Swipe more songs to get better recommendations!"
        } else {
            binding.tvNewUserTip.isVisible = false
        }
    }

    private fun showEmptyState() {
        binding.recommendedSection.isVisible = false
        binding.tvEmptyState.isVisible = true
        
        val interactionCount = getInteractionCount()
        binding.tvEmptyState.text = if (interactionCount == 0) {
            "🎵 Start swiping to discover your music taste!"
        } else {
            "✨ Keep swiping to get more recommendations!"
        }

        binding.emptyStateAnimation.apply {
            setAnimation(R.raw.music_discovery)
            playAnimation()
            repeatCount = LottieDrawable.INFINITE
        }
    }

    private fun showErrorState() {
        binding.tvEmptyState.isVisible = true
        binding.tvEmptyState.text = "⚠️ Couldn't load recommendations\nPull down to refresh"
        binding.emptyStateAnimation.setAnimation(R.raw.error_state)
    }

    private fun filterByGenre(genre: String) {
        val filteredSongs = AlgorithmManager.getRecommendationsByGenre(this, genre)
        if (filteredSongs.isNotEmpty()) {
            recommendedAdapter.updateSongs(filteredSongs)
            binding.recommendedSubtitle.text = "Top $genre picks for you"
            showSnackbar("Showing $genre recommendations")
        } else {
            showSnackbar("No $genre songs found")
        }
    }

    private fun playSong(song: Song) {
        val intent = Intent(this, SwipeActivity::class.java).apply {
            putExtra("search_query", "${song.artist} ${song.title}")
            putExtra("auto_play", true)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
    }

    private fun handleLike(song: Song) {
        lifecycleScope.launch {
            (application as SwipfyApp).musicRepository.addToLikedSongs(song)
            AlgorithmManager.trackPreference(song, "like")
            showSnackbar("❤️ Added to liked songs")
            
            // Refresh recommendations after a like
            delay(1000)
            loadRecommendedContent()
        }
    }

    private fun addToPlaylist(song: Song) {
        lifecycleScope.launch {
            PlaylistDialog.show(this@MainActivity, song) { playlistName ->
                lifecycleScope.launch {
                    (application as SwipfyApp).musicRepository.addToPlaylist(song, playlistName)
                    showSnackbar("📚 Added to $playlistName")
                }
            }
        }
    }

    private fun shareSong(song: Song) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this song I found on Swipfy: ${song.title} by ${song.artist}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share this song"))
    }

    private fun updateParallaxEffect() {
        val translationX = binding.recommendedRecyclerView.computeHorizontalScrollOffset() * 0.3f
        binding.recommendedTitle.translationX = -translationX
        binding.recommendedSubtitle.translationX = -translationX
    }

    private fun getInteractionCount(): Int {
        val prefs = getSharedPreferences("swipfy_prefs", MODE_PRIVATE)
        return prefs.getInt("interaction_count", 0)
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

    private fun animateTabSelection(view: View) {
        view.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(200)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .show()
    }

    override fun onResume() {
        super.onResume()
        // Refresh recommendations when returning to the app
        if (!isLoading) {
            loadRecommendedContent()
        }
    }
}

// Item spacing decoration for RecyclerView
class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    // Implementation would go here
}