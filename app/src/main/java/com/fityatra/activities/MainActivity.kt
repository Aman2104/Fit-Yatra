package com.fityatra.activities

import ApiClient.apiService
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fityatra.R
import com.fityatra.models.StepCounts
import com.fityatra.services.StepTrackingService
import com.fityatra.utils.ExerciseAdapter
import com.fityatra.utils.PermissionManager
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var stepCountsTextView: TextView
    private lateinit var stepProgress: ProgressBar
    private lateinit var calorieProgress: ProgressBar
    private lateinit var percentage: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var userImage: CircleImageView


    private lateinit var userId:String
    private lateinit var userName:String
    private lateinit var userEmail:String


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        getUser()
        startService(Intent(this, StepTrackingService::class.java))

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.exerciseRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        getExercises()

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
        drawerToggle.syncState()
        drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)



        userImage.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val currentStepCount = getStepCount()
        Log.d("working", "OK")
        val percent = (currentStepCount.total / 6000.0) * 100
        percentage.text = percent.toInt().toString() + "%"
        stepProgress.progress = percent.toInt()
        stepProgress.rotation = 270F
        calorieProgress.rotation = 270F
        stepCountsTextView.text = "Steps taken: ${currentStepCount.total}"

    }

    private fun getExercises() {
        lifecycleScope.launch(Dispatchers.Main) {
            val response = apiService.getExercises()
            if (response.isSuccessful) {
                val exercises = response.body()
                exercises?.let {
                    adapter = ExerciseAdapter(it)
                    recyclerView.adapter = adapter
                }
            } else {
                // Handle unsuccessful response
            }
        }

    }


    private fun getUser() {
        lifecycleScope.launch {
            try {
                val sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE)
                val authToken = sharedPreferences.getString("token", "")
                if (authToken != null) {
                    Log.d("token", authToken)
                    val response = apiService.getUser(authToken)
                    if (response.isSuccessful) {
                        val user = response.body()
                        userId = user?._id.toString()
                        userName = user?.name.toString()
                        userEmail = user?.email.toString()
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


    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this, PersonalDetailsActivity::class.java)
                intent.putExtra("id", userId)
                intent.putExtra("email",userEmail)
                intent.putExtra("name",userName)
                startActivity(intent)
            }
            R.id.nav_exercises -> {
                // Handle Exercises item click
                val intent = Intent(this, ExercisesActivity::class.java)
                intent.putExtra("id", userId)
                intent.putExtra("email",userEmail)
                intent.putExtra("name",userName)
                startActivity(intent)
            }
            R.id.nav_posture_detection -> {
                // Handle Posture Detection item click
                val intent = Intent(this, PostureDetectionActivity::class.java)
                intent.putExtra("id", userId)
                intent.putExtra("email",userEmail)
                intent.putExtra("name",userName)
                startActivity(intent)
            }
            R.id.nav_routine -> {
                // Handle Add Routine item click
//                val intent = Intent(this, AddRoutineActivity::class.java)
//                intent.putExtra("id", userId)
//                intent.putExtra("email",userEmail)
//                intent.putExtra("name",userName)
//                startActivity(intent)
            }
            R.id.nav_calorie -> {
                // Handle Calorie Monitor item click
                val intent = Intent(this, CalorieStatisticsActivity::class.java)
                intent.putExtra("id", userId)
                intent.putExtra("email",userEmail)
                intent.putExtra("name",userName)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                // Handle Logout item click
                // Clear token from storage
//                clearTokenFromStorage()
                // Redirect to login activity
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        return true
    }

}