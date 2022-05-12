package com.fitdback.test.barChartTest

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
        val btnLaunchChartAgain = findViewById<Button>(R.id.btnLaunchChartAgain)
        val btnShowPreviousWeek = findViewById<Button>(R.id.btnShowPreviousWeek)
        val btnShowNextWeek = findViewById<Button>(R.id.btnShowNextWeek)

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

        btnLaunchChartAgain.setOnClickListener { // 이번 주(1주일 전 ~ 오늘) 차트보기

            // barChart 초기화
            clearBarChart(barChart)

            // 보고자 하는 날짜 리스트
            val dateListOfTargetWeek = DataBasket.getDateListOfThisWeek()
            updateBarChartData(dateListOfTargetWeek) // BarChartData 클래스의 전역변수 update

            Log.d("BarChart", "BarChartData.lastDateOfXIndex: ${BarChartData.lastDateOfXIndex}")

            // 이번 주 날짜별 ex_count Sum
            val dailyExCountSumBarEntry =
                getDailySumBarEntry(dateListOfTargetWeek, "squat", "ex_count")

            // 실제 Bar Data Set 생성.
            // dailyExCountSumBarEntry 또는 dailyExCalorieSumBarEntry 로 argument변경하여 사용
            val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList").apply {

                valueTextColor = Color.BLACK
                valueTextSize = 10f
                setColors(*ColorTemplate.COLORFUL_COLORS)

            }

            // Bar Chart 데이터 삽입
            val data = BarData(barDataSet)
            barChart.data = data
            Log.d("BarChart", "BarChartData.lastDateOfXIndex: ${BarChartData.lastDateOfXIndex}")

            // Bar Chart 실행
            BarChartGenerator2().runBarChart(barChart, barDataSet.yMax + 1.0f)

        }

        btnShowPreviousWeek.setOnClickListener {

            clearBarChart(barChart)

            // 마지막에 저장된 X Index를 이용하여 일주일 전의 dateListOfTargetWeek을 생성
            val lastDateOfXIndex = BarChartData.lastDateOfXIndex
            var (year: Int, month: Int, date: Int) = getYearMonthDateOfLastDate(lastDateOfXIndex)

            val dateOneWeekBefore = DataBasket.getDateOneWeekBefore(year, month, date)

            val triple = getYearMonthDateOfLastDate(dateOneWeekBefore)
            year = triple.first
            month = triple.second
            date = triple.third

            val dateListOfTargetWeek = DataBasket.getOneWeekListBeforeTheDate(year, month, date)
            
            updateBarChartData(dateListOfTargetWeek)
            Log.d("BarChart", "BarChartData.lastDateOfXIndex: ${BarChartData.lastDateOfXIndex}")

            // Bar Chart 용 데이터 생성
            val dailyExCountSumBarEntry =
                getDailySumBarEntry(dateListOfTargetWeek, "squat", "ex_count")

            val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList").apply {

                valueTextColor = Color.BLACK
                valueTextSize = 10f
                setColors(*ColorTemplate.COLORFUL_COLORS)

            }

            // Bar Chart 데이터 삽입
            val data = BarData(barDataSet)
            barChart.data = data

            // Bar Chart 실행
            BarChartGenerator2().runBarChart(barChart, barDataSet.yMax + 1.0f)

        }

        btnShowNextWeek.setOnClickListener {

//            barChart.clearValues()
//            barChart.clear()
//            BarChartGenerator2().runBarChart(barChart, barDataSet.yMax + 1.0f)

        }

    }

    private fun getYearMonthDateOfLastDate(lastDateOfXIndex: String): Triple<Int, Int, Int> {
        val year: Int = lastDateOfXIndex.slice(0..1).toInt() + 2000 // 22 + 2000 = 2022
        val month: Int =
            lastDateOfXIndex.slice(2..3).toInt() - 1 // Gregorian Calendar 사용시 month 주의
        val date: Int = lastDateOfXIndex.slice(4..5).toInt()
        return Triple(year, month, date)
    }

    private fun updateBarChartData(dateListOfTargetWeek: MutableList<String>) {
        BarChartData.dateListOfWeek =
            dateListOfTargetWeek // BarChartData.dateListOfWeek 변수가 lateinit으로 선언된 것에 주의!
        BarChartData.lastDateOfXIndex = dateListOfTargetWeek[6]
    }

    private fun clearBarChart(barChart: BarChart) {
        // 오래된 데이터 삭제
        barChart.clear()
        if (!barChart.isEmpty) { // 기존 데이터가 있으면 clear
            barChart.clearValues()
        }
    }

    private fun getDailySumBarEntry(
        dateList: MutableList<String>,
        firstTargetData: String,
        secondTargetData: String
    ): MutableList<BarEntry> {
        /*
        dataSnapshot: 이미 로드한 Firebase DataSnapshot (users/ex_data)
        firstTargetData: ex_type 중 하나. "squat", "plank", "sideLateralRaise"
        secondtargetData: "ex_count", "ex_calorie", "ex_time"
         */

        val dailySumBarEntry = mutableListOf<BarEntry>()

        // (날짜, targetData의 합)의 Key-Value 구조의 Map
        val dailySumList =
            DataBasket.enhancedGetDailySum(
                DataBasket.individualExData!!,
                dateList,
                firstTargetData,
                secondTargetData
            )

        var xValue = 1f

        for (keyAndValue in dailySumList) {
            dailySumBarEntry.add(BarEntry(xValue, keyAndValue.value.toFloat()))
            xValue += 1f
        }

        return dailySumBarEntry
    }

}