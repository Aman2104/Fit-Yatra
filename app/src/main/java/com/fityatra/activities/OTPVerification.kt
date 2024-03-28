package com.fityatra.activities

import ApiClient.apiService
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fityatra.R
import com.fityatra.utils.CreateUserRequest
import com.fityatra.utils.VerifyOtpRequest
import kotlinx.coroutines.launch

class OTPVerification : AppCompatActivity() {

    private lateinit var otpDigit1: EditText
    private lateinit var otpDigit2: EditText
    private lateinit var otpDigit3: EditText
    private lateinit var otpDigit4: EditText
    private lateinit var otpDigit5: EditText
    private lateinit var otpDigit6: EditText
    private lateinit var verifyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        supportActionBar?.hide()

        otpDigit1 = findViewById(R.id.otpDigit1)
        otpDigit2 = findViewById(R.id.otpDigit2)
        otpDigit3 = findViewById(R.id.otpDigit3)
        otpDigit4 = findViewById(R.id.otpDigit4)
        otpDigit5 = findViewById(R.id.otpDigit5)
        otpDigit6 = findViewById(R.id.otpDigit6)
        verifyButton = findViewById(R.id.confirm_code_button)
        setupOtpInputs()


        verifyButton.setOnClickListener {
            val email = intent.getStringExtra("email")
            val name = intent.getStringExtra("name")
            val password = intent.getStringExtra("password")
            val image = intent.getStringExtra("image")
            Log.d("email", email.toString())
            val otp =
                "${otpDigit1.text}${otpDigit2.text}${otpDigit3.text}${otpDigit4.text}${otpDigit5.text}${otpDigit6.text}"
            if (email != null) {
                lifecycleScope.launch {
                    try {
                        val verifyOtpRequest = VerifyOtpRequest(email, otp)
                        val response = apiService.verifyOtp(verifyOtpRequest)
                        if (response.isSuccessful) {
                            Log.d("response ", response.toString())
                            if (name != null && password != null && image != null) {
                                Log.d("values: ", name)
                                createUser(name, password, image, email)
                            }

                        } else {
                            // Handle unsuccessful response
                        }
                    } catch (e: Exception) {
                        // Handle exception
                    }
                }

            }
        }
    }

    private fun createUser(name: String, password: String, image: String, email: String) {
        lifecycleScope.launch {
            try {
                val createUserRequest = CreateUserRequest(name, email, password, image)
                val response = apiService.createUser(createUserRequest)
                Log.d("response is ", response.toString())
                navigateToMainScreen()

            } catch (e: Exception) {
                Log.d("response is ", e.toString())
                // Handle exception
            }
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setupOtpInputs() {
        val genericTextWatcher = GenericTextWatcher()

        otpDigit1.addTextChangedListener(genericTextWatcher)
        otpDigit2.addTextChangedListener(genericTextWatcher)
        otpDigit3.addTextChangedListener(genericTextWatcher)
        otpDigit4.addTextChangedListener(genericTextWatcher)
        otpDigit5.addTextChangedListener(genericTextWatcher)
        otpDigit6.addTextChangedListener(genericTextWatcher)
    }

    inner class GenericTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.length == 1) {
                when (s) {
                    otpDigit1.text -> otpDigit2.requestFocus()
                    otpDigit2.text -> otpDigit3.requestFocus()
                    otpDigit3.text -> otpDigit4.requestFocus()
                    otpDigit4.text -> otpDigit5.requestFocus()
                    otpDigit5.text -> otpDigit6.requestFocus()
                }
            }
        }


        override fun afterTextChanged(s: Editable?) {
        }
    }
}
