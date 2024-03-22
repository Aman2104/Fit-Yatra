package com.fityatra.activities

import ApiClient.apiService
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fityatra.R
import com.fityatra.utils.SendOtpRequest
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SignupActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var emailIdField: EditText
    private lateinit var passwordField: EditText
    private lateinit var signupButton: Button
    private lateinit var profileImage: CircleImageView
    var image:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        usernameField = findViewById(R.id.username_field)
        emailIdField = findViewById(R.id.email_field)
        passwordField = findViewById(R.id.password_field)
        signupButton = findViewById(R.id.signup_button)
        profileImage = findViewById(R.id.profile_image)

        profileImage.setOnClickListener{ openGallery()}

        signupButton.setOnClickListener {
            val username = usernameField.text.toString()
            val email = emailIdField.text.toString()
            val password = passwordField.text.toString()

            if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && image != null) {
                sendOTP(username, email, password)
            } else {
                Toast.makeText(this, "Please fill in all the fields and select an image", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun sendOTP(username: String, email: String,  password: String) {
        lifecycleScope.launch {
            try {
                val sendOtpRequest = SendOtpRequest(email)
                val response = apiService.sendOtp(sendOtpRequest)
                Log.d("response in", response.toString())
                if (response.isSuccessful) {
                    navigateToOtpScreen(username, email,password)
                } else {
                    // Handle unsuccessful response
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    private fun navigateToOtpScreen(username: String, email: String, password: String) {

        val intent = Intent(this, OTPVerification::class.java).apply {
            putExtra("name", username)
            putExtra("email", email)
            putExtra("password", password)
            putExtra("image",image)
        }
        startActivity(intent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            profileImage.setImageURI(selectedImageUri)
            Log.d("imageUri", selectedImageUri.toString())
            val imageFile = File(getRealPathFromURI(selectedImageUri!!))
            Log.d("imageFile", imageFile.toString())
            uploadImage(imageFile)
        }
    }

    private fun uploadImage(file: File) {
        lifecycleScope.launch {
            try {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

                Log.d("responsing", file.toString())
                val response = apiService.uploadImage(imagePart)
                Log.d("responsing", response.toString())
                if (response.isSuccessful) {
                    image=response.body()?.imagePath
                } else {
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }


    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return filePath ?: ""
    }



    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }
}