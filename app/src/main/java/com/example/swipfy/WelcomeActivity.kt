package com.example.swipfy

import android.content.Intent
import android.os.Bundle
import android.view.View  // ADD THIS IMPORT
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.swipfy.databinding.ActivityWelcomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if ((application as SwipfyApp).preferenceRepository.hasSeenWelcome()) {
            startMainActivity()
            return
        }

        setupViewPager()
        setupListeners()
    }

    private fun setupViewPager() {
        val adapter = WelcomePagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // Just show dots, no text
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.btnNext.text = if (position == 2) "Get Started" else "Next"
                binding.btnSkip.visibility = if (position == 2) View.GONE else View.VISIBLE
            }
        })
    }

    private fun setupListeners() {
        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem < 2) {
                binding.viewPager.currentItem = binding.viewPager.currentItem + 1
            } else {
                markWelcomeSeen()
                startMainActivity()
            }
        }

        binding.btnSkip.setOnClickListener {
            markWelcomeSeen()
            startMainActivity()
        }
    }

    private fun markWelcomeSeen() {
        (application as SwipfyApp).preferenceRepository.setWelcomeSeen(true)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}