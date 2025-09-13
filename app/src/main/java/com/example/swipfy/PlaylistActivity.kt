package com.example.swipfy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swipfy.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // TODO: Implement playlist functionality
        binding.textView.text = "Playlists will be implemented here"
    }
}