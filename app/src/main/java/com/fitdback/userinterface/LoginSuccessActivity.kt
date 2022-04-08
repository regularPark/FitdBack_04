package com.fitdback.userinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.fitdback.posedetection.CameraActivity
import com.fitdback.posedetection.R
import com.google.firebase.auth.FirebaseAuth

class LoginSuccessActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_success)

        firebaseAuth = FirebaseAuth.getInstance()


        // 레이아웃 연결
        val currentUserIDArea = findViewById<TextView>(R.id.currentUserIDArea)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnFitnessStart = findViewById<Button>(R.id.btnFitnessStart)

        // Intent
        val toTestLoginActivityIntent = Intent(this, TestLoginActivity::class.java)
        val toCameraActivityIntent = Intent(this, CameraActivity::class.java)

        // 로그인에 성공해서 현재 액티비티로 전환되면, user의 ID를 화면에 표시
        currentUserIDArea.text = firebaseAuth.currentUser?.uid

        // LOGOUT 버튼을 클릭하면 로그아웃 처리를 하고 TestLoginActivity로 돌아감
        btnLogout.setOnClickListener {

            signOut(toTestLoginActivityIntent)

        }

        // FITNESS START 버튼을 클릭하면 카메라 실행
        btnFitnessStart.setOnClickListener {

            startActivity(toCameraActivityIntent)

        }

    } // eno of onCreate

    private fun signOut(intent: Intent) {

        firebaseAuth.signOut() // 로그아웃 처리
        startActivity(intent)
        finish()

    }

} // end of Activity