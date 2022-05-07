package com.fitdback.test

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.fitdback.database.DataBasket
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
import kotlinx.coroutines.*
import java.util.*

class BarChartTestActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_test)

        // Bar chart
        val barChart: BarChart = findViewById(R.id.barChart) // barChart 생성

        // 사용할 데이터
        val entries = ArrayList<BarEntry>()
        val dailySum = DataBasket.enhancedGetDailySum(DataBasket.individualExData!!, "ex_count")
        var xValue = 1.2f

        // 엔트리에 데이터 추가
        for (data in dailySum) {
            entries.add(BarEntry(xValue, data.value.toFloat()))
            xValue += 1
        }

        BarChartGenerator().runBarChart(barChart, entries)

    }

}