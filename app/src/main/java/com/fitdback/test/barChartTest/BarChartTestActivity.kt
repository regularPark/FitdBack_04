package com.fitdback.test.barChartTest

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class BarChartTestActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_test)

        // Bar chart
        val barChart: BarChart = findViewById(R.id.barChart) // barChart 생성

        // 사용할 데이터
//        val entryList = mutableListOf<BarEntry>()
//        val dailySum = DataBasket.enhancedGetDailySum(DataBasket.individualExData!!, "ex_count")
//        var xValue = 1.2f

        // 엔트리에 데이터 추가
//        for (data in dailySum) {
//            entryList.add(BarEntry(xValue, data.value.toFloat()))
//            xValue += 1
//        }

        // test sample
        val sampleEntryList = mutableListOf<BarEntry>()
        sampleEntryList.add(BarEntry(1f, 4f))
        sampleEntryList.add(BarEntry(2f, 10f))
        sampleEntryList.add(BarEntry(3f, 2f))
        sampleEntryList.add(BarEntry(4f, 15f))
        sampleEntryList.add(BarEntry(5f, 13f))
        sampleEntryList.add(BarEntry(6f, 2f))
        sampleEntryList.add(BarEntry(7f, 20f))

        // 실제 Bar Data Set 생성.
        val barDataSet = BarDataSet(sampleEntryList, "MySampleEntryList").apply {

            valueTextColor = Color.WHITE
            valueTextSize = 10f
            setColors(*ColorTemplate.COLORFUL_COLORS)

        }

        val data = BarData(barDataSet)
        barChart.data = data

        val barChartGenerator2 = BarChartGenerator2()
        BarChartGenerator2.initXLabels(BarChartGenerator2.testXLabels)

        BarChartGenerator2().runBarChart(barChart, barDataSet.yMax)

//        val barDataSet = BarDataSet(entryList, "MyBarDataSet")
//        val barData = BarData(barDataSet)
//        barChart.data =
//        val barChartGenerator = BarChartGenerator()
//        barChartGenerator.
//        BarChartGenerator().runBarChart(barChart, entryList)

    }

}