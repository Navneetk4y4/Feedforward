package com.example.feedforward

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.feedforward.databinding.ActivityHomePageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class home_page : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var profileImage: ImageView
    private var username: String? = null // Store the logged-in username

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)

        supportActionBar?.apply {
            title = "FeedForward"
            setDisplayShowTitleEnabled(true)
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home_page)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Get the username from intent
        username = intent.getStringExtra("USERNAME")

        // Set up Profile Image
        profileImage = findViewById(R.id.profile_image)

        // Generate a stable profile image for each user
        username?.let {
            val imageUrl = "https://robohash.org/${hashUsername(it)}.png?size=200x200"
            Glide.with(this)
                .load(imageUrl)
                .circleCrop()
                .into(profileImage)
        }

        // Set Click Listener for Profile Dropdown
        profileImage.setOnClickListener { showProfileMenu(it) }
    }

    // Hash username to generate a consistent profile image
    private fun hashUsername(username: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(username.toByteArray(StandardCharsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }.take(8) // Shorten hash for RoboHash
    }

    // Show Popup Menu
    private fun showProfileMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.profile_menu, popup.menu)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.action_logout -> {
                    Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, login_page::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}
