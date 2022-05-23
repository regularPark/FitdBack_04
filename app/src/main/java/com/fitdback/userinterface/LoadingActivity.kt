package com.fitdback.userinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R

class LoadingActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity_new::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}