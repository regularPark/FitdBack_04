package com.fitdback.userinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R
import com.fitdback.test.barChartTest.BarChartTestActivity

class LoadingActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val dbPath = DataBasket.getDBPath("users", "ex_data", true)
        DataBasket.getDataFromFB(dbPath!!, "individualExData")
        Log.d("데베", dbPath.toString())

        Handler().postDelayed({
            startActivity(Intent(this, MainTestActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}