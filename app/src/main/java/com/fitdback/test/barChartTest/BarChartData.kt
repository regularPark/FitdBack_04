package com.fitdback.test.barChartTest

import com.fitdback.database.DataBasket

class BarChartData {

    companion object {

        var lastDateOfXIndex: String = "" // 차트 X축 인덱스의 가장 마지막 날짜, "yyMMdd"
        lateinit var dateListOfWeek: MutableList<String>

    }

}