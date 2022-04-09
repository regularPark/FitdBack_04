package com.fitdback.userinterface

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fitdback.posedetection.CameraActivity
import com.fitdback.posedetection.R

// Firebase
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception


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
            val email = findViewById<EditText>(R.id.areaID).text.toString().trim()
            val password = findViewById<EditText>(R.id.areaPassword).text.toString().trim()

            if (checkIfBothEmailAndPasswordAreNotNull(email, password)) {
                emailLoginAuth(
                    email,
                    password,
                    toLoginSuccessActivityIntent
                )
            } else {
                Toast.makeText(this, "이메일과 비밀번호를 모두 입력하세요", Toast.LENGTH_SHORT).show()
            }
//            Log.d("TestLogin", email.text.toString())

        }

        // 회원가입 버튼 클릭 시 동작
        btnjoin.setOnClickListener {

            // 레이아웃의 EditText 에서 email과 password를 읽어들인다.
            val email = findViewById<EditText>(R.id.areaID).text.toString().trim()
            val password = findViewById<EditText>(R.id.areaPassword).text.toString().trim()

            if (checkIfBothEmailAndPasswordAreNotNull(email, password)) {
                joinAuth(email, password, toLoginSuccessActivityIntent)
            } else {
                Toast.makeText(this, "이메일과 비밀번호를 모두 입력하세요", Toast.LENGTH_SHORT).show()
            }

        }

        // Anonymous 버튼 클릭시 동작
        btnAnonymousLogin.setOnClickListener {

            anonymousAuth(toLoginSuccessActivityIntent)

        }

    } // end of onCreate()


    /*
        Login Function
     */

    // 사용자가 입력한 email과 password가 모두 null값이 아닌지 확인
    private fun checkIfBothEmailAndPasswordAreNotNull(email: String?, password: String?): Boolean {

        if (email.isNullOrEmpty() or password.isNullOrEmpty()) { // 둘 중 하나라도 null 이면
            return false
        }

        return true
    }

    // Firebase 로그인 과정에서 발생한 오류들에 대한 처리
    private fun checkTypesOfException(exception: Exception) {

        val exceptionToString = exception.toString()

        if (exceptionToString.contains("FirebaseAuthInvalidCredentialsException")) {
            Toast.makeText(this, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show()
        } else if (exceptionToString.contains("FirebaseAuthUserCollisionException")) {
            Toast.makeText(this, "이미 가입된 이메일 입니다.", Toast.LENGTH_SHORT).show()
        } else if (exceptionToString.contains("FirebaseAuthWeakPasswordException")) {
            Toast.makeText(this, "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
        } else if (exceptionToString.contains("FirebaseAuthInvalidUserException")) {
            Toast.makeText(this, "가입된 계정이 없습니다. 회원가입을 먼저 하세요.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, exceptionToString, Toast.LENGTH_SHORT).show()
        }

    }

    private fun emailLoginAuth(email: String, password: String, intent: Intent) { // 이메일 로그인 인증

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(this, "이메일 로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(intent) // LoginSuccessActivity로 이동

                } else {

                    Log.w("TestLogin", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "이메일 로그인 실패", Toast.LENGTH_SHORT).show()
                    checkTypesOfException(task.exception!!)

                }
            }
//            .addOnSuccessListener {
//                Toast.makeText(this, "이메일 로그인 성공", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, firebaseAuth.currentUser?.uid.toString(), Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "이메일 로그인 실패", Toast.LENGTH_SHORT).show()
//            }

    }

    private fun joinAuth(email: String, password: String, intent: Intent) { // 회원가입하고 Intent로 이동

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 회원 가입 성공

                    Toast.makeText(this, "회원 가입 성공", Toast.LENGTH_SHORT).show()
                    startActivity(intent) // LoginSuccessActivity로 이동

                } else { // 실패

                    Log.w("TestLogin", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "회원 가입 실패", Toast.LENGTH_SHORT).show()
                    checkTypesOfException(task.exception!!)

                }
            }

    }

    private fun anonymousAuth(intent: Intent) { // 익명 로그인 인증

        firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                Toast.makeText(
                    this, "익명 로그인 성공",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "익명 로그인 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
} // end of TestLoginActivity
