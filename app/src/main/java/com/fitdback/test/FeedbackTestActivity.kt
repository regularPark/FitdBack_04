package com.fitdback.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fitdback.posedetection.R

/*
넘어온 데이터에 현재 날짜, 시간 정보를 더해서 데이터 DB로 넘길 것임

 */
class FeedbackTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_test)
    }
}