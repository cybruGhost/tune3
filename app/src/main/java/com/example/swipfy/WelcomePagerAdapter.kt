package com.example.swipfy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swipfy.databinding.ItemWelcomeBinding

class WelcomePagerAdapter(
    private val context: android.content.Context
) : RecyclerView.Adapter<WelcomePagerAdapter.WelcomeViewHolder>() {

    private val pages = listOf(
        WelcomePage(
            R.drawable.ic_discover,
            "Discover New Music",
            "Swipe through songs and discover music you'll love"
        ),
        WelcomePage(
            R.drawable.ic_like,
            "Personalized Recommendations",
            "Our algorithm learns your taste and suggests perfect matches"
        ),
        WelcomePage(
            R.drawable.ic_playlist,
            "Create Playlists",
            "Save your favorite songs and build amazing playlists"
        )
    )

    inner class WelcomeViewHolder(private val binding: ItemWelcomeBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        fun bind(page: WelcomePage) {
            binding.ivIcon.setImageResource(page.iconRes)
            binding.tvTitle.text = page.title
            binding.tvDescription.text = page.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeViewHolder {
        val binding = ItemWelcomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WelcomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WelcomeViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size

    data class WelcomePage(
        val iconRes: Int,
        val title: String,
        val description: String
    )
}