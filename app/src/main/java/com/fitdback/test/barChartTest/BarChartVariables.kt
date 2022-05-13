package com.fitdback.test.barChartTest

import com.fitdback.database.DataBasket
import com.github.mikephil.charting.formatter.ValueFormatter

class BarChartVariables {

    companion object {

        var lastDateOfXIndex: String = "" // 차트 X축 인덱스의 가장 마지막 날짜, "yyMMdd"
        lateinit var dateListOfWeek: MutableList<String>
        var firstTargetData: String? =
            null // firstTargetData: "squat" or "plank" or "sideLateralRaise"
        var secondTargetData: String? =
            null // secondTargetData: "ex_count" or "ex_calorie" or "ex_time"

        val vf: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value.toInt() == 0) {
                    ""
                } else {
                    "" + value.toInt()
                }
            }
        }

    }

}