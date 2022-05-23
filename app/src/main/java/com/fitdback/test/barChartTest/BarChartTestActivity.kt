package com.fitdback.test.barChartTest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet


class BarChartTestActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_test)

        // Bar chart Layout
        val barChart: BarChart = findViewById(R.id.barChart)

        val selectedExTypeArea = findViewById<TextView>(R.id.selectedExTypeArea)
        val selectedDataArea = findViewById<TextView>(R.id.selectedDataArea)
        val yAxisTitleArea = findViewById<TextView>(R.id.yAxisTitleArea)

        val btnSetSquatChart = findViewById<Button>(R.id.btnSetSquatChart)
        val btnSetPlankChart = findViewById<Button>(R.id.btnSetPlankChart)
        val btnSetSideLateralRaiseChart = findViewById<Button>(R.id.btnSetSideLateralRaiseChart)

        val btnSetExCalorieChart = findViewById<Button>(R.id.btnShowExCalorieChart)
        val btnSetExCountChart = findViewById<Button>(R.id.btnShowExCountChart)
        val btnSetExTimeChart = findViewById<Button>(R.id.btnShowExTimeChart)

        val btnShowThisWeek = findViewById<Button>(R.id.btnShowThisWeek)
        val btnShowPreviousWeek = findViewById<Button>(R.id.btnShowPreviousWeek)
        val btnShowNextWeek = findViewById<Button>(R.id.btnShowNextWeek)

        /*
            운동 종류, 데이터 종류 선택
         */
        btnSetSquatChart.setOnClickListener {
            BarChartVariables.setFirstTargetData("squat", selectedExTypeArea)
        }

        btnSetPlankChart.setOnClickListener {
            BarChartVariables.setFirstTargetData("plank", selectedExTypeArea)
        }

        btnSetSideLateralRaiseChart.setOnClickListener {
            BarChartVariables.setFirstTargetData("sideLateralRaise", selectedExTypeArea)
        }

        btnSetExCalorieChart.setOnClickListener {
            BarChartVariables.setSecondTargetData("ex_calorie", selectedDataArea)
        }

        btnSetExCountChart.setOnClickListener {
            BarChartVariables.setSecondTargetData("ex_count", selectedDataArea)
        }

        btnSetExTimeChart.setOnClickListener {
            BarChartVariables.setSecondTargetData("ex_time", selectedDataArea)
        }

        /*
            차트 보기
         */
        btnShowThisWeek.setOnClickListener { // 이번 주(1주일 전 ~ 오늘) 차트보기

            if (BarChartVariables.firstTargetData == null || BarChartVariables.secondTargetData == null) {

                Toast.makeText(this, "Select both ExType & Data First!!!", Toast.LENGTH_SHORT)
                    .show()

            } else {
                // barChart 초기화
                BarChartVariables.clearBarChart(barChart)

                // yAxis Title
                BarChartVariables.setYAxisTitle(yAxisTitleArea)

                // 보고자 하는 날짜 리스트
                val dateListOfTargetWeek = DataBasket.getDateListOfThisWeek()
                BarChartVariables.updateBarChartData(dateListOfTargetWeek) // BarChartVariables 클래스의 전역변수 update

                Log.d(
                    "BarChart",
                    "BarChartVariables.lastDateOfXIndex: ${BarChartVariables.lastDateOfXIndex}"
                )

                // 이번 주 날짜별 e Sum
                val dailyExCountSumBarEntry =
                    BarChartVariables.getDailySumBarEntry(
                        dateListOfTargetWeek,
                        BarChartVariables.firstTargetData!!,
                        BarChartVariables.secondTargetData!!
                    )


                // 실제 Bar Data Set 생성.
                // dailyExCountSumBarEntry 또는 dailyExCalorieSumBarEntry 로 argument변경하여 사용
                val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList")
                BarChartVariables.setExpressedDataFormat(barDataSet)

                // Bar Chart 데이터 삽입
                val data = BarData(barDataSet)
                barChart.data = data
                Log.d(
                    "BarChart",
                    "BarChartVariables.lastDateOfXIndex: ${BarChartVariables.lastDateOfXIndex}"
                )

                // Bar Chart 실행
                MyBarChartGenerator().runBarChart(barChart, barDataSet.yMax + 1.0f)

            }

        }

        btnShowPreviousWeek.setOnClickListener {

            if (BarChartVariables.firstTargetData == null || BarChartVariables.secondTargetData == null) {

                Toast.makeText(this, "Select both ExType & Data First!!!", Toast.LENGTH_SHORT)
                    .show()

            } else {

                BarChartVariables.clearBarChart(barChart)

                // yAxis Title
                BarChartVariables.setYAxisTitle(yAxisTitleArea)

                // 마지막에 저장된 X Index를 이용하여 일주일 전의 dateListOfTargetWeek을 생성
                val lastDateOfXIndex = BarChartVariables.lastDateOfXIndex
                var (year: Int, month: Int, date: Int) = BarChartVariables.getYearMonthDateOfLastDate(
                    lastDateOfXIndex!!
                )

                val dateOneWeekBefore =
                    DataBasket.getDateOfOneWeekBeforeOrTomorrow(year, month, date, "Before")

                val triple = BarChartVariables.getYearMonthDateOfLastDate(dateOneWeekBefore)
                year = triple.first
                month = triple.second
                date = triple.third

                val dateListOfTargetWeek =
                    DataBasket.getOneWeekListFromDate(year, month, date, "Before")

                BarChartVariables.updateBarChartData(dateListOfTargetWeek)
                Log.d(
                    "BarChart",
                    "BarChartVariables.lastDateOfXIndex: ${BarChartVariables.lastDateOfXIndex}"
                )

                // Bar Chart 용 데이터 생성
                val dailyExCountSumBarEntry =
                    BarChartVariables.getDailySumBarEntry(
                        dateListOfTargetWeek,
                        BarChartVariables.firstTargetData!!,
                        BarChartVariables.secondTargetData!!
                    )

                val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList")
                BarChartVariables.setExpressedDataFormat(barDataSet)

                // Bar Chart 데이터 삽입
                val data = BarData(barDataSet)
                barChart.data = data

                // Bar Chart 실행
                MyBarChartGenerator().runBarChart(barChart, barDataSet.yMax + 1.0f)

            }

        }

        btnShowNextWeek.setOnClickListener {

            if (BarChartVariables.firstTargetData == null || BarChartVariables.secondTargetData == null) {

                Toast.makeText(this, "Select both ExType & Data First!!!", Toast.LENGTH_SHORT)
                    .show()

            } else {

                BarChartVariables.clearBarChart(barChart)

                // yAxis Title
                BarChartVariables.setYAxisTitle(yAxisTitleArea)

                // 마지막에 저장된 X Index를 이용하여 일주일 후의 dateListOfTargetWeek을 생성
                val lastDateOfXIndex = BarChartVariables.lastDateOfXIndex
                var (year: Int, month: Int, date: Int) = BarChartVariables.getYearMonthDateOfLastDate(
                    lastDateOfXIndex!!
                )

                val dateOfTomorrow =
                    DataBasket.getDateOfOneWeekBeforeOrTomorrow(year, month, date, "Tomorrow")

                val triple = BarChartVariables.getYearMonthDateOfLastDate(dateOfTomorrow)
                year = triple.first
                month = triple.second
                date = triple.third

                val dateListOfTargetWeek =
                    DataBasket.getOneWeekListFromDate(year, month, date, "After")

                BarChartVariables.updateBarChartData(dateListOfTargetWeek)
                Log.d(
                    "BarChart",
                    "BarChartVariables.lastDateOfXIndex: ${BarChartVariables.lastDateOfXIndex}"
                )

                // Bar Chart 용 데이터 생성
                val dailyExCountSumBarEntry =
                    BarChartVariables.getDailySumBarEntry(
                        dateListOfTargetWeek,
                        BarChartVariables.firstTargetData!!,
                        BarChartVariables.secondTargetData!!
                    )

                val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList")
                BarChartVariables.setExpressedDataFormat(barDataSet)

                // Bar Chart 데이터 삽입
                val data = BarData(barDataSet)
                barChart.data = data

                // Bar Chart 실행
                MyBarChartGenerator().runBarChart(barChart, barDataSet.yMax + 1.0f)

            }

        }

    } // end of onCreate()

}