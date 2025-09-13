package com.example.swipfy

import com.example.swipfy.data.models.Song
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swipfy.databinding.ItemCardBinding

class SongAdapter(
    private var songs: List<Song>,  // FIXED: Added Song type
    private val onAction: (Song, String) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(private val binding: ItemCardBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(song: Song) {
            binding.tvSongTitle.text = song.title
            binding.tvArtistName.text = song.artist
            binding.tvAlbumName.text = song.album
            
            Glide.with(binding.root.context)
                .load(song.albumCover)
                .placeholder(R.drawable.ic_music_note)
                .into(binding.ivAlbumCover)

            // Set click listeners
            binding.root.setOnClickListener {
                // Handle card click if needed
            }

            // You can add swipe detection here if needed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    fun updateSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }
}