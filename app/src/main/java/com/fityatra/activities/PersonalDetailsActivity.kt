package com.fityatra.activities

import ApiClient.apiService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fityatra.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class PersonalDetailsActivity : AppCompatActivity() {
    // Declare views
    private lateinit var userImageView: CircleImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewAge: TextView
    private lateinit var textViewGender: TextView
    private lateinit var textViewHeight: TextView
    private lateinit var textViewWeight: TextView
    private lateinit var textViewProblem: TextView
    private lateinit var updateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_details)

        // Initialize views
        userImageView = findViewById(R.id.userImageView)
        textViewName = findViewById(R.id.textViewName)
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewAge = findViewById(R.id.textViewAge)
        textViewGender = findViewById(R.id.textViewGender)
        textViewHeight = findViewById(R.id.textViewHeight)
        textViewWeight = findViewById(R.id.textViewWeight)
        textViewProblem = findViewById(R.id.textViewProblem)
        updateBtn = findViewById(R.id.updateBtn)

        val userId = intent.getStringExtra("id")
        getProfile(userId)

        updateBtn.setOnClickListener {
            val intent = Intent(this, EditPersonalDetailsActivity::class.java)
            intent.putExtra("id", userId)
            startActivity(intent)

        }
    }

    private fun getProfile(userId: String?) {
        lifecycleScope.launch {
            try {
                val response = apiService.getProfile(userId!!)
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Update UI with user data
                        textViewName.text = "Name: ${user.user.name}"
                        textViewEmail.text = "Email: ${user.user.email}"
                        textViewAge.text = "Age: ${user.age}"
                        textViewGender.text = "Gender: ${user.gender}"
                        textViewHeight.text = "Email: ${user.height}"
                        textViewWeight.text = "Email: ${user.weight}"
                        textViewProblem.text = "Email: ${user.postureProblems}"

                        // Update other TextViews similarly
                    } else {
                        // Handle case where user data is null
                        showNoDataAvailable()
                    }
                } else {
                    // Handle unsuccessful response
                    showNoDataAvailable()
                }
            } catch (e: Exception) {
                // Handle exception
                Log.e("getProfile", "Exception: $e")
                showNoDataAvailable()
            }
        }
    }

    private fun showNoDataAvailable() {
        // Display a message indicating no data available
        textViewName.text = "No Data Available"
        textViewName.gravity=Gravity.CENTER_HORIZONTAL
        textViewEmail.visibility = View.GONE
        textViewAge.visibility= View.GONE
        textViewGender.visibility= View.GONE
        textViewHeight.visibility= View.GONE
        textViewWeight.visibility= View.GONE
        textViewProblem.visibility= View.GONE
        userImageView.visibility=View.GONE
    }
}
