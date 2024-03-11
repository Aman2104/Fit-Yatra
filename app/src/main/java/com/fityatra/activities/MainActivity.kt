package com.fityatra.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fityatra.PermissionManager
import com.fityatra.R
import com.fityatra.R.id
import com.fityatra.services.StepTrackingService

class MainActivity : AppCompatActivity() {
    private lateinit var stepCountsTextView: TextView
    private lateinit var lowAccuracyTextView: TextView
    private lateinit var mediumAccuracyTextView: TextView
    private lateinit var highAccuracyTextView: TextView

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        startService(Intent(this, StepTrackingService::class.java))
        if (!PermissionManager.requestActivityRecognitionPermission(this)) {
            showPermissionRationaleDialog()
        }

        stepCountsTextView = findViewById(id.completedStepsTextView)
        lowAccuracyTextView = findViewById(id.lowAccuracyStepsTextView)
        mediumAccuracyTextView = findViewById(id.mediumAccuracyStepsTextView)
        highAccuracyTextView = findViewById(id.highAccuracyStepsTextView)
        val currentStepCount = getStepCount()
        Log.d("working", "OK")
        stepCountsTextView.text = "Steps taken: ${currentStepCount.total}"
        lowAccuracyTextView.text = "L: ${currentStepCount.lowAccuracy}"
        mediumAccuracyTextView.text = "M: ${currentStepCount.mediumAccuracy}"
        highAccuracyTextView.text = "H: ${currentStepCount.highAccuracy}"


    }

    private fun getStepCount(): StepCounts {
        val sharedPreferences = getSharedPreferences("step_counts", MODE_PRIVATE)
        val totalSteps = sharedPreferences.getInt("total_step_count", 0)
        val lowAccuracy = sharedPreferences.getInt("low_accuracy_count", 0)
        val mediumAccuracy = sharedPreferences.getInt("medium_accuracy_count", 0)
        val highAccuracy = sharedPreferences.getInt("high_accuracy_count", 0)
        return StepCounts(totalSteps, lowAccuracy, mediumAccuracy, highAccuracy)
    }

    private fun showPermissionRationaleDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Activity Recognition Permission Needed")
        builder.setMessage("This app needs activity recognition permission to track your steps. Would you like to allow it?")
        builder.setPositiveButton("Allow") { dialog, which ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton("Deny") { dialog, which ->
            Toast.makeText(
                this,
                "Step tracking unavailable without permission.",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.show()
    }


    data class StepCounts(
        val total: Int,
        val lowAccuracy: Int,
        val mediumAccuracy: Int,
        val highAccuracy: Int
    )


}