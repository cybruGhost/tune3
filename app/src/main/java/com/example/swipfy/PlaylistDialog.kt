package com.example.swipfy

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.swipfy.data.models.Song
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object PlaylistDialog {

    fun show(context: Context, song: Song, onPlaylistSelected: (String) -> Unit) {
        // Default playlists for now
        val playlists = listOf("Favorites", "Recently Played", "My Playlist")
        val playlistNames = playlists.toTypedArray()
        
        MaterialAlertDialogBuilder(context)
            .setTitle("Add to Playlist")
            .setItems(playlistNames) { dialog, which ->
                onPlaylistSelected(playlistNames[which])
            }
            .setPositiveButton("New Playlist") { dialog, which ->
                showCreatePlaylistDialog(context, song, onPlaylistSelected)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showCreatePlaylistDialog(context: Context, song: Song, onPlaylistSelected: (String) -> Unit) {
        val editText = EditText(context)
        editText.hint = "Playlist name"
        
        MaterialAlertDialogBuilder(context)
            .setTitle("Create New Playlist")
            .setView(editText)
            .setPositiveButton("Create") { dialog, which ->
                val playlistName = editText.text.toString().trim()
                if (playlistName.isNotEmpty()) {
                    onPlaylistSelected(playlistName)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}