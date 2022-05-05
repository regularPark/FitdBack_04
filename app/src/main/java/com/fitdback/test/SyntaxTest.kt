package com.fitdback.test

import com.fitdback.database.DataBasket

fun main() {

    val dateOfWeekList = DataBasket.getDateOfWeek()

    val exDate = "220505"

    println(exDate)

    val exCountMap = mutableMapOf<String, Int>()
    exCountMap[exDate] = 3
    println("${exCountMap.javaClass}")
    println("${exCountMap[exDate]?.javaClass}")
    val path = exCountMap[exDate]

    if (exCountMap[exDate] == null) {
        println("null")
    } else {
        println(exCountMap[exDate])
        exCountMap[exDate] = exCountMap[exDate]!! + 4
        println(exCountMap[exDate])
    }

    exCountMap["220503"] = 7
    println(exCountMap.toString())

    val text1 = "220403"

    when(text1) {

        "220403" -> println("true")
        "220404" -> println("false")

    }

}