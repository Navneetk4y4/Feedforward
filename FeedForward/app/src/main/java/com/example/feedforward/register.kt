package com.example.feedforward

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

class register : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        username = findViewById(R.id.name)
        email = findViewById(R.id.gmail)
        phone = findViewById(R.id.phone)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.conf_password)
        registerButton = findViewById(R.id.register)

        registerButton.setOnClickListener {
            handleRegister()
        }
    }

    private fun handleRegister() {
        val userName = username.text.toString().trim()
        val userEmail = email.text.toString().trim()
        val userPhone = phone.text.toString().trim()
        val userPassword = password.text.toString().trim()
        val confirmPass = confirmPassword.text.toString().trim()

        // Validation Checks
        if (userName.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (!userPhone.matches(Regex("^[0-9]{10}$"))) {
            Toast.makeText(this, "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidPassword(userPassword)) {
            Toast.makeText(
                this,
                "Password must be at least 8 characters, contain an uppercase letter, a number, and a special character",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (userPassword != confirmPass) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            return
        }

        val salt = generateSalt()
        val hashedPassword = hashPassword(userPassword, salt)

        saveUser(userName, userEmail, userPhone, hashedPassword, salt)

        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, login_page::class.java))
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$"
        return password.matches(Regex(passwordPattern))
    }

    private fun generateSalt(): String {
        val random = SecureRandom()
        val saltBytes = ByteArray(8)
        random.nextBytes(saltBytes)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(saltBytes).substring(0, 8)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    private fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest((salt + password).toByteArray(StandardCharsets.UTF_8))
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(hashBytes)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    private fun saveUser(username: String, email: String, phone: String, hashedPassword: String, salt: String) {
        val file = File(filesDir, "users.json")
        val userArray = if (file.exists()) {
            JSONArray(file.readText())
        } else {
            JSONArray()
        }

        for (i in 0 until userArray.length()) {
            val userObj = userArray.getJSONObject(i)
            if (userObj.getString("username") == username) {
                Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val newUser = JSONObject()
        newUser.put("username", username)
        newUser.put("password", hashedPassword)
        newUser.put("salt", salt)
        newUser.put("email", email)
        newUser.put("phone", phone)

        userArray.put(newUser)

        FileWriter(file).use { writer ->
            writer.write(userArray.toString())
        }
    }
}
