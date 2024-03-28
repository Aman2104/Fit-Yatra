package com.fityatra.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fityatra.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class PostureDetectionActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button

    private val PICK_IMAGE_REQUEST = 1

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_posture_detection)

        uploadButton.setOnClickListener {
            openFileChooser()
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageView.setImageBitmap(bitmap)
                // Call your API with the selected image URI
                if (imageUri != null) {
                    callApiWithImage(imageUri)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun callApiWithImage(imageUri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            // Perform your API call here
            // Example:
            // val response = RetrofitInstance.api.uploadImage(imageUri)
            // Handle response accordingly
            withContext(Dispatchers.Main) {
                // Update UI or handle response
            }
        }
    }
}
