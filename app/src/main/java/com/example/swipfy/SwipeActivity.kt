package com.example.swipfy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.swipfy.data.models.Song
import com.example.swipfy.databinding.ActivitySwipeBinding
import kotlinx.coroutines.launch
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.math.abs

class SwipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySwipeBinding
    private lateinit var adapter: SongAdapter
    private val songs = mutableListOf<Song>()
    private var player: ExoPlayer? = null
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupListeners()
        setupSearch()

        val query = intent.getStringExtra("search_query") ?: "popular"
        fetchSongs(query)
    }

    private fun setupViewPager() {
        adapter = SongAdapter(songs) { song, action ->
            when (action) {
                "like" -> handleLike(song)
                "dislike" -> handleDislike(song)
                "add_to_playlist" -> addToPlaylist(song)
                "lyrics" -> showLyrics(song)
                "artist" -> showArtistInfo(song.artist)
            }
        }

        binding.viewPager.apply {
            this.adapter = this@SwipeActivity.adapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 3
            
            setPageTransformer { page, position ->
                val scale = 1 - (0.1f * abs(position))
                page.scaleX = scale
                page.scaleY = scale
            }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentPosition = position
                    playPreviewAt(position)
                }
            })
        }
    }

    private fun setupListeners() {
        binding.fabLike.setOnClickListener {
            if (songs.isNotEmpty()) {
                val currentSong = songs.getOrNull(currentPosition)
                currentSong?.let { handleLike(it) }
                swipeToNext()
            }
        }

        binding.fabDislike.setOnClickListener {
            if (songs.isNotEmpty()) {
                val currentSong = songs.getOrNull(currentPosition)
                currentSong?.let { handleDislike(it) }
                swipeToNext()
            }
        }

        binding.fabAddToPlaylist.setOnClickListener {
            if (songs.isNotEmpty()) {
                val currentSong = songs.getOrNull(currentPosition)
                currentSong?.let { addToPlaylist(it) }
            }
        }

        binding.fabLyrics.setOnClickListener {
            if (songs.isNotEmpty()) {
                val currentSong = songs.getOrNull(currentPosition)
                currentSong?.let { showLyrics(it) }
            }
        }

        binding.fabPlayPause.setOnClickListener {
            togglePlayback()
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                fetchSongs(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })
    }

    private fun swipeToNext() {
        if (songs.isEmpty()) return
        
        val nextPosition = (currentPosition + 1) % songs.size
        binding.viewPager.setCurrentItem(nextPosition, true)
        
        val slideAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.viewPager.startAnimation(slideAnimation)
    }

    private fun fetchSongs(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvEmptyState.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val tracks = MusicApi.searchTracks(query, 20)
                songs.clear()
                songs.addAll(tracks)
                
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    if (songs.isEmpty()) {
                        binding.tvEmptyState.visibility = View.VISIBLE
                        binding.tvEmptyState.text = "No songs found for \"$query\""
                    } else {
                        adapter.notifyDataSetChanged()
                        playPreviewAt(0)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.VISIBLE
                    binding.tvEmptyState.text = "Error loading songs. Please try again."
                    Log.e("SwipeActivity", "Error fetching songs", e)
                }
            }
        }
    }

    private fun handleLike(song: Song) {
        lifecycleScope.launch {
            (application as SwipfyApp).musicRepository.addToLikedSongs(song)
            AlgorithmManager.trackPreference(song, "like")
            showToast("Added to liked songs: ${song.title}")
        }
    }

    private fun handleDislike(song: Song) {
        AlgorithmManager.trackPreference(song, "dislike")
        showToast("Skipped: ${song.title}")
    }

    private fun addToPlaylist(song: Song) {
        lifecycleScope.launch {
            PlaylistDialog.show(this@SwipeActivity, song) { playlistName ->
                lifecycleScope.launch {  // FIXED: Wrap the inner call in coroutine too
                    (application as SwipfyApp).musicRepository.addToPlaylist(song, playlistName)
                    showToast("Added to $playlistName: ${song.title}")
                }
            }
        }
    }

    private fun showLyrics(song: Song) {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val lyrics = MusicApi.getLyrics(song.artist, song.title)
                
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    MaterialAlertDialogBuilder(this@SwipeActivity)
                        .setTitle("${song.title} - ${song.artist}")
                        .setMessage(lyrics)
                        .setPositiveButton("Close", null)
                        .show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    showToast("Could not load lyrics: ${e.message}")
                }
            }
        }
    }

    private fun showArtistInfo(artistName: String) {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val artistInfo = MusicApi.getArtistInfo(artistName)
                val name = artistInfo.getString("name")
                val picture = artistInfo.getString("picture_medium")
                val nbFan = artistInfo.getInt("nb_fan")
                val nbAlbum = artistInfo.getInt("nb_album")
                
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    MaterialAlertDialogBuilder(this@SwipeActivity)
                        .setTitle(name)
                        .setMessage("Fans: $nbFan\nAlbums: $nbAlbum")
                        .setPositiveButton("Close", null)
                        .show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    showToast("Could not load artist info: ${e.message}")
                }
            }
        }
    }

    private fun togglePlayback() {
        player?.let {
            if (it.isPlaying) {
                it.pause()
                binding.fabPlayPause.setImageResource(R.drawable.ic_play)
            } else {
                it.play()
                binding.fabPlayPause.setImageResource(R.drawable.ic_pause)
            }
        }
    }

    private fun playPreviewAt(position: Int) {
        if (position !in songs.indices) return
        
        player?.release()
        currentPosition = position

        val song = songs[position]
        try {
            player = ExoPlayer.Builder(this).build().apply {
                setMediaItem(MediaItem.fromUri(song.previewUrl))
                prepare()
                play()
            }

            binding.fabPlayPause.setImageResource(R.drawable.ic_pause)
            updateNowPlaying(song)
        } catch (e: Exception) {
            Log.e("SwipeActivity", "Playback error", e)
            binding.fabPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun updateNowPlaying(song: Song) {
        binding.tvSongTitle.text = song.title
        binding.tvArtistName.text = song.artist
        Glide.with(this)
            .load(song.albumCover)
            .placeholder(R.drawable.ic_music_note)
            .into(binding.ivAlbumArt)
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}