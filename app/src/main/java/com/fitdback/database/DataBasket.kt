package com.fitdback.database

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DataBasket {

    companion object {

        var tempExrModel = ExerciseDataModel()

        @SuppressLint("SimpleDateFormat")
        fun getDateOfDay(days: Int): String? { // "yyMMdd"형태로 원하는 날짜를 반환, 최대 30일 전까지 변환

            val now = System.currentTimeMillis()
            val daysAgo = now - (days * 24 * 60 * 60 * 1000)
            val dateOfDay = Date(daysAgo)
            val dateFormat = SimpleDateFormat("yyMMdd")

            return dateFormat.format(dateOfDay)

        }

    }

}