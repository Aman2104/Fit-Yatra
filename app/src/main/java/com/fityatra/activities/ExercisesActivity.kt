package com.fityatra.activities

import ApiClient.apiService
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fityatra.R
import com.fityatra.utils.ExerciseAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExercisesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)


        recyclerView = findViewById(R.id.exerciseRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        getExercises()

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
}