package com.fitdback.test

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("LogNotTimber", "SimpleDateFormat")
fun main() {

    val cal = GregorianCalendar(2022, 4, 3);
//    val today = GregorianCalendar();
    println(cal.time)

    val simpleDateFormat = SimpleDateFormat("yyMMdd")
//    println("today: " + simpleDateFormat.format(today.time))
    println(simpleDateFormat.format(cal.time))

    cal.add(GregorianCalendar.DATE, -1)
//    println("today: " + simpleDateFormat.format(today.time))
    println(simpleDateFormat.format(cal.time))

    for (i in 0..6) {
        cal.add(GregorianCalendar.DATE, -1)
        println(simpleDateFormat.format(cal.time).toString())
    }

}

