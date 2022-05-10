package com.fitdback.database

import android.annotation.SuppressLint
import android.util.Log
import com.fitdback.database.datamodel.ExerciseDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("LogNotTimber")
class DataBasket {

    companion object {

        private lateinit var firebaseAuth: FirebaseAuth
        val database = Firebase.database

        var tempExrModel = ExerciseDataModel()
        var dataSample: DataSnapshot? = null
        var individualExData: DataSnapshot? = null

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


        fun getDBPath(
            node1: String,
            node2: String,
            isUsingUserId: Boolean
        ): DatabaseReference? {

            var databaseRef: DatabaseReference? = null
            firebaseAuth = FirebaseAuth.getInstance()

            if (isUsingUserId) {
                databaseRef =
                    database.getReference(node1).child(firebaseAuth.currentUser!!.uid).child(node2)
            }

            return databaseRef

        }

        fun getDataFromFB(
            dbPath: DatabaseReference,
            dataDescription: String
        ) { // get Data From Firebase

            dbPath.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSample = dataSnapshot

                    when (dataDescription) {
                        "individualExData" -> individualExData = dataSnapshot
                    }

                    Log.d("Data - getDataFromDB", dataSample.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Data", "onCancelled")
                }
            })

        }

        fun enhancedGetDailySum(
            dataSnapshot: DataSnapshot,
            targetData: String
        ): MutableMap<String, Int> {

            val dateOfWeek = DataBasket.getDateOfWeek()
            Log.d("Data - dateOfWeek", "$dateOfWeek")
            val dailySum = mutableMapOf<String, Int>()

            for (i in 0 .. 6) {
                updateMap(dailySum, dateOfWeek[i], 0, true)
                Log.d("Data - dailySum Init", dailySum.toString())
            }

            calculateDailySum(dataSnapshot, targetData, dateOfWeek, dailySum)

            Log.d("Data - dailySum ", dailySum.toString())
            return dailySum

        }

        private fun calculateDailySum(
            dataSnapshot: DataSnapshot,
            targetData: String,
            dateOfWeek: MutableList<String>,
            dailySum: MutableMap<String, Int>
        ) {
            for (exDataSet in dataSnapshot.children) {

                val exData = exDataSet.getValue(ExerciseDataModel::class.java)

//                Log.d("Data - exData.ex_date", exData!!.ex_date)
//                Log.d("Data - exData.ex_count", exData.ex_count.toString())
                // exCountMap에 <"yyMMdd", sumOfExCount> key-value 형태로 update
                if (exData!!.ex_type.equals("squat")) {

                    var targetInt = 0

                    when (targetData) {
                        "ex_count" -> targetInt = exData.ex_count
                        "ex_calorie" -> targetInt = exData.ex_calorie
                        "ex_time" -> targetInt = exData.ex_time
                    }

                    when (exData.ex_date) {

                        dateOfWeek[0] -> {
                            updateMap(dailySum, dateOfWeek[0], targetInt, false)
                        }
                        dateOfWeek[1] -> {
                            updateMap(dailySum, dateOfWeek[1], targetInt, false)
                        }
                        dateOfWeek[2] -> {
                            updateMap(dailySum, dateOfWeek[2], targetInt, false)
                        }
                        dateOfWeek[3] -> {
                            updateMap(dailySum, dateOfWeek[3], targetInt, false)
                        }
                        dateOfWeek[4] -> {
                            updateMap(dailySum, dateOfWeek[4], targetInt, false)
                        }
                        dateOfWeek[5] -> {
                            updateMap(dailySum, dateOfWeek[5], targetInt, false)
                        }
                        dateOfWeek[6] -> {
                            updateMap(dailySum, dateOfWeek[6], targetInt, false)
                        }

                    }

                }

            }
        }

        private fun updateMap(
            targetMap: MutableMap<String, Int>,
            key: String,
            value: Int,
            isInitialization: Boolean
        ) {

            if (isInitialization) {
                targetMap[key] = value
            } else {
                targetMap[key] = targetMap[key]!! + value // update
            }

        }

    } // end of companion object
}



