package com.fitdback.database

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

        fun getDateOfWeek(): MutableList<String> {

            val dateOfWeekList = mutableListOf<String>()

            for (i in 6 downTo 0) {
                dateOfWeekList.add(getDateOfDay(i)!!)
            }

            return dateOfWeekList

        }

        fun updateMap(targetMap: MutableMap<String, Int>, key: String, value: Int) {

            if (targetMap.containsKey(key)) {
                targetMap[key] = targetMap[key]!! + value // update
            } else {
                targetMap[key] = value // put(key, value)
            }

        }

        fun getDBPath(
            firebaseAuth: FirebaseAuth,
            database: FirebaseDatabase,
            node1: String,
            node2: String,
            isUsingUserId: Boolean
        ): DatabaseReference? {

            var databaseRef: DatabaseReference? = null

            if (isUsingUserId) {
                databaseRef = database.getReference(node1).child(firebaseAuth.currentUser!!.uid).child(node2)
            }

            return databaseRef

        }


    }

}