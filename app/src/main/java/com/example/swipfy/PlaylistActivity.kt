package com.example.swipfy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swipfy.data.models.Song
import com.example.swipfy.databinding.ActivityPlaylistBinding
import com.example.swipfy.databinding.ItemPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var playlistAdapter: PlaylistAdapter
    private val playlists = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadPlaylists()
        setupListeners()
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter(playlists) { playlistName ->
            showPlaylistSongs(playlistName)
        }
        
        binding.playlistRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PlaylistActivity)
            adapter = playlistAdapter
        }
    }

    private fun loadPlaylists() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userPlaylists = (application as SwipfyApp).musicRepository.getUserPlaylists()
                withContext(Dispatchers.Main) {
                    playlists.clear()
                    playlists.addAll(userPlaylists)
                    playlistAdapter.notifyDataSetChanged()
                    
                    if (playlists.isEmpty()) {
                        binding.emptyState.visibility = View.VISIBLE
                    } else {
                        binding.emptyState.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PlaylistActivity, "Error loading playlists", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.fabCreatePlaylist.setOnClickListener {
            showCreatePlaylistDialog()
        }
    }

    private fun showCreatePlaylistDialog() {
        val editText = EditText(this)
        editText.hint = "Playlist name"
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Create New Playlist")
            .setView(editText)
            .setPositiveButton("Create") { dialog, which ->
                val playlistName = editText.text.toString().trim()
                if (playlistName.isNotEmpty()) {
                    createPlaylist(playlistName)
                } else {
                    Toast.makeText(this, "Please enter a playlist name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun createPlaylist(playlistName: String) {
        if (playlists.contains(playlistName)) {
            Toast.makeText(this, "Playlist already exists", Toast.LENGTH_SHORT).show()
            return
        }

        playlists.add(playlistName)
        playlistAdapter.notifyItemInserted(playlists.size - 1)
        binding.emptyState.visibility = View.GONE
        
        Toast.makeText(this, "Playlist created: $playlistName", Toast.LENGTH_SHORT).show()
    }

    private fun showPlaylistSongs(playlistName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val songs = (application as SwipfyApp).musicRepository.getPlaylistSongs(playlistName)
                withContext(Dispatchers.Main) {
                    if (songs.isEmpty()) {
                        Toast.makeText(this@PlaylistActivity, "No songs in $playlistName", Toast.LENGTH_SHORT).show()
                    } else {
                        showPlaylistContentDialog(playlistName, songs)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PlaylistActivity, "Error loading playlist", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showPlaylistContentDialog(playlistName: String, songs: List<Song>) {
        val songTitles = songs.joinToString("\n") { "• ${it.title} - ${it.artist}" }
        
        MaterialAlertDialogBuilder(this)
            .setTitle("$playlistName (${songs.size} songs)")
            .setMessage(songTitles)
            .setPositiveButton("OK", null)
            .setNeutralButton("Delete Playlist") { dialog, which ->
                deletePlaylist(playlistName)
            }
            .show()
    }

    private fun deletePlaylist(playlistName: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Playlist")
            .setMessage("Are you sure you want to delete '$playlistName'?")
            .setPositiveButton("Delete") { dialog, which ->
                val index = playlists.indexOf(playlistName)
                if (index != -1) {
                    playlists.removeAt(index)
                    playlistAdapter.notifyItemRemoved(index)
                    
                    if (playlists.isEmpty()) {
                        binding.emptyState.visibility = View.VISIBLE
                    }
                    
                    Toast.makeText(this, "Playlist deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    class PlaylistAdapter(
        private val playlists: List<String>,
        private val onPlaylistClick: (String) -> Unit
    ) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

        inner class PlaylistViewHolder(private val binding: ItemPlaylistBinding) : 
            RecyclerView.ViewHolder(binding.root) {
            
            fun bind(playlistName: String) {
                binding.tvPlaylistName.text = playlistName
                
                // Load song count for this playlist
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val app = binding.root.context.applicationContext as SwipfyApp
                        val songs = app.musicRepository.getPlaylistSongs(playlistName)
                        withContext(Dispatchers.Main) {
                            binding.tvSongCount.text = "${songs.size} songs"
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.tvSongCount.text = "0 songs"
                        }
                    }
                }
                
                binding.root.setOnClickListener {
                    onPlaylistClick(playlistName)
                }
                
                binding.btnDelete.setOnClickListener {
                    // Handle delete here or you can move this to the activity
                    val context = binding.root.context
                    if (context is PlaylistActivity) {
                        context.deletePlaylist(playlistName)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
            val binding = ItemPlaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PlaylistViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
            holder.bind(playlists[position])
        }

        override fun getItemCount(): Int = playlists.size
    }
}