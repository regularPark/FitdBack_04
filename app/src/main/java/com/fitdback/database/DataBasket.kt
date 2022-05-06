package com.fitdback.database

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("LogNotTimber")
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
                databaseRef =
                    database.getReference(node1).child(firebaseAuth.currentUser!!.uid).child(node2)
            }

            return databaseRef

        }

        fun getDailySum(dbPath: DatabaseReference): MutableMap<String, Int> {

            val dateOfWeek = DataBasket.getDateOfWeek()
            val dailySum = mutableMapOf<String, Int>()

            dbPath.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (exDataSet in dataSnapshot.children) {

                        val exData = exDataSet.getValue(ExerciseDataModel::class.java)

                        // exCountMap에 <"yyMMdd", sumOfExCount> key-value 형태로 update
                        if (exData!!.ex_type.equals("squat")) {

                            val exCount = exData.ex_count

                            when (exData.ex_date) {

                                dateOfWeek[0] -> {
                                    updateMap(dailySum, dateOfWeek[0], exCount)
                                }
                                dateOfWeek[1] -> {
                                    updateMap(dailySum, dateOfWeek[1], exCount)
                                }
                                dateOfWeek[2] -> {
                                    updateMap(dailySum, dateOfWeek[2], exCount)
                                }
                                dateOfWeek[3] -> {
                                    updateMap(dailySum, dateOfWeek[3], exCount)
                                }
                                dateOfWeek[4] -> {
                                    updateMap(dailySum, dateOfWeek[4], exCount)
                                }
                                dateOfWeek[5] -> {
                                    updateMap(dailySum, dateOfWeek[5], exCount)
                                }
                                dateOfWeek[6] -> {
                                    updateMap(dailySum, dateOfWeek[6], exCount)
                                }

                            }

                        }

                    }

                    Log.d("exData", dateOfWeek.toString())
                    Log.d("exData", dailySum.toString())


                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("exData", "DB Read Error")
                }
            })

            return dailySum

        }

    }
}

