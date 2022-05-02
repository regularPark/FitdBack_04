package com.fitdback.userinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.fitdback.posedetection.CameraActivity
import com.fitdback.posedetection.R

class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // 레이아웃
        val btnFitnessStart = findViewById<Button>(R.id.btnFitnessStart)

        // 운동정보 넘기기
        val exr = intent.getStringExtra("exr_mod")

        // 인텐트
        val intent = Intent(this, CameraActivity::class.java)

        // btnFitnessStart 버튼을 클릭하여 CameraActivity 실행
        btnFitnessStart.setOnClickListener {
            intent.putExtra("exr_mod",exr)
            startActivity(intent)
        }

    }
}