package com.fitdback.test.barChartTest

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.content.ContextCompat
import com.fitdback.posedetection.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.lang.Exception

@SuppressLint("LogNotTimber")
class BarChartGenerator2() {

    companion object {

        val testXLabels = mutableListOf<String>()

        fun initXLabels(xLabels: MutableList<String>) {

            xLabels.add(0, "월")
            xLabels.add(1, "화")
            xLabels.add(2, "수")
            xLabels.add(3, "목")
            xLabels.add(4, "금")
            xLabels.add(5, "토")
            xLabels.add(6, "일")
            Log.d("BarChartGenerator", "$xLabels")

        }
    }

    fun runBarChart(barChart: BarChart) {

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
            axisLeft.axisMaximum = 100f
            axisLeft.textColor = ContextCompat.getColor(context, R.color.white) // 라벨 텍스트 컬러 설정
            axisLeft.axisLineWidth = 2.0f

            // x축 설정
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.axisLineWidth = 5.0f
            xAxis.textColor = ContextCompat.getColor(context, R.color.white) // 라벨 텍스트 컬러 설정
            xAxis.setDrawAxisLine(true) // 축 그리기
            xAxis.valueFormatter = MyXAxisFormatter()

            //add animation
//            animateXY(0, 1000)
            animateY(800)

        }

        //draw chart
        barChart.invalidate()
    }

    inner class MyXAxisFormatter : IndexAxisValueFormatter() {

        private val testSample: MutableList<String> = testXLabels

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {

            Log.d("testSample", testSample.toString())
            val index = value.toInt()
            Log.d("ValueFormatter", "getAxisLabel: index$index")

            try {
                Log.d("ValueFormatter", "getAxisLabel: index${testSample[index - 1]}")
            } catch (e: Exception) {
                Log.d("ValueFormatter", "getAxisLabel: Error${e}")
            }

            return if (index - 1 < testSample.size) {
                testSample[index - 1]
            } else {
                "i am null"
            }

        }

    }
}


