package com.fityatra.activities

import ApiClient.apiService
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.fityatra.R
import com.fityatra.models.StepCounts
import com.fityatra.services.StepTrackingService
import com.fityatra.utils.GetUserRequest
import com.fityatra.utils.LoginRequest
import com.fityatra.utils.PermissionManager
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var stepCountsTextView: TextView
    private lateinit var stepProgress: ProgressBar
    private lateinit var calorieProgress: ProgressBar
    private lateinit var percentage: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var userImage: CircleImageView


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        getUser()
        startService(Intent(this, StepTrackingService::class.java))

        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        PermissionManager().initPermissionLauncher(this)
        stepCountsTextView = findViewById(R.id.completedStepsTextView)
        stepProgress = findViewById(R.id.stepProgressBar)
        calorieProgress = findViewById(R.id.calorieProgressBar)
        percentage = findViewById(R.id.txtper)
        drawerLayout = findViewById(R.id.drawer_layout)
        userImage = findViewById(R.id.userImage)



        drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        userImage.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val currentStepCount = getStepCount()
        Log.d("working", "OK")
        val percent = (currentStepCount.total / 6000.0) * 100
        percentage.text = percent.toInt().toString() + "%"
        stepProgress.progress = percent.toInt()
        stepProgress.rotation= 270F
        calorieProgress.rotation= 270F
        stepCountsTextView.text = "Steps taken: ${currentStepCount.total}"

    }

    private fun getUser() {
        lifecycleScope.launch {
            try {
                val sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE)
                val authToken = sharedPreferences.getString("token", "")
                if (authToken != null) {
                    Log.d("token", authToken)
                    val userRequest = GetUserRequest(authToken)
                val response = apiService.getUser(userRequest)
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d("response is ", user.toString())
                    // Handle user data
                } else {
                    Log.d("else response is ", response.toString())
                    navigateToLoginScreen()
                }
                }
            } catch (e: Exception) {
                Log.d("response is ", e.toString())
                // Handle exception
            }
        }

    }

    private fun getStepCount(): StepCounts {
        val sharedPreferences = getSharedPreferences("step_counts", MODE_PRIVATE)
        val totalSteps = sharedPreferences.getInt("total_step_count", 0)
        val lowAccuracy = sharedPreferences.getInt("low_accuracy_count", 0)
        val mediumAccuracy = sharedPreferences.getInt("medium_accuracy_count", 0)
        val highAccuracy = sharedPreferences.getInt("high_accuracy_count", 0)
        return StepCounts(totalSteps, lowAccuracy, mediumAccuracy, highAccuracy)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher
        return true
    }


    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}