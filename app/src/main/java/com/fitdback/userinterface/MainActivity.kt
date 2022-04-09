package com.fitdback.userinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.fitdback.posedetection.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        // 레이아웃
        val squatBtn = findViewById<Button>(R.id.squatBtn)
        val puBtn = findViewById<Button>(R.id.puBtn)
        val plkBtn = findViewById<Button>(R.id.plkBtn)
        val myPageBtn = findViewById<Button>(R.id.mypageBtn)
        val btnSignOut = findViewById<Button>(R.id.btnSignOut)

        // 인텐트
        val toTutorialActivity = Intent(this, TutorialActivity::class.java)
        val toLoginActivity = Intent(this, LoginActivity::class.java)

        squatBtn.setOnClickListener {
            startActivity(toTutorialActivity)
        }

        puBtn.setOnClickListener {
            startActivity(toTutorialActivity)
        }

        plkBtn.setOnClickListener {
            startActivity(toTutorialActivity)
        }

        myPageBtn.setOnClickListener {
            startActivity(toTutorialActivity)
        }

        // Sign Out
        btnSignOut.setOnClickListener {

            signOut(toLoginActivity)

        }

    } // end of MainActivity

    private fun signOut(intent: Intent) {

        firebaseAuth.signOut() // 로그아웃 처리
        Toast.makeText(this, "로그아웃 완료", Toast.LENGTH_SHORT).show()

        startActivity(intent)
        finish()

    }

}