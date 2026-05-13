package com.example.nammarastereporter.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nammarastereporter.MainActivity
import com.example.nammarastereporter.data.db.AppDatabase
import com.example.nammarastereporter.databinding.ActivityLoginBinding
import com.example.nammarastereporter.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if already logged in
        val prefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(Constants.KEY_LOGGED_IN, false)) {
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

<<<<<<< HEAD
            val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

=======
            if (username.isEmpty() || password.isEmpty()) {
                if (username.isEmpty()) binding.tilUsername.error = "Username is required"
                if (password.isEmpty()) binding.tilPassword.error = "Password is required"
                return@setOnClickListener
            }
>>>>>>> 2f37900 (Save local changes)

            // Hardcoded check
            if (username == "user" && password == "1234") {
                val prefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
                prefs.edit().putBoolean(Constants.KEY_LOGGED_IN, true).apply()
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                navigateToMain()
                return@setOnClickListener
            }

            // Database check
            verifyLoginInDatabase(username, password)
        }


    }

    private fun verifyLoginInDatabase(username: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@LoginActivity)
            val user = db.userDao().getUser(username)

            withContext(Dispatchers.Main) {
                if (user != null && user.password == password) {
                    val prefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
                    prefs.edit().putBoolean(Constants.KEY_LOGGED_IN, true).apply()
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    binding.tilPassword.error = "Invalid username or password"
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
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
