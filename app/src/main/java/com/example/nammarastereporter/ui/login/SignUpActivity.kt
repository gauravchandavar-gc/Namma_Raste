package com.example.nammarastereporter.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nammarastereporter.data.db.AppDatabase
import com.example.nammarastereporter.data.db.UserEntity
import com.example.nammarastereporter.databinding.ActivitySignupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSignup.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            // Reset errors
            binding.tilUsername.error = null
            binding.tilEmail.error = null
            binding.tilPassword.error = null
            binding.tilConfirmPassword.error = null

            when {
                username.isEmpty() -> {
                    binding.tilUsername.error = "Username is required"
                }
                email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.tilEmail.error = "Valid email is required"
                }
                password.length < 4 -> {
                    binding.tilPassword.error = "Password must be at least 4 characters"
                }
                password != confirmPassword -> {
                    binding.tilConfirmPassword.error = "Passwords do not match"
                }
                else -> {
                    registerUserInDatabase(username, email, password)
                }
            }
        }

        binding.btnBackToLogin.setOnClickListener {
            finish()
        }
    }

    private fun registerUserInDatabase(username: String, email: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@SignUpActivity)
            val existingUser = db.userDao().getUser(username)

            withContext(Dispatchers.Main) {
                if (existingUser != null) {
                    binding.tilUsername.error = "Username already exists"
                } else {
                    val newUser = UserEntity(username = username, email = email, password = password)
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.userDao().insertUser(newUser)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignUpActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            finish() // Go back to login
                        }
                    }
                }
            }
        }
    }
}
