package com.fityatra.activities

import ApiClient.apiService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fityatra.R
import com.fityatra.utils.LoginRequest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordLink: TextView
    private lateinit var signupLink: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        // Initialize views
        usernameField = findViewById(R.id.username_field)
        passwordField = findViewById(R.id.password_field)
        loginButton = findViewById(R.id.login_button)
        forgotPasswordLink = findViewById(R.id.forgot_password_link)
        signupLink = findViewById(R.id.signup_link)
        // Set click listener for login button
        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            userLogin(username, password)
        }

        forgotPasswordLink.setOnClickListener {

        }
        signupLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userLogin(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        lifecycleScope.launch {
            try {
                val response = apiService.loginUser(loginRequest)
                if (response.isSuccessful) {

                    val sharedPreferences =
                        getSharedPreferences("Token", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("token", response.body()?.token)
                        apply()
                    }
                    navigateToMainScreen()

                } else {
                    Log.d("error ok", response.toString())
                }
            } catch (e: Exception) {
                Log.d("error ", e.toString())
            }
        }

    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
