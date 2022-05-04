package com.fitdback.userinterface

import android.util.Log
import java.util.*
import kotlin.concurrent.timerTask

class TimerClass {
    companion object{
        var second = 6;
        private var timerTask: Timer? = null

        // 타이머 시작 함수
        fun cdStart(){
            timerTask = kotlin.concurrent.timer(period = 1000){
                if(second <= 0){
                    timerTask!!.cancel()
                }
                else {
                    second--
                }
                Log.d("시간", second.toString())

            }
        }

        // 타이머 종료 함수
        fun cdStop() {
            second = 6
            timerTask!!.cancel()
        }
    }
}