package com.example.nammarastereporter.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nammarastereporter.MainActivity
import com.example.nammarastereporter.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val PREFS_NAME = "namma_raste_prefs"
        private const val KEY_LOGGED_IN = "is_logged_in"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if already logged in
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_LOGGED_IN, false)) {
            navigateToMain()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Clear previous errors
            binding.tilUsername.error = null
            binding.tilPassword.error = null

            val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


            // Hardcoded check
            if (username == "user" && password == "1234") {
                // Save login state
                prefs.edit().putBoolean(KEY_LOGGED_IN, true).apply()

                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                navigateToMain()
            } else if (username.isEmpty() || password.isEmpty()) {
                if (username.isEmpty()) binding.tilUsername.error = "Username is required"
                if (password.isEmpty()) binding.tilPassword.error = "Password is required"
            } else {
                binding.tilPassword.error = "Invalid username or password"
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
