package com.example.swipfy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swipfy.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // TODO: Implement profile functionality
        binding.textView.text = "Profile will be implemented here"
    }
}