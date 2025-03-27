package com.example.feedforward

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Smooth animation for loading bar
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animator.duration = 1000 // 1 second duration
        animator.start()

        // Delay before moving to the next activity
        Handler().postDelayed({
            startActivity(Intent(this@MainActivity, login_page::class.java))
            finish()
        }, 1200) // Slight delay after animation
    }
}