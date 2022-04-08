package com.fitdback.userinterface

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fitdback.posedetection.CameraActivity
import com.fitdback.posedetection.R

// Firebase
import com.google.firebase.auth.FirebaseAuth


class TestLoginActivity : AppCompatActivity() {

    //    private lateinit var auth: FirebaseAuth
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_login)

        // 해당 라인 'Firebase.' 뒤의 'auth'가 자동 import 되지 않고 빨간줄로 표시 됨. 팝업 메시지 : Unresolved reference: auth
//        auth = Firebase.auth

        firebaseAuth = FirebaseAuth.getInstance() // auth = Firebase.auth 대체

        // 레이아웃
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnjoin = findViewById<Button>(R.id.btnJoin)
        val btnAnonymousLogin = findViewById<Button>(R.id.btnAnonymousLogin)

        // Intent
        val toCameraAcitivityIntent = Intent(this, CameraActivity::class.java)
        val toLoginSuccessActivityIntent = Intent(this, LoginSuccessActivity::class.java)

        // 로그인 버튼 클릭 시 동작
        btnLogin.setOnClickListener {

            // 레이아웃의 EditText 에서 email과 password를 읽어들인다.
            val email = findViewById<EditText>(R.id.areaID)
            val password = findViewById<EditText>(R.id.areaPassword)

            emailLoginAuth(
                email.text.toString(),
                password.text.toString(),
                toLoginSuccessActivityIntent
            )
//            Log.d("TestLogin", email.text.toString())

        }

        // Anonymous 버튼 클릭시 동작
        btnAnonymousLogin.setOnClickListener {

            anonymousAuth(toLoginSuccessActivityIntent)

        }

    } // end of onCreate()

    private fun emailLoginAuth(email: String, password: String, intent: Intent) { // 이메일 로그인 인증

//        Toast.makeText(this, "email: ${email}, pw: ${password}", Toast.LENGTH_LONG).show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 이메일 로그인 성공

                    Toast.makeText(this, "이메일 로그인 성공", Toast.LENGTH_LONG).show()
                    startActivity(intent) // LoginSuccessActivity로 이동

                } else { // 실패

                    Toast.makeText(this, "이메일 로그인 실패", Toast.LENGTH_LONG).show()

                }
            }
//            .addOnSuccessListener {
//                Toast.makeText(this, "이메일 로그인 성공", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, firebaseAuth.currentUser?.uid.toString(), Toast.LENGTH_LONG).show()
//                Toast.makeText(this, "이메일 로그인 실패", Toast.LENGTH_LONG).show()
//            }

    }

    private fun anonymousAuth(intent: Intent) { // 익명 로그인 인증

        firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                Toast.makeText(
                    this, "익명 로그인 성공",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "익명 로그인 실패",
                    Toast.LENGTH_LONG
                ).show()
            }

    }
} // end of TestLoginActivity

//            firebaseAuth!!.createUserWithEmailAndPassword(
//                email.text.toString(),
//                password.text.toString()
//            ).addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = firebaseAuth!!.currentUser
//
//                    Toast.makeText(
//                        baseContext, "Authentication Success.",
//                        Toast.LENGTH_LONG
//                    ).show()
//
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(
//                        baseContext, "Authentication failed.",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
////            startActivity(toCameraIntent)


// 익명 로그인 버튼 클릭시 동작
//        btnAnonymousLogin.setOnClickListener {
//
//            Toast.makeText(this, "Anonymous Login", Toast.LENGTH_LONG).show()
//
//            auth.signInAnonymously()
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//
//                        val user = auth.currentUser
//
//                        Toast.makeText(
//                            baseContext, "Authentication Success.",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Toast.makeText(
//                            baseContext, "Authentication failed.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//        }