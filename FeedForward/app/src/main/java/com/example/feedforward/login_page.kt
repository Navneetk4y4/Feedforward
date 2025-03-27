package com.example.feedforward

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64

class login_page : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var registerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        username = findViewById(R.id.editTextFullName)
        password = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        registerText = findViewById(R.id.textRegister)

        loginButton.setOnClickListener {
            handleLogin()
        }

        registerText.setOnClickListener {
            startActivity(Intent(this, register::class.java))
        }
    }

    private fun handleLogin() {
        val userName = username.text.toString().trim()
        val userPassword = password.text.toString().trim()

        if (userName.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(filesDir, "users.json")
        if (!file.exists()) {
            Toast.makeText(this, "No users found. Please register first!", Toast.LENGTH_SHORT).show()
            return
        }

        val userArray = JSONArray(file.readText())

        for (i in 0 until userArray.length()) {
            val userObj = userArray.getJSONObject(i)
            if (userObj.getString("username") == userName) {
                val storedPassword = userObj.getString("password")
                val storedSalt = userObj.getString("salt")

                if (verifyPassword(userPassword, storedPassword, storedSalt)) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, home_page::class.java)
                    intent.putExtra("USERNAME", userName) // Pass username
                    startActivity(intent)
                    finish()
                    return
                } else {
                    Toast.makeText(this, "Invalid Password!", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }

        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show()
    }

    private fun verifyPassword(inputPassword: String, storedPassword: String, salt: String): Boolean {
        val hashBytes = MessageDigest.getInstance("SHA-256").digest((salt + inputPassword).toByteArray(StandardCharsets.UTF_8))
        val hashedInput = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(hashBytes)
        } else {
            return false
        }
        return hashedInput == storedPassword
    }
}
