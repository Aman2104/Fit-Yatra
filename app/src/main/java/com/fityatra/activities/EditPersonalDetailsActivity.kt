package com.fityatra.activities

import ApiClient.apiService
import com.fityatra.R
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fityatra.utils.ProfileData
import kotlinx.coroutines.launch


class EditPersonalDetailsActivity : AppCompatActivity() {
    private lateinit var editTextAge: EditText
    private lateinit var editTextHeight: EditText
    private lateinit var editTextWeight: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var spinnerPostureProblems: Spinner
    private lateinit var buttonSave: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_personal_details)
        editTextAge = findViewById(R.id.editTextAge)
        editTextHeight = findViewById(R.id.editTextHeight)
        editTextWeight = findViewById(R.id.editTextWeight)
        spinnerGender = findViewById(R.id.spinnerGender)
        spinnerPostureProblems = findViewById(R.id.spinnerPostureProblems)
        buttonSave = findViewById(R.id.buttonSave)

        // Populate gender spinner
        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.genders,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = genderAdapter

        // Populate posture problems spinner
        val postureProblemsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.posture_problems,
            android.R.layout.simple_spinner_item
        )
        postureProblemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPostureProblems.adapter = postureProblemsAdapter

        buttonSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val age = editTextAge.text.toString().toInt()
        val height = editTextHeight.text.toString().toInt()
        val weight = editTextWeight.text.toString().toInt()
        val gender = spinnerGender.selectedItem.toString()
        val postureProblems = spinnerPostureProblems.selectedItem.toString()

        val sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", "")

        val profileData = ProfileData(age, height, weight, gender, postureProblems)
        Log.d("profileData", profileData.toString())

        lifecycleScope.launch {
            try {
                val response = apiService.saveProfile(authToken!!,profileData)
                showToast(response.toString())
                if (response.isSuccessful) {
                    showToast("Data Saved Successfully")
                } else {
                    showToast("Error")
                    // Handle unsuccessful response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

}
