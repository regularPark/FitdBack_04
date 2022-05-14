package com.fitdback.test.barChartTest

import com.fitdback.database.DataBasket
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class BarChartVariables {

    companion object {

        var lastDateOfXIndex: String = "" // 차트 X축 인덱스의 가장 마지막 날짜, "yyMMdd"
        lateinit var dateListOfWeek: MutableList<String>
        var firstTargetData: String? =
            null // firstTargetData: "squat" or "plank" or "sideLateralRaise"
        var secondTargetData: String? =
            null // secondTargetData: "ex_count" or "ex_calorie" or "ex_time"

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


        val yAxisTitleList: Array<String> = arrayOf("kcal", "개수", "초")

    }

}