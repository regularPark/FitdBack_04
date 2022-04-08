package com.fitdback.userinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.fitdback.posedetection.CameraActivity
import com.fitdback.posedetection.R

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.*

class TestLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_login)

        // 해당 라인 'Firebase.' 뒤의 'auth'가 자동 import 되지 않고 빨간줄로 표시 됨.
        // 팝업 메시지 : Unresolved reference: auth
//        auth = Firebase.auth

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnjoin = findViewById<Button>(R.id.btnJoin)
        val btnAnonymousLogin = findViewById<Button>(R.id.btnAnonymousLogin)

        val toCameraIntent = Intent(this, CameraActivity::class.java)

        btnLogin.setOnClickListener {

            val email = findViewById<EditText>(R.id.areaID)
            val password = findViewById<EditText>(R.id.areaPassword)

            Log.d("tla", email.text.toString())
            Log.d("tla", password.text.toString())

            startActivity(toCameraIntent)

        }

        btnAnonymousLogin.setOnClickListener {

            Toast.makeText(this, "Anonymous Login", Toast.LENGTH_LONG).show()

            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        Log.d("TestLogin", user!!.uid)

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }

    }
}