package com.fitdback.test.feedbackTest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dinuscxj.progressbar.CircleProgressBar
import com.fitdback.algorithm.FeedbackAlgorithm
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FeedbackTestActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_test)

        // 프로그레스 바
        val prgBar = findViewById<CircleProgressBar>(R.id.complete_PrgBar)

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val database = Firebase.database

        // 다이얼로그
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_exercise_data_write, null)
        val mBuilder =
            AlertDialog.Builder(this).setView(mDialogView).setTitle("FeedbackTest 다이얼로그")
        val mAlertDialog = mBuilder.show()

        val btnDataWrite =
            mAlertDialog.findViewById<Button>(R.id.btnDataWrite)

        btnDataWrite?.setOnClickListener {

            val dataModel = DataBasket.tempExrModel

            // 데이터를 저장할 path 지정
            val databaseRef =
                database.reference
                    .child("users")
                    .child(firebaseAuth.currentUser!!.uid)
                    .child("ex_data")

            // Data Write
            databaseRef.push().setValue(dataModel)
                .addOnSuccessListener {
                    Toast.makeText(this, "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    mAlertDialog.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "데이터가 저장실패", Toast.LENGTH_SHORT).show()
                }

        }

        prgBar.max = FeedbackAlgorithm.target_cnt
        prgBar.progress = FeedbackAlgorithm.exr_cnt_f
        prgBar.setProgressFormatter { progress, max ->
            val DEFAULT_PATTERN = "%d%"
            String.format(DEFAULT_PATTERN, (progress.toFloat() / max.toFloat() * 100).toInt())
        }

    }

}