package com.fitdback.test.loginTest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.fitdback.posedetection.R
import com.fitdback.test.CustomDialog
import com.fitdback.userinterface.MainTestActivity
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class LoginTestActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_test)

        firebaseAuth = FirebaseAuth.getInstance()

        // 레이아웃
        val btnRunEmailLogin = findViewById<Button>(R.id.btnRunEmailLogin)
        val btnRunGoogleLogin = findViewById<Button>(R.id.btnRunGoogleLogin)
        val btnRunDevLogin = findViewById<Button>(R.id.btnRunDevLogin)
        val joinTextView = findViewById<TextView>(R.id.joinTextView)

        // 인텐트
        val toMainActivity = Intent(this, MainTestActivity::class.java)

        // 클릭 이벤트
        btnRunEmailLogin.setOnClickListener {
            val dialog = CustomDialog(this, R.layout.dialog_login_email_login, "Email Login")
            val mAlertDialog = dialog.showDialog()

            // 레이아웃의 EditText 에서 email과 password를 읽어들인다.
            val email =
                mAlertDialog!!.findViewById<EditText>(R.id.loginEmailArea)!!.text.toString().trim()
            val password =
                mAlertDialog.findViewById<EditText>(R.id.loginPasswordArea)!!.text.toString().trim()

            if (checkIfBothEmailAndPasswordAreNotNull(email, password)) {
                emailLoginAuth(
                    email,
                    password,
                    toMainActivity
                )
            } else {
                Toast.makeText(this, "이메일과 비밀번호를 모두 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        btnRunGoogleLogin.setOnClickListener {

        }

        btnRunDevLogin.setOnClickListener {
            val dialog = CustomDialog(this, R.layout.dialog_dev_login, "Email Login")
            val mAlertDialog = dialog.showDialog()

            val btnKshLogin =
                mAlertDialog!!.findViewById<Button>(R.id.btnKshLogin)

            val btnOmsLogin =
                mAlertDialog.findViewById<Button>(R.id.btnOmsLogin)

            val btnPjkLogin =
                mAlertDialog.findViewById<Button>(R.id.btnPjkLogin)

            btnKshLogin?.setOnClickListener {
                emailLoginAuth("ksh@gmail.com", "123456", toMainActivity)
            }
            btnOmsLogin?.setOnClickListener {
                emailLoginAuth("oms@gmail.com", "123456", toMainActivity)
            }
            btnPjkLogin?.setOnClickListener {
                emailLoginAuth("pjk@gmail.com", "123456", toMainActivity)
            }
        }

        joinTextView.setOnClickListener {
            val dialog = CustomDialog(this, R.layout.dialog_login_join, "Email Join")
            val mAlertDialog = dialog.showDialog()

            val btnEmailJoin =
                mAlertDialog!!.findViewById<Button>(R.id.btnEmailJoin)
            val btnGoogleJoin =
                mAlertDialog.findViewById<Button>(R.id.btnGoogleJoin)

            // 이메일 회원가입 다이얼로그 실행
            btnEmailJoin?.setOnClickListener {
                val emailJoinDialog =
                    CustomDialog(this, R.layout.dialog_login_join_email, "Email Join")
                val emailJoinAlertDialog = emailJoinDialog.showDialog()

                val email =
                    emailJoinAlertDialog!!.findViewById<EditText>(R.id.joinEmailArea)!!.text.toString()
                        .trim()
                val password =
                    emailJoinAlertDialog.findViewById<EditText>(R.id.joinPasswordArea)!!.text.toString()
                        .trim()
                val passwordCheck =
                    emailJoinAlertDialog.findViewById<EditText>(R.id.joinPasswordCheckArea)!!.text.toString()
                        .trim()
                val nickname =
                    emailJoinAlertDialog.findViewById<EditText>(R.id.joinNicknameArea)!!.text.toString()
                        .trim()
                val height =
                    emailJoinAlertDialog.findViewById<EditText>(R.id.joinHeightArea)!!.text.toString()
                        .trim()
                val weight =
                    emailJoinAlertDialog.findViewById<EditText>(R.id.joinWeightArea)!!.text.toString()
                        .trim()

                // Firebase Join 처리
                val btnRunFirebaseEmailJoin =
                    emailJoinAlertDialog.findViewById<Button>(R.id.btnRunFirebaseEmailJoin)

                btnRunFirebaseEmailJoin?.setOnClickListener {
                    val joinAvailability = checkJoinAvailability(
                        email,
                        password,
                        passwordCheck,
                        nickname,
                        height,
                        weight
                    )
                    var toastMessage = ""

                }

                // Firebase에 writing


            }

            btnGoogleJoin?.setOnClickListener {
                // TODO : 구글 회원가입 처리
            }

        }

    }

    // 공란이 있는지 체크
    private fun checkIfBothEmailAndPasswordAreNotNull(email: String?, password: String?): Boolean {

        if (email.isNullOrEmpty() or password.isNullOrEmpty()) { // 둘 중 하나라도 null 이면
            return false
        }

        return true
    }

    private fun checkJoinAvailability(
        email: String,
        password: String,
        passwordCheck: String,
        nickname: String,
        height: String,
        weight: String
    ): String {

        val isBlankExists = email.isEmpty() || password.isEmpty() ||
                passwordCheck.isEmpty() || nickname.isEmpty() || height.isEmpty() || weight.isEmpty()

        return if (isBlankExists) {
            "blankExists"
        } else {
            if (password != passwordCheck) {
                "passwordNotMatch"
            } else {
                "joinAvailable"
            }
        }
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
                    startActivity(intent)
                    finish() // 액티비티가 두개 존재하는 오류 수정!


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

    private fun emailJoinAuth(
        email: String,
        password: String,
        intent: Intent
    ) { // 회원가입하고 Intent로 이동

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 회원 가입 성공

                    Toast.makeText(this, "회원 가입 성공", Toast.LENGTH_SHORT).show()
                    startActivity(intent) // LoginSuccessActivity로 이동
                    finish() // 액티비티가 두개 존재하는 오류 수정!


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
                finish() // 액티비티가 두개 존재하는 오류 수정!

            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "익명 로그인 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
}