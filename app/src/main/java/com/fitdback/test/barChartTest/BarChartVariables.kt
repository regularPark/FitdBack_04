package com.fitdback.test.barChartTest

import android.graphics.Color
import android.widget.TextView
import com.fitdback.database.DataBasket
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot

class BarChartVariables {

    companion object {

        var lastDateOfXIndex: String? = null // 차트 X축 인덱스의 가장 마지막 날짜, "yyMMdd"
        lateinit var dateListOfWeek: MutableList<String>
        var firstTargetData: String? =
            null // firstTargetData: "squat" or "plank" or "sideLateralRaise"
        var secondTargetData: String? =
            null // secondTargetData: "ex_count" or "ex_calorie" or "ex_time"
        var isFirstBarChart = true

        val expressedDataFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {

                if (secondTargetData == "ex_time") {
                    val time = value.toInt()
                    val minute = time / 60
                    val sec = time % 60
                    return if (time == 0) {
                        ""
                    } else {
                        "${minute}분 ${sec}초"
                    }
                } else {
                    return if (value.toInt() == 0) {
                        ""
                    } else {
                        "" + value.toInt()
                    }
                }

            }
        }

        val MyYAxisFormatter: ValueFormatter = object : ValueFormatter() {

            override fun getAxisLabel(value: Float, axis: AxisBase?): String {

                return if (secondTargetData == "ex_time") {
                    if ((value.toInt() % 60) == 0) {
                        (value.toInt() / 60).toString()
                    } else {
                        ""
                    }
                } else {
                    return if (value.toInt() == 0) {
                        ""
                    } else {
                        "" + value.toInt()
                    }
                }

            }

        }


        // barChart 조작
        fun clearBarChart(barChart: BarChart) {
            // 오래된 데이터 삭제
            barChart.clear()
            if (!barChart.isEmpty) { // 기존 데이터가 있으면 clear
                barChart.clearValues()
            }
        }

        fun setYAxisTitle(yAxisTitleArea: TextView) {
            yAxisTitleArea.text = when (secondTargetData) {
                "ex_count" -> "개수"
                "ex_calorie" -> "kcal"
                "ex_time" -> "시간"
                else -> "오류"
            }
        }

        fun setFeedbackYAxisTitle(yAxisTitleArea: TextView) {
            yAxisTitleArea.text = when (secondTargetData) {
                "ex_count" -> "개수"
                "ex_calorie" -> "kcal"
                "ex_time" -> "시간"
                else -> "오류"
            }
        }

        fun setFirstTargetData(firstTargetData: String, selectedExTypeArea: TextView?) {
            BarChartVariables.firstTargetData = firstTargetData
            selectedExTypeArea?.text = BarChartVariables.firstTargetData
        }

        fun setSecondTargetData(secondTargetData: String, selectedDataArea: TextView?) {
            BarChartVariables.secondTargetData = secondTargetData
            selectedDataArea?.text = BarChartVariables.secondTargetData
        }

        fun updateBarChartData(dateListOfTargetWeek: MutableList<String>) {
            dateListOfWeek =
                dateListOfTargetWeek // BarChartVariables.dateListOfWeek 변수가 lateinit으로 선언된 것에 주의!
            lastDateOfXIndex = dateListOfTargetWeek[6]
        }

        fun getDailySumBarEntry(
            dateList: MutableList<String>,
            firstTargetData: String,
            secondTargetData: String,
            whoseData: DataSnapshot
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
                    whoseData,
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

        fun setExpressedDataFormat(barDataSet: BarDataSet) {

            barDataSet.apply {

                valueTextColor = Color.BLACK
                valueTextSize = 10f
                setColors(*ColorTemplate.COLORFUL_COLORS)
                valueFormatter = expressedDataFormatter // 데이터 소수점 표기 -> 정수 표기

            }

        }


        fun getYearMonthDateOfLastDate(lastDateOfXIndex: String): Triple<Int, Int, Int> {
            val year: Int = lastDateOfXIndex.slice(0..1).toInt() + 2000 // 22 + 2000 = 2022
            val month: Int =
                lastDateOfXIndex.slice(2..3).toInt() - 1 // Gregorian Calendar 사용시 month 주의
            val date: Int = lastDateOfXIndex.slice(4..5).toInt()
            return Triple(year, month, date)
        }
    }

}