package com.fitdback.userinterface.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R
import com.fitdback.test.barChartTest.BarChartTestActivity
import com.fitdback.test.barChartTest.BarChartVariables
import com.fitdback.test.barChartTest.MyBarChartGenerator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatisticFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_statistic, container, false)


        // Bar chart Layout
        val barChart: BarChart = view.findViewById(R.id.barChart)

        val selectedExTypeArea = view.findViewById<TextView>(R.id.selectedExTypeArea)
        val selectedDataArea = view.findViewById<TextView>(R.id.selectedDataArea)
        val yAxisTitleArea = view.findViewById<TextView>(R.id.yAxisTitleArea)

        val btnSetSquatChart = view.findViewById<Button>(R.id.btnSetSquatChart_)
        val btnSetPlankChart = view.findViewById<Button>(R.id.btnSetPlankChart_)
        val btnSetSideLateralRaiseChart = view.findViewById<Button>(R.id.btnSetSideLateralRaiseChart_)

        val btnSetExCalorieChart = view.findViewById<Button>(R.id.btnShowExCalorieChart_)
        val btnSetExCountChart = view.findViewById<Button>(R.id.btnShowExCountChart_)
        val btnSetExTimeChart = view.findViewById<Button>(R.id.btnShowExTimeChart_)

        val btnShowThisWeek = view.findViewById<Button>(R.id.btnShowThisWeek_)
        val btnShowPreviousWeek = view.findViewById<Button>(R.id.btnShowPreviousWeek_)
        val btnShowNextWeek = view.findViewById<Button>(R.id.btnShowNextWeek_)

        /*
            운동 종류, 데이터 종류 선택
         */
        btnSetSquatChart.setOnClickListener {
            setFirstTargetData("squat", selectedExTypeArea)
        }

        btnSetPlankChart.setOnClickListener {
            setFirstTargetData("plank", selectedExTypeArea)
        }

        btnSetSideLateralRaiseChart.setOnClickListener {
            setFirstTargetData("sideLateralRaise", selectedExTypeArea)
        }

        btnSetExCalorieChart.setOnClickListener {
            setSecondTargetData("ex_calorie", selectedDataArea)
        }

        btnSetExCountChart.setOnClickListener {
            setSecondTargetData("ex_count", selectedDataArea)
        }

        btnSetExTimeChart.setOnClickListener {
            setSecondTargetData("ex_time", selectedDataArea)
        }

        /*
            차트 보기
         */
        btnShowThisWeek.setOnClickListener { // 이번 주(1주일 전 ~ 오늘) 차트보기

            if (BarChartVariables.firstTargetData == null || BarChartVariables.secondTargetData == null) {

                Toast.makeText(context, "Select both ExType & Data First!!!", Toast.LENGTH_SHORT)
                    .show()

            } else {
                // barChart 초기화
                clearBarChart(barChart)

                // yAxis Title
                setYAxisTitle(yAxisTitleArea)

                // 보고자 하는 날짜 리스트
                val dateListOfTargetWeek = DataBasket.getDateListOfThisWeek()
                updateBarChartData(dateListOfTargetWeek) // BarChartVariables 클래스의 전역변수 update

                Log.d(
                    "BarChart",
                    "BarChartVariables.lastDateOfXIndex: ${BarChartVariables.lastDateOfXIndex}"
                )

                // 이번 주 날짜별 e Sum
                val dailyExCountSumBarEntry =
                    getDailySumBarEntry(
                        dateListOfTargetWeek,
                        BarChartVariables.firstTargetData!!,
                        BarChartVariables.secondTargetData!!
                    )


                // 실제 Bar Data Set 생성.
                // dailyExCountSumBarEntry 또는 dailyExCalorieSumBarEntry 로 argument변경하여 사용
                val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList")
                setExpressedDataFormat(barDataSet)

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

                Toast.makeText(context, "Select both ExType & Data First!!!", Toast.LENGTH_SHORT)
                    .show()

            } else {

                clearBarChart(barChart)

                // yAxis Title
                setYAxisTitle(yAxisTitleArea)

                // 마지막에 저장된 X Index를 이용하여 일주일 전의 dateListOfTargetWeek을 생성
                val lastDateOfXIndex = BarChartVariables.lastDateOfXIndex
                var (year: Int, month: Int, date: Int) = getYearMonthDateOfLastDate(lastDateOfXIndex)

                val dateOneWeekBefore =
                    DataBasket.getDateOfOneWeekBeforeOrTomorrow(year, month, date, "Before")

                val triple = getYearMonthDateOfLastDate(dateOneWeekBefore)
                year = triple.first
                month = triple.second
                date = triple.third

                val dateListOfTargetWeek =
                    DataBasket.getOneWeekListFromDate(year, month, date, "Before")

                updateBarChartData(dateListOfTargetWeek)
                Log.d(
                    "BarChart",
                    "BarChartVariables.lastDateOfXIndex: ${BarChartVariables.lastDateOfXIndex}"
                )

                // Bar Chart 용 데이터 생성
                val dailyExCountSumBarEntry =
                    getDailySumBarEntry(
                        dateListOfTargetWeek,
                        BarChartVariables.firstTargetData!!,
                        BarChartVariables.secondTargetData!!
                    )

                val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList")
                setExpressedDataFormat(barDataSet)

                // Bar Chart 데이터 삽입
                val data = BarData(barDataSet)
                barChart.data = data

                // Bar Chart 실행
                MyBarChartGenerator().runBarChart(barChart, barDataSet.yMax + 1.0f)

            }

        }

        btnShowNextWeek.setOnClickListener {

            if (BarChartVariables.firstTargetData == null || BarChartVariables.secondTargetData == null) {

                Toast.makeText(context, "Select both ExType & Data First!!!", Toast.LENGTH_SHORT)
                    .show()

            } else {

                clearBarChart(barChart)

                // yAxis Title
                setYAxisTitle(yAxisTitleArea)

                // 마지막에 저장된 X Index를 이용하여 일주일 후의 dateListOfTargetWeek을 생성
                val lastDateOfXIndex = BarChartVariables.lastDateOfXIndex
                var (year: Int, month: Int, date: Int) = getYearMonthDateOfLastDate(lastDateOfXIndex)

                val dateOfTomorrow =
                    DataBasket.getDateOfOneWeekBeforeOrTomorrow(year, month, date, "Tomorrow")

                val triple = getYearMonthDateOfLastDate(dateOfTomorrow)
                year = triple.first
                month = triple.second
                date = triple.third

                val dateListOfTargetWeek =
                    DataBasket.getOneWeekListFromDate(year, month, date, "After")

                updateBarChartData(dateListOfTargetWeek)
                Log.d(
                    "BarChart",
                    "BarChartVariables.lastDateOfXIndex: ${BarChartVariables.lastDateOfXIndex}"
                )

                // Bar Chart 용 데이터 생성
                val dailyExCountSumBarEntry =
                    getDailySumBarEntry(
                        dateListOfTargetWeek,
                        BarChartVariables.firstTargetData!!,
                        BarChartVariables.secondTargetData!!
                    )

                val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList")
                setExpressedDataFormat(barDataSet)

                // Bar Chart 데이터 삽입
                val data = BarData(barDataSet)
                barChart.data = data

                // Bar Chart 실행
                MyBarChartGenerator().runBarChart(barChart, barDataSet.yMax + 1.0f)

            }

        }
        return view
    }



    private fun setYAxisTitle(yAxisTitleArea: TextView) {
        yAxisTitleArea.text = when (BarChartVariables.secondTargetData) {
            "ex_count" -> "개수"
            "ex_calorie" -> "kcal"
            "ex_time" -> "시간"
            else -> "오류"
        }
    }

    private fun setFirstTargetData(firstTargetData: String, selectedExTypeArea: TextView) {
        BarChartVariables.firstTargetData = firstTargetData
        selectedExTypeArea.text = BarChartVariables.firstTargetData
    }

    private fun setSecondTargetData(secondTargetData: String, selectedDataArea: TextView) {
        BarChartVariables.secondTargetData = secondTargetData
        selectedDataArea.text = BarChartVariables.secondTargetData
    }

    @SuppressLint("LogNotTimber")
    private fun showEachChart(barChart: BarChart, secondTargetData: String) {

        /*
            기능 : 칼로리 소모량, 운동 횟수, 운동 시간 별로 차트 보기
            secondTargetData: "ex_count" or "ex_calorie" or "ex_time"
         */

        clearBarChart(barChart)

        val dailyExCountSumBarEntry =
            getDailySumBarEntry(BarChartVariables.dateListOfWeek, "squat", secondTargetData)

        val barDataSet = BarDataSet(dailyExCountSumBarEntry, "exDataList").apply {

            valueTextColor = Color.BLACK
            valueTextSize = 10f
            setColors(*ColorTemplate.COLORFUL_COLORS)

        }

        val data = BarData(barDataSet)
        barChart.data = data
        Log.d(
            "BarChart",
            "BarChartVariables.lastDateOfXIndex: ${BarChartVariables.lastDateOfXIndex}"
        )

        MyBarChartGenerator().runBarChart(barChart, barDataSet.yMax + 1.0f)

    }

    private fun getYearMonthDateOfLastDate(lastDateOfXIndex: String): Triple<Int, Int, Int> {
        val year: Int = lastDateOfXIndex.slice(0..1).toInt() + 2000 // 22 + 2000 = 2022
        val month: Int =
            lastDateOfXIndex.slice(2..3).toInt() - 1 // Gregorian Calendar 사용시 month 주의
        val date: Int = lastDateOfXIndex.slice(4..5).toInt()
        return Triple(year, month, date)
    }

    private fun updateBarChartData(dateListOfTargetWeek: MutableList<String>) {
        BarChartVariables.dateListOfWeek =
            dateListOfTargetWeek // BarChartVariables.dateListOfWeek 변수가 lateinit으로 선언된 것에 주의!
        BarChartVariables.lastDateOfXIndex = dateListOfTargetWeek[6]
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
        secondTargetData: "ex_count", "ex_calorie", "ex_time"
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

    private fun setExpressedDataFormat(barDataSet: BarDataSet) {

        barDataSet.apply {

            valueTextColor = Color.BLACK
            valueTextSize = 10f
            setColors(*ColorTemplate.COLORFUL_COLORS)
            valueFormatter = BarChartVariables.expressedDataFormatter // 데이터 소수점 표기 -> 정수 표기

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatisticFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatisticFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}