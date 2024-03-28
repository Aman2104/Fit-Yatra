package com.fityatra.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.fityatra.BuildConfig
import com.fityatra.R
import com.fityatra.models.Exercise

class ExerciseDescriptionActivity : AppCompatActivity() {

    private lateinit var textViewExerciseName:TextView
    private lateinit var textViewExerciseDescription:TextView
    private lateinit var videoViewExercise:VideoView
    private lateinit var timer_button: Button
    private lateinit var camera_button: Button
    private lateinit var chronometer: Chronometer

    private var isTimerRunning=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_description)

        textViewExerciseName= findViewById(R.id.textViewExerciseName)
        textViewExerciseDescription= findViewById(R.id.textViewExerciseDescription)
        videoViewExercise= findViewById(R.id.videoViewExercise)
        timer_button= findViewById(R.id.timer_button)
        camera_button= findViewById(R.id.camera_button)
        chronometer= findViewById(R.id.chronometer)

        val exercise = intent.getSerializableExtra("exercise") as Exercise
        Log.d("exercise", exercise.toString())

        val videoUri = Uri.parse(BuildConfig.API_URL + exercise.video)

        textViewExerciseName.text = exercise.name
        textViewExerciseDescription.text = exercise.description

        videoViewExercise.setVideoURI(videoUri)
        videoViewExercise.setOnPreparedListener { mp ->
            mp.isLooping = true
            mp.setVolume(0f, 0f)
        }
        videoViewExercise.start()


        timer_button.setOnClickListener {
            if (!isTimerRunning) {
                startTimer(chronometer)
            } else {
                stopTimer(chronometer)
            }
        }
        camera_button.setOnClickListener {
            val intent= Intent(this,CameraActivity::class.java)
            startActivity(intent)
        }
    }


    private fun startTimer(chronometer: Chronometer) {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
        isTimerRunning = true
        timer_button.text = "Stop Exercise Timer"
    }

    private fun stopTimer(chronometer: Chronometer) {
        chronometer.stop()
        isTimerRunning = false
        timer_button.text = "Start Exercise Timer"
        Toast.makeText(this, "Exercise Timer Stopped", Toast.LENGTH_SHORT).show()
    }

}