package com.example.swipfy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.swipfy.data.models.Song
import com.example.swipfy.databinding.ActivitySwipeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlin.math.abs

class SwipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySwipeBinding
    private lateinit var adapter: SongAdapter
    private val songs = mutableListOf<Song>()
    private var player: ExoPlayer? = null
    private var currentPosition = 0
    private var isPlaying = false
    private var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupListeners()
        setupSearch()
        setupPlayer()

        currentQuery = intent.getStringExtra("search_query") ?: "popular"
        fetchSongs(currentQuery)
    }

    private fun setupViewPager() {
        adapter = SongAdapter(songs) { song, action ->
            when (action) {
                "like" -> handleLike(song)
                "dislike" -> handleDislike(song)
                "add_to_playlist" -> addToPlaylist(song)
                "lyrics" -> showLyrics(song)
                "artist" -> showArtistInfo(song.artist)
                "share" -> shareSong(song)
            }
        }

        binding.viewPager.apply {
            this.adapter = this@SwipeActivity.adapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 3
            
            setPageTransformer { page, position ->
                val scale = 1 - (0.1f * abs(position))
                val alpha = 1 - (0.3f * abs(position))
                page.scaleX = scale
                page.scaleY = scale
                page.alpha = alpha
            }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentPosition = position
                    playPreviewAt(position)
                    updateSwipeIndicator()
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        binding.fabPlayPause.hide()
                    } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        binding.fabPlayPause.show()
                    }
                }
            })
        }
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_ENDED -> {
                            binding.fabPlayPause.setImageResource(R.drawable.ic_play)
                            isPlaying = false
                        }
                        Player.STATE_READY -> {
                            binding.fabPlayPause.setImageResource(R.drawable.ic_pause)
                            isPlaying = true
                        }
                    }
                }
            })
        }
    }

private fun setupListeners() {
    binding.fabLike.setOnClickListener {
        animateButton(binding.fabLike)
        if (songs.isNotEmpty()) {
            val currentSong = songs.getOrNull(currentPosition)
            currentSong?.let { 
                handleLike(it)
                binding.feedbackAnimation.setAnimation(R.raw.like_animation)
                binding.feedbackAnimation.repeatCount = 0
                binding.feedbackAnimation.playAnimation()
                binding.feedbackAnimation.isVisible = true
                binding.feedbackAnimation.postDelayed({
                    binding.feedbackAnimation.isVisible = false
                }, binding.feedbackAnimation.duration)
            }
            swipeToNext()
        }
    }

    binding.fabDislike.setOnClickListener {
        animateButton(binding.fabDislike)
        if (songs.isNotEmpty()) {
            val currentSong = songs.getOrNull(currentPosition)
            currentSong?.let { 
                handleDislike(it)
                binding.feedbackAnimation.setAnimation(R.raw.dislike_animation)
                binding.feedbackAnimation.repeatCount = 0
                binding.feedbackAnimation.playAnimation()
                binding.feedbackAnimation.isVisible = true
                binding.feedbackAnimation.postDelayed({
                    binding.feedbackAnimation.isVisible = false
                }, binding.feedbackAnimation.duration)
            }
            swipeToNext()
        }
    }

    binding.fabAddToPlaylist.setOnClickListener {
        animateButton(binding.fabAddToPlaylist)
        if (songs.isNotEmpty()) {
            val currentSong = songs.getOrNull(currentPosition)
            currentSong?.let { addToPlaylist(it) }
        }
    }

    binding.fabLyrics.setOnClickListener {
        animateButton(binding.fabLyrics)
        if (songs.isNotEmpty()) {
            val currentSong = songs.getOrNull(currentPosition)
            currentSong?.let { showLyrics(it) }
        }
    }

    binding.fabShare.setOnClickListener {
        animateButton(binding.fabShare)
        if (songs.isNotEmpty()) {
            val currentSong = songs.getOrNull(currentPosition)
            currentSong?.let { shareSong(it) }
        }
    }

    binding.fabPlayPause.setOnClickListener {
        togglePlayback()
    }

    binding.root.setOnTouchListener(SwipeGestureListener(
        onSwipeLeft = {
            if (songs.isNotEmpty()) {
                val currentSong = songs.getOrNull(currentPosition)
                currentSong?.let { handleDislike(it) }
                swipeToNext()
            }
        },
        onSwipeRight = {
            if (songs.isNotEmpty()) {
                val currentSong = songs.getOrNull(currentPosition)
                currentSong?.let { handleLike(it) }
                swipeToNext()
            }
        },
        onSwipeUp = {
            if (songs.isNotEmpty()) {
                val currentSong = songs.getOrNull(currentPosition)
                currentSong?.let { addToPlaylist(it) }
            }
        },
        onSwipeDown = {
            if (songs.isNotEmpty()) {
                val currentSong = songs.getOrNull(currentPosition)
                currentSong?.let { showLyrics(it) }
            }
        }
    ))
}


    private fun setupSearch() {
        binding.searchView.apply {
            setQuery(currentQuery, false)
            setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    currentQuery = query
                    fetchSongs(query)
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        currentQuery = "popular"
                        fetchSongs(currentQuery)
                    }
                    return false
                }
            })
        }

        binding.btnShuffle.setOnClickListener {
            animateButton(binding.btnShuffle)
            songs.shuffle()
            adapter.notifyDataSetChanged()
            binding.viewPager.setCurrentItem(0, true)
            showSnackbar("Songs shuffled")
        }

        binding.btnFilter.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        val genres = arrayOf("All", "Rock", "Pop", "HipHop", "Electronic", "R&B", "Jazz")
        var selectedGenre = 0

        MaterialAlertDialogBuilder(this)
            .setTitle("Filter by Genre")
            .setSingleChoiceItems(genres, 0) { dialog, which ->
                selectedGenre = which
            }
            .setPositiveButton("Apply") { dialog, which ->
                if (selectedGenre > 0) {
                    currentQuery = genres[selectedGenre].toLowerCase()
                    fetchSongs(currentQuery)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun fetchSongs(query: String) {
        binding.progressBar.isVisible = true
        binding.tvEmptyState.isVisible = false
        binding.viewPager.isVisible = false

        lifecycleScope.launch {
            try {
                val tracks = MusicApi.searchTracks(query, 20)
                songs.clear()
                songs.addAll(tracks)
                
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    if (songs.isEmpty()) {
                        binding.tvEmptyState.isVisible = true
                        binding.tvEmptyState.text = "No songs found for \"$query\""
                        binding.viewPager.isVisible = false
                    } else {
                        binding.viewPager.isVisible = true
                        adapter.notifyDataSetChanged()
                        playPreviewAt(0)
                        updateSwipeIndicator()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    binding.tvEmptyState.isVisible = true
                    binding.tvEmptyState.text = "Error loading songs. Please try again."
                    binding.viewPager.isVisible = false
                    Log.e("SwipeActivity", "Error fetching songs", e)
                }
            }
        }
    }

    private fun handleLike(song: Song) {
        lifecycleScope.launch {
            (application as SwipfyApp).musicRepository.addToLikedSongs(song)
            AlgorithmManager.trackPreference(song, "like")
            showSnackbar("❤️ Added to liked songs: ${song.title}")
        }
    }

    private fun handleDislike(song: Song) {
        AlgorithmManager.trackPreference(song, "dislike")
        showSnackbar("💔 Skipped: ${song.title}")
    }

    private fun addToPlaylist(song: Song) {
        lifecycleScope.launch {
            PlaylistDialog.show(this@SwipeActivity, song) { playlistName ->
                lifecycleScope.launch {
                    (application as SwipfyApp).musicRepository.addToPlaylist(song, playlistName)
                    showSnackbar("📚 Added to $playlistName: ${song.title}")
                }
            }
        }
    }

    private fun shareSong(song: Song) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this song: ${song.title} by ${song.artist}\n${song.externalUrl}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share song via"))
    }

    private fun showLyrics(song: Song) {
        binding.progressBar.isVisible = true
        
        lifecycleScope.launch {
            try {
                val lyrics = MusicApi.getLyrics(song.artist, song.title)
                
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    MaterialAlertDialogBuilder(this@SwipeActivity)
                        .setTitle("${song.title} - ${song.artist}")
                        .setMessage(lyrics)
                        .setPositiveButton("Close", null)
                        .setNeutralButton("Copy") { dialog, which ->
                            copyToClipboard(lyrics)
                            showSnackbar("Lyrics copied to clipboard")
                        }
                        .show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    showSnackbar("Could not load lyrics: ${e.message}")
                }
            }
        }
    }

    private fun showArtistInfo(artistName: String) {
        binding.progressBar.isVisible = true
        
        lifecycleScope.launch {
            try {
                val artistInfo = MusicApi.getArtistInfo(artistName)
                val name = artistInfo.getString("name")
                val picture = artistInfo.getString("picture_medium")
                val nbFan = artistInfo.getInt("nb_fan")
                val nbAlbum = artistInfo.getInt("nb_album")
                
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    MaterialAlertDialogBuilder(this@SwipeActivity)
                        .setTitle(name)
                        .setMessage("🎵 Fans: ${nbFan.formatLargeNumber()}\n💿 Albums: $nbAlbum")
                        .setPositiveButton("Close", null)
                        .setNeutralButton("View More") { dialog, which ->
                            // Could open artist page or search for more songs
                            currentQuery = artistName
                            fetchSongs(currentQuery)
                        }
                        .show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    showSnackbar("Could not load artist info")
                }
            }
        }
    }

    private fun togglePlayback() {
        player?.let {
            if (it.isPlaying) {
                it.pause()
                binding.fabPlayPause.setImageResource(R.drawable.ic_play)
                isPlaying = false
            } else {
                it.play()
                binding.fabPlayPause.setImageResource(R.drawable.ic_pause)
                isPlaying = true
            }
            animateButton(binding.fabPlayPause)
        }
    }

    private fun playPreviewAt(position: Int) {
        if (position !in songs.indices) return
        
        player?.stop()
        currentPosition = position

        val song = songs[position]
        try {
            player?.setMediaItem(MediaItem.fromUri(song.previewUrl))
            player?.prepare()
            player?.play()
            
            binding.fabPlayPause.setImageResource(R.drawable.ic_pause)
            updateNowPlaying(song)
            isPlaying = true
        } catch (e: Exception) {
            Log.e("SwipeActivity", "Playback error", e)
            binding.fabPlayPause.setImageResource(R.drawable.ic_play)
            isPlaying = false
        }
    }

    private fun updateNowPlaying(song: Song) {
        binding.tvSongTitle.text = song.title
        binding.tvArtistName.text = song.artist
        binding.tvAlbumName.text = song.album
        
        Glide.with(this)
            .load(song.albumCover)
            .placeholder(R.drawable.ic_music_note)
            .error(R.drawable.ic_music_note)
            .into(binding.ivAlbumArt)

        // Update progress bar max duration
        binding.progressBarMax.text = formatDuration(song.duration)
    }

    private fun updateSwipeIndicator() {
        binding.tvSwipeIndicator.text = "${currentPosition + 1}/${songs.size}"
    }

    private fun swipeToNext() {
        if (songs.isEmpty()) return
        
        val nextPosition = (currentPosition + 1) % songs.size
        binding.viewPager.setCurrentItem(nextPosition, true)
        
        val slideAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in).apply {
            interpolator = OvershootInterpolator()
            duration = 300
        }
        binding.viewPager.startAnimation(slideAnimation)
    }

    private fun animateButton(view: View) {
        view.animate()
            .scaleX(0.8f)
            .scaleY(0.8f)
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

    private fun showFeedbackAnimation(isLike: Boolean) {
        binding.feedbackAnimation.apply {
            setAnimation(if (isLike) R.raw.like_animation else R.raw.dislike_animation)
            playAnimation()
            isVisible = true
            repeatCount = 0
            postDelayed({ isVisible = false }, 1000)
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .show()
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Lyrics", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%d:%02d", minutes, remainingSeconds)
    }

    private fun Int.formatLargeNumber(): String {
        return when {
            this >= 1_000_000 -> String.format("%.1fM", this / 1_000_000.0)
            this >= 1_000 -> String.format("%.1fK", this / 1_000.0)
            else -> toString()
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (isPlaying) {
            player?.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}

// Swipe Gesture Listener class
class SwipeGestureListener(
    private val onSwipeLeft: () -> Unit = {},
    private val onSwipeRight: () -> Unit = {},
    private val onSwipeUp: () -> Unit = {},
    private val onSwipeDown: () -> Unit = {}
) : View.OnTouchListener {
    
    private var initialX = 0f
    private var initialY = 0f
    private val swipeThreshold = 100

    override fun onTouch(v: View, event: android.view.MotionEvent): Boolean {
        when (event.action) {
            android.view.MotionEvent.ACTION_DOWN -> {
                initialX = event.x
                initialY = event.y
                return true
            }
            android.view.MotionEvent.ACTION_UP -> {
                val finalX = event.x
                val finalY = event.y
                val diffX = finalX - initialX
                val diffY = finalY - initialY

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > swipeThreshold) {
                        if (diffX > 0) onSwipeRight() else onSwipeLeft()
                        return true
                    }
                } else {
                    if (Math.abs(diffY) > swipeThreshold) {
                        if (diffY > 0) onSwipeDown() else onSwipeUp()
                        return true
                    }
                }
            }
        }
        return false
    }
}