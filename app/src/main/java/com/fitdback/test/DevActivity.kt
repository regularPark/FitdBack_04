package com.fitdback.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fitdback.posedetection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/*
22.04.26 일종의 개발자 옵션
- 기능 테스트 용도
 */

class DevActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev)

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val database = Firebase.database
        val myRef = database.getReference("userExerciseInfo")

        // 레이아웃 연결
        val userIDArea = findViewById<TextView>(R.id.userIDArea)
        val btnDBTest = findViewById<Button>(R.id.btnDBTest)
        val btnFeedbackTest = findViewById<Button>(R.id.btnFeedbackTest)

        // Intent
        val toHealthMemoTestActivity = Intent(this, HealthMemoTestActivity::class.java)
        val toFeedbackTestActivity = Intent(this, FeedbackTestActivity::class.java)

        // 로그인에 성공해서 현재 액티비티로 전환되면, user의 ID를 화면에 표시
        userIDArea.text = firebaseAuth.currentUser?.uid


        /* 버튼 관련 동작 */

        // btnDBTest
        btnDBTest.setOnClickListener {

            startActivity(toHealthMemoTestActivity)

        }


        // Feedback Test
        btnFeedbackTest.setOnClickListener {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_feedback_test, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(mDialogView).setTitle("FeedbackTest 다이얼로그")

            val mAlertDialog = mBuilder.show()

            val btnToFeedbackTestActivity =
                mAlertDialog.findViewById<Button>(R.id.btnToFeedbackTestActivity) // mAlertDialog에서 찾아야함!!

            btnToFeedbackTestActivity?.setOnClickListener { // nullable type(?을 붙여줌) 붙여줘야 'pipe:qemud:wififorward' service 에러가 안남

//                Toast.makeText(this, "버튼 클릭됨", Toast.LENGTH_SHORT).show()

                // <EditText>에서 텍스트 가져오기
                val totalCount =
                    mAlertDialog.findViewById<EditText>(R.id.totalCountArea)?.text.toString()
                        .toInt()
                val successCount =
                    mAlertDialog.findViewById<EditText>(R.id.successCountArea)?.text.toString()
                        .toInt()
                val exerciseTime =
                    mAlertDialog.findViewById<EditText>(R.id.exerciseTimeArea)?.text.toString()
                        .toInt()

                // DB에 저장할 모델 생성
                val model = FeedbackTestDataModel(totalCount, successCount, exerciseTime)
                Log.d("datamodel", model.toString())

                val databaseRef =
                    database.reference
                        .child("users")
                        .child(firebaseAuth.currentUser!!.uid) // uid 밑의 exerciseInfo 에 저장
                        .child("exerciseInfo")
//                val myRefByUserID =
//                    database.getReference("userExerciseInfo").child(firebaseAuth.currentUser!!.uid)

//                myRefByUserID.push().setValue(model) // DB에 중복허용하여 데이터 삽입
//                startActivity(toFeedbackTestActivity)
                databaseRef.push().setValue(model)

                mAlertDialog.dismiss()

            }
        }

        // 데이터 전달 테스트


    }
}