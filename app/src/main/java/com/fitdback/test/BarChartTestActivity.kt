package com.fitdback.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fitdback.posedetection.R
import com.fitdback.userinterface.barchart.BarChartGenerator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class BarChartTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_test)

        // Bar chart
        val barChart: BarChart = findViewById(R.id.barChart) // barChart 생성

        // 데이터 셋
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1.2f, 20.0f))
        entries.add(BarEntry(2.2f, 70.0f))
        entries.add(BarEntry(3.2f, 30.0f))
        entries.add(BarEntry(4.2f, 90.0f))
        entries.add(BarEntry(5.2f, 70.0f))
        entries.add(BarEntry(6.2f, 30.0f))
        entries.add(BarEntry(7.2f, 90.0f))

        BarChartGenerator().runBarChart(barChart, entries)

    }

}