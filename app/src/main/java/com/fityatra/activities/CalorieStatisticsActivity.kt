package com.fityatra.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fityatra.R
import com.fityatra.utils.RoundedBarChartRenderer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class CalorieStatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calorie_statistics)
        val barChart = findViewById<BarChart>(R.id.lineChart)


        val roundedRenderer =
            RoundedBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler)
        barChart.renderer = roundedRenderer

        // Dummy data for illustration
        val entries = arrayListOf<BarEntry>()
        val labels = arrayOf("Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7")
        val data = floatArrayOf(2000f, 2200f, 1800f, 2400f, 2100f, 2300f, 2500f)

        // Populate entries
        for (i in data.indices) {
            entries.add(BarEntry(i.toFloat(), data[i]))
        }

        // Create dataset
        val dataSet = BarDataSet(entries, "Calories Consumed")
        dataSet.color = resources.getColor(R.color.purple_200)
        dataSet.valueTextColor = resources.getColor(android.R.color.black)

        val barWidth = 0.4f

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setAvoidFirstLastClipping(true)

        // Create BarData and set to chart
        val barData = BarData(dataSet)
        barData.barWidth = barWidth
        barData.isHighlightEnabled = false
        barChart.data = barData


        barChart.invalidate()
    }
}
