package com.example.feedforward

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class login_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)

        val login = findViewById<Button>(R.id.button)
        val reg = findViewById<TextView>(R.id.textView2)
        val username = findViewById<EditText>(R.id.editTextText2)
        val password = findViewById<EditText>(R.id.editTextTextPassword)


        login.setOnClickListener{

            val login = Intent(this, home_page::class.java)

            startActivity(login)

        }


        reg.setOnClickListener{

            val reg = Intent(this, register::class.java)

            startActivity(reg)


        }
    }
}