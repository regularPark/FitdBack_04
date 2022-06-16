package com.fitdback.userinterface.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R
import com.fitdback.test.barChartTest.BarChartVariables
import com.fitdback.test.barChartTest.MyBarChartGenerator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet

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

    @SuppressLint("LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_statistic, container, false)

        // Bar chart Layout
        val barChart: BarChart = view.findViewById(R.id.barChart)

        val selectedExTypeArea = view.findViewById<TextView>(R.id.selectedExTypeArea_)
        val selectedDataArea = view.findViewById<TextView>(R.id.selectedDataArea_)
        val yAxisTitleArea = view.findViewById<TextView>(R.id.yAxisTitleArea_)

        val btnSetSquatChart = view.findViewById<RadioButton>(R.id.btnSetSquatChart_)
        val btnSetPlankChart = view.findViewById<RadioButton>(R.id.btnSetPlankChart_)
        val btnSetSideLateralRaiseChart =
            view.findViewById<RadioButton>(R.id.btnSetSideLateralRaiseChart_)

        val btnSetExCalorieChart = view.findViewById<Button>(R.id.btnShowExCalorieChart_)
        val btnSetExCountChart = view.findViewById<Button>(R.id.btnShowExCountChart_)
        val btnSetExTimeChart = view.findViewById<Button>(R.id.btnShowExTimeChart_)

        val btnShowThisWeek = view.findViewById<Button>(R.id.btnShowThisWeek_)
        val btnShowPreviousWeek = view.findViewById<Button>(R.id.btnShowPreviousWeek_)
        val btnShowNextWeek = view.findViewById<Button>(R.id.btnShowNextWeek_)

        // 차트 그려주는 함수
        fun showChart() {
            if (BarChartVariables.firstTargetData == null || BarChartVariables.secondTargetData == null) {

                Toast.makeText(context, "운동 종류와 데이터를 선택해주세요.", Toast.LENGTH_SHORT)
                    .show()

            } else {
                // barChart 초기화
                BarChartVariables.clearBarChart(barChart)

                // yAxis Title
                BarChartVariables.setYAxisTitle(yAxisTitleArea)

                // 보고자 하는 날짜 리스트
                val dateListOfTargetWeek = BarChartVariables.dateListOfWeek
                BarChartVariables.updateBarChartData(dateListOfTargetWeek) // BarChartVariables 클래스의 전역변수 update

                Log.d(
                    "BarChart",
                    "BarChartVariables.lastDateOfXIndex: ${BarChartVariables.lastDateOfXIndex}"
                )

                // 이번 주 날짜별 Sum
                val dailySumBarEntry =
                    BarChartVariables.getDailySumBarEntry(
                        dateListOfTargetWeek,
                        BarChartVariables.firstTargetData!!,
                        BarChartVariables.secondTargetData!!,
                        DataBasket.individualExData!!
                    )

                // 실제 Bar Data Set 생성.
                val barDataSet = BarDataSet(dailySumBarEntry, "exDataList")
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

        /*
            운동 종류, 데이터 종류 선택
         */
        val radioGroup_1 = view.findViewById<RadioGroup>(R.id.radioGp_1)
        val radioGroup_2 = view.findViewById<RadioGroup>(R.id.radioGp_2)

        radioGroup_1.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btnSetSquatChart_ -> btnSetSquatChart.setOnClickListener {
                    BarChartVariables.setFirstTargetData("squat", null)
                    selectedExTypeArea.text = "스쿼트"

                    if (BarChartVariables.secondTargetData != null) {
                        showChart()
                    }
                }
                R.id.btnSetPlankChart_ -> btnSetPlankChart.setOnClickListener {
                    BarChartVariables.setFirstTargetData("plank", null)
                    selectedExTypeArea.text = "플랭크"

                    if (BarChartVariables.secondTargetData != null) {
                        showChart()
                    }
                }
                R.id.btnSetSideLateralRaiseChart_ -> btnSetSideLateralRaiseChart.setOnClickListener {
                    BarChartVariables.setFirstTargetData("sideLateralRaise", null)
                    selectedExTypeArea.text = "사이드 래터럴 레이즈"

                    if (BarChartVariables.secondTargetData != null) {
                        showChart()
                    }
                }

            }
        }

        radioGroup_2.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btnShowExCalorieChart_ -> btnSetExCalorieChart.setOnClickListener {
                    BarChartVariables.setSecondTargetData("ex_calorie", null)
                    selectedDataArea.text = "칼로리 소모량"

                    checkIsChartInitialization()
                    showChart()

                }
                R.id.btnShowExCountChart_ -> btnSetExCountChart.setOnClickListener {
                    BarChartVariables.setSecondTargetData("ex_count", null)
                    selectedDataArea.text = "운동 횟수"

                    checkIsChartInitialization()
                    showChart()
                }

                R.id.btnShowExTimeChart_ -> btnSetExTimeChart.setOnClickListener {
                    BarChartVariables.setSecondTargetData("ex_time", null)
                    selectedDataArea.text = "운동 시간"

                    checkIsChartInitialization()
                    showChart()

                }
            }
        }


        /*
            이번 주, 지난 주, 다음 주 차트 보기 
         */
        btnShowThisWeek.setOnClickListener { // 이번 주(1주일 전 ~ 오늘) 차트보기

            BarChartVariables.dateListOfWeek = DataBasket.getDateListOfThisWeek()
            showChart()

        }

        btnShowPreviousWeek.setOnClickListener {

            // 마지막에 저장된 X Index를 이용하여 일주일 전의 dateListOfTargetWeek을 생성
            if (BarChartVariables.lastDateOfXIndex == null) {
                Toast.makeText(context, "운동 종류와 데이터를 선택해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else {
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

                showChart()
            }

        }

        btnShowNextWeek.setOnClickListener {

            // 마지막에 저장된 X Index를 이용하여 일주일 후의 dateListOfTargetWeek을 생성
            if (BarChartVariables.lastDateOfXIndex == null) {
                Toast.makeText(context, "운동 종류와 데이터를 선택해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else {
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

                showChart()
            }

        }
        return view
    }

    private fun checkIsChartInitialization() {

//        var returnValue = false

        if (BarChartVariables.isFirstBarChart) {
            BarChartVariables.isFirstBarChart = false
            BarChartVariables.dateListOfWeek = DataBasket.getDateListOfThisWeek()
//            returnValue = true
        }

    }

//    private fun isFirstExTypeSelection(): Boolean {
//        if (BarChartVariables.isFirstBarChart) {
//            BarChartVariables.isFirstBarChart = false
//            BarChartVariables.dateListOfWeek = DataBasket.getDateListOfThisWeek()
//        }
//
//        return true
//    }

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