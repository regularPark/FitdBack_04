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
import com.fitdback.database.ExerciseDataModel
import com.fitdback.posedetection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/*
22.04.26 일종의 개발자 옵션
- 기능 테스트 용도
 */

class DevModeActivity : AppCompatActivity() {

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


        // Data Write Test
        btnFeedbackTest.setOnClickListener {

            val mDialogView =
                LayoutInflater.from(this).inflate(R.layout.dialog_data_write_test, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(mDialogView).setTitle("FeedbackTest 다이얼로그")

            val mAlertDialog = mBuilder.show()

            val btnToFeedbackTestActivity =
                mAlertDialog.findViewById<Button>(R.id.btnToFeedbackTestActivity) // mAlertDialog에서 찾아야함!!

            btnToFeedbackTestActivity?.setOnClickListener { // nullable type(?을 붙여줌) 붙여줘야 'pipe:qemud:wififorward' service 에러가 안남

//                Toast.makeText(this, "버튼 클릭됨", Toast.LENGTH_SHORT).show()

                // <EditText>에서 텍스트 가져오기
                val ex_date: String =
                    mAlertDialog.findViewById<EditText>(R.id.ex_date_area)?.text.toString()

                val ex_type: String =
                    mAlertDialog.findViewById<EditText>(R.id.ex_type_area)?.text.toString()

                val ex_time: Int =
                    mAlertDialog.findViewById<EditText>(R.id.ex_time_area)?.text.toString().toInt()

                val ex_count: Int =
                    mAlertDialog.findViewById<EditText>(R.id.ex_count_area)?.text.toString().toInt()

                val ex_success_count: Int =
                    mAlertDialog.findViewById<EditText>(R.id.ex_success_count_area)?.text.toString().toInt()

                val ex_calorie: Int =
                    mAlertDialog.findViewById<EditText>(R.id.ex_calorie_area)?.text.toString().toInt()

                // DB에 저장할 모델 생성
                val ex_datamodel = ExerciseDataModel(ex_date, ex_type, ex_time, ex_count, ex_success_count, ex_calorie)

                Log.d("datamodel", ex_datamodel.toString())

                // 데이터 저장 path 지정
                val databaseRef =
                    database.reference
                        .child("users")
                        .child(firebaseAuth.currentUser!!.uid)
                        .child("ex_data")
//                val myRefByUserID =
//                    database.getReference("userExerciseInfo").child(firebaseAuth.currentUser!!.uid)

//                myRefByUserID.push().setValue(model) // DB에 중복허용하여 데이터 삽입
//                startActivity(toFeedbackTestActivity)
                databaseRef.push().setValue(ex_datamodel)

                mAlertDialog.dismiss()

            }

        }

        // 데이터 전달 테스트


    }
}