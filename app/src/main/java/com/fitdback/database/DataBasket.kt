package com.fitdback.database

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DataBasket {

    companion object {

        var tempExrModel = ExerciseDataModel()

        fun getDateOfToday(): String? { // "yyMMdd"형태로 오늘 날짜를 반환

            val now = System.currentTimeMillis()
            val dateOfToday = Date(now)
            val dateFormat = SimpleDateFormat("yyMMdd")

            return dateFormat.format(dateOfToday)

        }

    }

}