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

        // 인텐트
        val toLoginInfoActivityIntent = Intent(this, UserInformationActivity::class.java)

        loginInfoBtn.setOnClickListener{
            startActivity(toLoginInfoActivityIntent)
        }


    }
}