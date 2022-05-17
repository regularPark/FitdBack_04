package com.fitdback.database

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.fitdback.database.datamodel.ExerciseDataModel
import com.fitdback.database.datamodel.UserInfoDataModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
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
        lateinit var googleSignInClient: GoogleSignInClient
        val database = Firebase.database

        var tempExrModel = ExerciseDataModel()
        var dataSample: DataSnapshot? = null
        var individualExData: DataSnapshot? = null
        
        /*
            날짜 관련
        */
        @SuppressLint("SimpleDateFormat")
        fun getDateOfDay(days: Int): String? { // "yyMMdd"형태로 원하는 날짜를 반환, 최대 30일 전까지 변환

            val now = System.currentTimeMillis()
            val daysAgo = now - (days * 24 * 60 * 60 * 1000)
            val dateOfDay = Date(daysAgo)
            val dateFormat = SimpleDateFormat("yyMMdd")

            return dateFormat.format(dateOfDay)

        }

        @SuppressLint("SimpleDateFormat")
        fun getDateOfOneWeekBeforeOrTomorrow(
            year: Int,
            month: Int,
            date: Int,
            BeforeOrTomorrow: String
        ): String {

            val cal = GregorianCalendar(year, month, date) // month: 0~11
            val simpleDateFormat = SimpleDateFormat("yyMMdd")

            if (BeforeOrTomorrow == "Before") {
                cal.add(GregorianCalendar.DATE, -7)
            }

            Log.d(
                "Date",
                "DataBasket.getDateOneWeekBefore() : ${simpleDateFormat.format(cal.time)}"
            )
            return simpleDateFormat.format(cal.time)

        }

        fun getDateListOfThisWeek(): MutableList<String> {

            val dateOfWeekList = mutableListOf<String>()

            for (i in 6 downTo 0) {
                dateOfWeekList.add(getDateOfDay(i)!!)
            }

            return dateOfWeekList

        }

        @SuppressLint("SimpleDateFormat")
        fun getOneWeekListFromDate(
            year: Int,
            month: Int,
            date: Int,
            BeforeOrAfter: String
        ): MutableList<String> {

            val dateList = mutableListOf<String>()

            val cal = GregorianCalendar(year, month, date) // month: 0~11
            val simpleDateFormat = SimpleDateFormat("yyMMdd")

            if (BeforeOrAfter == "Before") { // "Before" 이면 6일전~오늘(7일)로 셋팅
                cal.add(GregorianCalendar.DATE, -7)
            }

            for (i in 0..6) {
                cal.add(GregorianCalendar.DATE, +1)
                dateList.add(simpleDateFormat.format(cal.time))
            }

            return dateList

        }

        fun getDBPath(
            node1: String,
            node2: String,
            isUsingUserId: Boolean
        ): DatabaseReference? {

            var databaseRef: DatabaseReference? = null
            firebaseAuth = FirebaseAuth.getInstance()

            if (isUsingUserId) { // 개인별 데이터
                databaseRef =
                    database.getReference(node1).child(firebaseAuth.currentUser!!.uid).child(node2)
            } else {
                // TODO : 전체 회원의 child 데이터 경로
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
            dateOfWeek: MutableList<String>,
            firstTargetData: String,
            secondTargetData: String
        ): MutableMap<String, Int> {

            Log.d("Data - dateOfWeek", "$dateOfWeek")
            val dailySum = mutableMapOf<String, Int>()

            // dailySum Map을 (날짜, 0) 형태로 initialization
            for (i in 0..6) {
                updateMap(dailySum, dateOfWeek[i], 0, true)
                Log.d("Data - dailySum Init", dailySum.toString())
            }

            /*
            dataSnapshot: 이미 로드한 Firebase DataSnapshot (users/ex_data)
            firstTargetData: ex_type 중 하나. "squat", "plank", "sideLateralRaise"
            secondtargetData: "ex_count", "ex_calorie", "ex_time"
             */
            calculateDailySum(dataSnapshot, dateOfWeek, firstTargetData, secondTargetData, dailySum)

            Log.d("Data - dailySum ", dailySum.toString())
            return dailySum

        }

        private fun calculateDailySum(
            dataSnapshot: DataSnapshot,
            dateOfWeek: MutableList<String>,
            firstTargetData: String,
            secondTargetData: String,
            dailySum: MutableMap<String, Int>
        ) {
            for (exDataSet in dataSnapshot.children) {

                val exData = exDataSet.getValue(ExerciseDataModel::class.java)

//                Log.d("Data - exData.ex_date", exData!!.ex_date)
//                Log.d("Data - exData.ex_count", exData.ex_count.toString())
                // exCountMap에 <"yyMMdd", sumOfExCount> key-value 형태로 update
                if (exData!!.ex_type.equals(firstTargetData)) { // firstTargetData: ex_type 중 하나. "squat", "plank", "sideLateralRaise"

                    var targetInt = 0

                    when (secondTargetData) {
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

        /*
            로그인 처리 관련
         */
        fun addUserInfoDataModel(dataModel: UserInfoDataModel): Boolean {

            val dbPath = getDBPath("users", "user_info", true)
            var isJoinSuccessful: Boolean = true

            dbPath!!.setValue(dataModel)
                .addOnSuccessListener {
                    isJoinSuccessful = true
                }
                .addOnFailureListener {
                    isJoinSuccessful = false
                }

            return isJoinSuccessful
        }

    } // end of companion object
}



