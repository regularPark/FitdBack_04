package com.fitdback.test.barChartTest

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class BarChartTestActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_test)

        // Bar chart Layout
        val barChart: BarChart = findViewById(R.id.barChart)

        // test sample
        val sampleEntryList = mutableListOf<BarEntry>()
        sampleEntryList.add(BarEntry(1f, 4f))
        sampleEntryList.add(BarEntry(2f, 10f))
        sampleEntryList.add(BarEntry(3f, 2f))
        sampleEntryList.add(BarEntry(4f, 15f))
        sampleEntryList.add(BarEntry(5f, 13f))
        sampleEntryList.add(BarEntry(6f, 2f))
        sampleEntryList.add(BarEntry(7f, 20f))

        /*
            Bar Data Set을 만들기 위한 Data Entry
         */

        // 날짜별 ex_count Sum
        val dailyExCountSumBarEntry = getDailySumBarEntry("ex_count")

        // 날짜별 ex_calorie Sum
        val dailyExCalorieSumBarEntry = getDailySumBarEntry("ex_calorie")

        // 실제 Bar Data Set 생성.
        // dailyExCountSumBarEntry 또는 dailyExCalorieSumBarEntry 로 argument변경하여 사용
        val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList").apply {

            valueTextColor = Color.BLACK
            valueTextSize = 10f
            setColors(*ColorTemplate.COLORFUL_COLORS)

        }

        val data = BarData(barDataSet)
        barChart.data = data

        val barChartGenerator2 = BarChartGenerator2()
        BarChartGenerator2().runBarChart(barChart, barDataSet.yMax + 1.0f)

    }

    private fun getDailySumBarEntry(targetData: String): MutableList<BarEntry> {
        /*
            targetData: String 종류 : "ex_count" , "ex_calorie" // 스펠링 주의!
         */
        val dailySumBarEntry = mutableListOf<BarEntry>()
        val dailySumList    =
            DataBasket.enhancedGetDailySum(DataBasket.individualExData!!, targetData)
        var xValue = 1f

        for (keyAndValue in dailySumList) {
            dailySumBarEntry.add(BarEntry(xValue, keyAndValue.value.toFloat()))
            xValue += 1f
        }

        return dailySumBarEntry
    }

}