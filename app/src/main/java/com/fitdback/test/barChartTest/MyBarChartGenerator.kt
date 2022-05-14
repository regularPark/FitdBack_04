package com.fitdback.test.barChartTest

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.content.ContextCompat
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.lang.StringBuilder

@SuppressLint("LogNotTimber")
class MyBarChartGenerator() {

    fun runBarChart(barChart: BarChart, yMax: Float) {

        barChart.apply { // barChart 설정

            //hide grid lines
            axisLeft.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)

            //remove right y-axis
            axisRight.isEnabled = false

            //remove legend
            legend.isEnabled = false  // 하단 범례 보이기 여부

            //remove description label
            description.isEnabled = false // 차트 우측 하단 Description Label 여부

            // y축 설정
            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = yMax + 1f
            axisLeft.textColor = ContextCompat.getColor(context, R.color.black) // 라벨 텍스트 컬러 설정
            axisLeft.axisLineWidth = 2.0f
            axisLeft.valueFormatter = BarChartVariables.MyYAxisFormatter

            // x축 설정
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.axisLineWidth = 5.0f
            xAxis.textColor = ContextCompat.getColor(context, R.color.black) // 라벨 텍스트 컬러 설정
            xAxis.setDrawAxisLine(true) // 축 그리기
            xAxis.textSize = 12.0f
            xAxis.valueFormatter = MyXAxisFormatter()

            //add animation
            animateY(1000)

        }

        //draw chart
        barChart.invalidate()
    }

    inner class MyXAxisFormatter : IndexAxisValueFormatter() {

        private val targetList: MutableList<String> = BarChartVariables.dateListOfWeek

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {

//            Log.d("BarChart", "MyXAxisFormatter - targetList $targetList")
            val index = value.toInt()
            val tempValue = targetList[index - 1] // IndexOutOfRange Error 주의

            // x축 인덱스 가공. 예) "220508" -> "05.08"
            val stringBuilder = StringBuilder()
            stringBuilder.append(tempValue.slice(IntRange(2, 3))) // "220508" -> "05"
            stringBuilder.append(".")
            stringBuilder.append(tempValue.slice(IntRange(4, 5))) // "220508" -> "08"

//            try {
//                Log.d("ValueFormatter", "getAxisLabel: index${targetList[index - 1]}")
//            } catch (e: Exception) {
//                Log.d("ValueFormatter", "getAxisLabel: Error${e}")
//            }

            return if (index - 1 < targetList.size) {
//                testSample[index - 1]
                stringBuilder.toString()
            } else {
                "i am null"
            }

        }

    }
}

