package com.fitdback.test

import android.annotation.SuppressLint
import android.util.Log
import com.fitdback.database.DataBasket

@SuppressLint("LogNotTimber")
fun main() {

//    val dateOfWeekList = DataBasket.getDateOfWeek()
//
//    val exDate = "220505"
//
//    println(exDate)
//
//    val exCountMap = mutableMapOf<String, Int>()
//    exCountMap[exDate] = 3
//    println("${exCountMap.javaClass}")
//    println("${exCountMap[exDate]?.javaClass}")
//    val path = exCountMap[exDate]
//
//    if (exCountMap[exDate] == null) {
//        println("null")
//    } else {
//        println(exCountMap[exDate])
//        exCountMap[exDate] = exCountMap[exDate]!! + 4
//        println(exCountMap[exDate])
//    }
//
//    exCountMap["220503"] = 7
//    println(exCountMap.toString())
//
//    val text1 = "220403"
//
//    when(text1) {
//
//        "220403" -> println("true")
//        "220404" -> println("false")
//
//    }

    val testXLabels = mutableListOf<String>()
    initXLabels(testXLabels)
    println(testXLabels.size)

}

@SuppressLint("LogNotTimber")
fun initXLabels(xLabels: MutableList<String>) {

//        for (i in 0.. 6) {
//            xLabels.add(i, )
//        }
    xLabels.add(0, "월")
    xLabels.add(1, "화")
    xLabels.add(2, "수")
    xLabels.add(3, "목")
    xLabels.add(4, "금")
    xLabels.add(5, "토")
    xLabels.add(6, "일")

}