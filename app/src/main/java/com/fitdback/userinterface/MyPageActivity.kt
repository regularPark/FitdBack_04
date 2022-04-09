package com.fitdback.userinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.fitdback.posedetection.R

class MyPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        // 레이아웃
        val loginInfoBtn = findViewById<Button>(R.id.loginInfoBtn)
        val statisticBtn = findViewById<Button>(R.id.statBtn)

        // 인텐트
        val toLoginInfoActivityIntent = Intent(this, UserInformationActivity::class.java)
        val toStaticsPopupActivityIntent = Intent(this, StatisticsPopUpActivity::class.java)

        loginInfoBtn.setOnClickListener{
            startActivity(toLoginInfoActivityIntent)
        }

        statisticBtn.setOnClickListener{
            startActivity(toStaticsPopupActivityIntent)
        }

    }
}